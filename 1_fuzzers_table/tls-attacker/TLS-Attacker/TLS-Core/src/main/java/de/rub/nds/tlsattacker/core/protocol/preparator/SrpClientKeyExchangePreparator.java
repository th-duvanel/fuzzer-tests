/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.protocol.preparator;

import de.rub.nds.modifiablevariable.util.ArrayConverter;
import de.rub.nds.tlsattacker.core.protocol.message.SrpClientKeyExchangeMessage;
import de.rub.nds.tlsattacker.core.workflow.chooser.Chooser;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SrpClientKeyExchangePreparator
        extends ClientKeyExchangePreparator<SrpClientKeyExchangeMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    private BigInteger clientPublicKey;
    private byte[] premasterSecret;
    private byte[] random;
    private final SrpClientKeyExchangeMessage msg;

    public SrpClientKeyExchangePreparator(Chooser chooser, SrpClientKeyExchangeMessage msg) {
        super(chooser, msg);
        this.msg = msg;
    }

    @Override
    public void prepareHandshakeMessageContents() {
        LOGGER.debug("Preparing SRPClientExchangeMessage");
        msg.prepareComputations();
        setComputationGenerator(msg);
        setComputationModulus(msg);
        setComputationPrivateKey(msg);
        setComputationServerPublicKey(msg);
        setComputationSalt(msg);

        setSRPIdentity(msg);
        setSRPPassword(msg);

        clientPublicKey =
                calculatePublicKey(
                        msg.getComputations().getGenerator().getValue(),
                        msg.getComputations().getModulus().getValue(),
                        msg.getComputations().getPrivateKey().getValue());
        prepareModulus(msg);
        prepareModulusLength(msg);
        prepareGenerator(msg);
        prepareGeneratorLength(msg);
        prepareSalt(msg);
        prepareSaltLength(msg);
        preparePublicKey(msg);
        preparePublicKeyLength(msg);
        premasterSecret =
                calculateClientPremasterSecret(
                        msg.getComputations().getModulus().getValue(),
                        msg.getComputations().getGenerator().getValue(),
                        msg.getComputations().getPrivateKey().getValue(),
                        msg.getComputations().getServerPublicKey().getValue(),
                        clientPublicKey,
                        msg.getComputations().getSalt().getValue(),
                        msg.getComputations().getSRPIdentity().getValue(),
                        msg.getComputations().getSRPPassword().getValue());
        preparePremasterSecret(msg);
        prepareClientServerRandom(msg);
    }

    private BigInteger calculatePublicKey(
            BigInteger generator, BigInteger modulus, BigInteger privateKey) {
        if (modulus.compareTo(BigInteger.ZERO) == 1) {
            return generator.modPow(privateKey, modulus);
        } else {
            LOGGER.warn("Modulusis smaller than zero, using zero as the public key");
            return BigInteger.ZERO;
        }
    }

    private byte[] calculateClientPremasterSecret(
            BigInteger modulus,
            BigInteger generator,
            BigInteger privateKey,
            BigInteger serverPublicKey,
            BigInteger clientPublicKey,
            byte[] salt,
            byte[] identity,
            byte[] password) {
        // PremasterSecret: (ServerPublicKey -(k * g^x))^(ClientPrivatKey +(u *
        // x)) % modulus
        if (modulus.compareTo(BigInteger.ZERO) == 1) {

            BigInteger u = calculateU(clientPublicKey, serverPublicKey, modulus);
            LOGGER.debug("Intermediate Value U {}", ArrayConverter.bigIntegerToByteArray(u));
            BigInteger k = calculateSRP6Multiplier(modulus, generator);
            BigInteger x = calculateX(salt, identity, password);
            LOGGER.debug("Intermediate Value X {}", ArrayConverter.bigIntegerToByteArray(x));
            BigInteger helpValue1 = generator.modPow(x, modulus);
            LOGGER.debug(
                    "Intermediate Value V {}", ArrayConverter.bigIntegerToByteArray(helpValue1));
            BigInteger helpValue2 = k.multiply(helpValue1);
            BigInteger helpValue3 = helpValue2.mod(modulus);
            // helpValue1 = helpValue2.subtract(serverPublicKey);
            helpValue1 = serverPublicKey.subtract(helpValue3);
            helpValue2 = helpValue1.mod(modulus);
            helpValue3 = u.multiply(x);
            helpValue1 = helpValue3.mod(modulus);
            helpValue3 = privateKey.add(helpValue1);
            helpValue1 = helpValue3.mod(modulus);
            helpValue3 = helpValue2.modPow(helpValue1, modulus);

            return ArrayConverter.bigIntegerToByteArray(helpValue3);
        } else {
            LOGGER.warn("Modulus is smaller than zero, using new byte[0] as the pms");
            return new byte[0];
        }
    }

    private byte[] calculatePremasterSecretServer(
            BigInteger modulus,
            BigInteger generator,
            BigInteger serverPrivateKey,
            BigInteger serverPublicKey,
            BigInteger clientPublicKey,
            byte[] salt,
            byte[] identity,
            byte[] password) {
        // PremasterSecret: (ClientPublicKey * v^u) ^ServerPrivatKey % modulus
        BigInteger u = calculateU(clientPublicKey, serverPublicKey, modulus);
        LOGGER.debug("Intermediate Value U {}", () -> ArrayConverter.bigIntegerToByteArray(u));
        BigInteger x = calculateX(salt, identity, password);
        LOGGER.debug("Intermediate Value X {}", () -> ArrayConverter.bigIntegerToByteArray(x));
        BigInteger v = calculateV(x, generator, modulus);
        LOGGER.debug("Intermediate Value V {}", () -> ArrayConverter.bigIntegerToByteArray(v));
        BigInteger helpValue1 = v.modPow(u, modulus);
        LOGGER.debug("v^u {}", ArrayConverter.bigIntegerToByteArray(helpValue1));
        BigInteger helpValue2 = clientPublicKey.multiply(helpValue1);
        BigInteger helpValue3 = helpValue2.mod(modulus);
        LOGGER.debug("A * v^u {}", () -> ArrayConverter.bigIntegerToByteArray(helpValue3));
        helpValue1 = helpValue3.modPow(serverPrivateKey, modulus);
        LOGGER.debug("PremasterSecret {}", ArrayConverter.bigIntegerToByteArray(helpValue1));
        return ArrayConverter.bigIntegerToByteArray(helpValue1);
    }

    private BigInteger calculateV(BigInteger x, BigInteger generator, BigInteger modulus) {
        BigInteger v = generator.modPow(x, modulus);
        return v;
    }

    private BigInteger calculateU(
            BigInteger clientPublic, BigInteger serverPublic, BigInteger modulus) {
        byte[] paddedClientPublic = calculatePadding(modulus, clientPublic);
        LOGGER.debug(
                "ClientPublic Key: {}", () -> ArrayConverter.bigIntegerToByteArray(clientPublic));
        LOGGER.debug("PaddedClientPublic. {}", paddedClientPublic);
        byte[] paddedServerPublic = calculatePadding(modulus, serverPublic);
        LOGGER.debug(
                "ServerPublic Key: {}", () -> ArrayConverter.bigIntegerToByteArray(serverPublic));
        LOGGER.debug("PaddedServerPublic. {}", paddedServerPublic);
        byte[] hashInput = ArrayConverter.concatenate(paddedClientPublic, paddedServerPublic);
        LOGGER.debug("HashInput for u: {}", hashInput);
        byte[] hashOutput = shaSum(hashInput);
        LOGGER.debug("HashValue for u: {}", hashOutput);
        return new BigInteger(1, hashOutput);
    }

    private byte[] calculatePadding(BigInteger modulus, BigInteger toPad) {
        byte[] padding;
        int modulusByteLength = ArrayConverter.bigIntegerToByteArray(modulus).length;
        byte[] paddingArray = ArrayConverter.bigIntegerToByteArray(toPad);
        if (modulusByteLength == paddingArray.length) {
            return paddingArray;
        }
        int paddingByteLength = modulusByteLength - paddingArray.length;
        if (paddingByteLength < 0) {
            LOGGER.warn("Negative SRP Padding Size. Using 0");
            paddingByteLength = 0;
        }
        padding = new byte[paddingByteLength];
        return ArrayConverter.concatenate(padding, paddingArray);
    }

    public BigInteger calculateX(byte[] salt, byte[] identity, byte[] password) {
        byte[] hashInput1 =
                ArrayConverter.concatenate(
                        identity, ArrayConverter.hexStringToByteArray("3A"), password);
        LOGGER.debug("HashInput for hashInput1: {}", hashInput1);
        byte[] hashOutput1 = shaSum(hashInput1);
        LOGGER.debug("HashValue for hashInput1: {}", hashOutput1);
        byte[] hashInput2 = ArrayConverter.concatenate(salt, hashOutput1);
        LOGGER.debug("HashInput for hashInput2: {}", hashInput2);
        byte[] hashOutput2 = shaSum(hashInput2);
        LOGGER.debug("HashValue for hashInput2: {}", hashOutput2);
        return new BigInteger(1, hashOutput2);
    }

    private BigInteger calculateSRP6Multiplier(BigInteger modulus, BigInteger generator) {
        BigInteger srp6Multiplier;
        byte[] paddedGenerator = calculatePadding(modulus, generator);
        byte[] hashInput =
                ArrayConverter.concatenate(
                        ArrayConverter.bigIntegerToByteArray(modulus), paddedGenerator);
        LOGGER.debug("HashInput SRP6Multi: {}", hashInput);
        byte[] hashOutput = shaSum(hashInput);
        return new BigInteger(1, hashOutput);
    }

    public byte[] shaSum(byte[] toHash) {
        MessageDigest dig = null;
        try {
            dig = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.warn(ex);
        }
        dig.update(toHash);
        return dig.digest();
    }

    private void setComputationGenerator(SrpClientKeyExchangeMessage msg) {
        msg.getComputations().setGenerator(chooser.getSRPGenerator());
        LOGGER.debug("Generator: " + msg.getComputations().getGenerator().getValue());
    }

    private void setComputationModulus(SrpClientKeyExchangeMessage msg) {
        msg.getComputations().setModulus(chooser.getSRPModulus());
        LOGGER.debug("Modulus: " + msg.getComputations().getModulus().getValue());
    }

    private void preparePremasterSecret(SrpClientKeyExchangeMessage msg) {
        msg.getComputations().setPremasterSecret(premasterSecret);
        premasterSecret = msg.getComputations().getPremasterSecret().getValue();
        LOGGER.debug("PremasterSecret: {}", msg.getComputations().getPremasterSecret().getValue());
    }

    private void preparePublicKey(SrpClientKeyExchangeMessage msg) {
        msg.setPublicKey(clientPublicKey.toByteArray());
        LOGGER.debug("PublicKey: {}", msg.getPublicKey().getValue());
    }

    private void preparePublicKeyLength(SrpClientKeyExchangeMessage msg) {
        msg.setPublicKeyLength(msg.getPublicKey().getValue().length);
        LOGGER.debug("PublicKeyLength: " + msg.getPublicKeyLength().getValue());
    }

    private void prepareClientServerRandom(SrpClientKeyExchangeMessage msg) {
        random = ArrayConverter.concatenate(chooser.getClientRandom(), chooser.getServerRandom());
        msg.getComputations().setClientServerRandom(random);
        random = msg.getComputations().getClientServerRandom().getValue();
        LOGGER.debug(
                "ClientServerRandom: {}", msg.getComputations().getClientServerRandom().getValue());
    }

    @Override
    public void prepareAfterParse(boolean clientMode) {
        if (!clientMode) {
            BigInteger privateKey = chooser.getSRPServerPrivateKey();
            BigInteger clientPublic = new BigInteger(1, msg.getPublicKey().getValue());
            msg.prepareComputations();
            premasterSecret =
                    calculatePremasterSecretServer(
                            chooser.getSRPModulus(),
                            chooser.getSRPGenerator(),
                            privateKey,
                            chooser.getSRPServerPublicKey(),
                            clientPublic,
                            chooser.getSRPServerSalt(),
                            chooser.getSRPIdentity(),
                            chooser.getSRPPassword());
            preparePremasterSecret(msg);
            prepareClientServerRandom(msg);
        }
    }

    private void setComputationPrivateKey(SrpClientKeyExchangeMessage msg) {
        msg.getComputations().setPrivateKey(chooser.getSRPClientPrivateKey());
        LOGGER.debug(
                "Computation PrivateKey: "
                        + msg.getComputations().getPrivateKey().getValue().toString());
    }

    private void setComputationServerPublicKey(SrpClientKeyExchangeMessage msg) {
        msg.getComputations().setServerPublicKey(chooser.getSRPServerPublicKey());
        LOGGER.debug(
                "Computation PublicKey: "
                        + msg.getComputations().getServerPublicKey().getValue().toString());
    }

    private void prepareSalt(SrpClientKeyExchangeMessage msg) {
        msg.setSalt(msg.getComputations().getSalt());
        LOGGER.debug("Salt: {}", msg.getSalt().getValue());
    }

    private void prepareSaltLength(SrpClientKeyExchangeMessage msg) {
        msg.setSaltLength(msg.getSalt().getValue().length);
        LOGGER.debug("Salt Length: {}", msg.getSaltLength().getValue());
    }

    private void setSRPIdentity(SrpClientKeyExchangeMessage msg) {
        msg.getComputations().setSRPIdentity(chooser.getSRPIdentity());
        LOGGER.debug(
                "SRP Identity used for Computations: " + msg.getComputations().getSRPIdentity());
    }

    private void setSRPPassword(SrpClientKeyExchangeMessage msg) {
        msg.getComputations().setSRPPassword(chooser.getSRPPassword());
        LOGGER.debug(
                "SRP Password used for Computations: " + msg.getComputations().getSRPPassword());
    }

    private void setComputationSalt(SrpClientKeyExchangeMessage msg) {
        msg.getComputations().setSalt(chooser.getSRPServerSalt());
        LOGGER.debug("Salt used for Computations: " + msg.getComputations().getSalt());
    }

    private void prepareGenerator(SrpClientKeyExchangeMessage msg) {
        msg.setGenerator(msg.getComputations().getGenerator().getByteArray());
        LOGGER.debug("Generator: {}", msg.getGenerator().getValue());
    }

    private void prepareModulus(SrpClientKeyExchangeMessage msg) {
        msg.setModulus(msg.getComputations().getModulus().getByteArray());
        LOGGER.debug("Modulus: {}", msg.getModulus().getValue());
    }

    private void prepareGeneratorLength(SrpClientKeyExchangeMessage msg) {
        msg.setGeneratorLength(msg.getGenerator().getValue().length);
        LOGGER.debug("Generator Length: " + msg.getGeneratorLength().getValue());
    }

    private void prepareModulusLength(SrpClientKeyExchangeMessage msg) {
        msg.setModulusLength(msg.getModulus().getValue().length);
        LOGGER.debug("Modulus Length: " + msg.getModulusLength().getValue());
    }
}
