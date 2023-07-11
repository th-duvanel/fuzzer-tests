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
import de.rub.nds.tlsattacker.core.constants.CertificateVerifyConstants;
import de.rub.nds.tlsattacker.core.constants.SignatureAndHashAlgorithm;
import de.rub.nds.tlsattacker.core.crypto.SSLUtils;
import de.rub.nds.tlsattacker.core.crypto.SignatureCalculator;
import de.rub.nds.tlsattacker.core.exceptions.CryptoException;
import de.rub.nds.tlsattacker.core.protocol.message.CertificateVerifyMessage;
import de.rub.nds.tlsattacker.core.workflow.chooser.Chooser;
import de.rub.nds.tlsattacker.transport.ConnectionEndType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CertificateVerifyPreparator
        extends HandshakeMessagePreparator<CertificateVerifyMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    private SignatureAndHashAlgorithm algorithm;
    private byte[] signature;
    private final CertificateVerifyMessage msg;

    public CertificateVerifyPreparator(Chooser chooser, CertificateVerifyMessage message) {
        super(chooser, message);
        this.msg = message;
    }

    @Override
    public void prepareHandshakeMessageContents() {
        LOGGER.debug("Preparing CertificateVerifyMessage");
        algorithm = chooser.getSelectedSigHashAlgorithm();
        signature = new byte[0];
        try {
            signature = createSignature();
        } catch (CryptoException e) {
            LOGGER.warn("Could not generate Signature! Using empty one instead!", e);
        }
        prepareSignature(msg);
        prepareSignatureLength(msg);
        prepareSignatureHashAlgorithm(msg);
    }

    private byte[] createSignature() throws CryptoException {
        byte[] toBeSigned = chooser.getContext().getTlsContext().getDigest().getRawBytes();
        if (chooser.getSelectedProtocolVersion().isTLS13()) {
            if (chooser.getConnectionEndType() == ConnectionEndType.CLIENT) {
                toBeSigned =
                        ArrayConverter.concatenate(
                                ArrayConverter.hexStringToByteArray(
                                        "2020202020202020202020202020202020202020202020202020"
                                                + "2020202020202020202020202020202020202020202020202020202020202020202020202020"),
                                CertificateVerifyConstants.CLIENT_CERTIFICATE_VERIFY.getBytes(),
                                new byte[] {(byte) 0x00},
                                chooser.getContext()
                                        .getTlsContext()
                                        .getDigest()
                                        .digest(
                                                chooser.getSelectedProtocolVersion(),
                                                chooser.getSelectedCipherSuite()));
            } else {
                toBeSigned =
                        ArrayConverter.concatenate(
                                ArrayConverter.hexStringToByteArray(
                                        "2020202020202020202020202020202020202020202020202020"
                                                + "2020202020202020202020202020202020202020202020202020202020202020202020202020"),
                                CertificateVerifyConstants.SERVER_CERTIFICATE_VERIFY.getBytes(),
                                new byte[] {(byte) 0x00},
                                chooser.getContext()
                                        .getTlsContext()
                                        .getDigest()
                                        .digest(
                                                chooser.getSelectedProtocolVersion(),
                                                chooser.getSelectedCipherSuite()));
            }
        } else if (chooser.getSelectedProtocolVersion().isSSL()) {
            final byte[] handshakeMessageContent =
                    chooser.getContext().getTlsContext().getDigest().getRawBytes();
            final byte[] masterSecret = chooser.getMasterSecret();
            return SSLUtils.calculateSSLCertificateVerifySignature(
                    handshakeMessageContent, masterSecret);
        }
        algorithm = chooser.getSelectedSigHashAlgorithm();
        return SignatureCalculator.generateSignature(algorithm, chooser, toBeSigned);
    }

    private void prepareSignature(CertificateVerifyMessage msg) {
        msg.setSignature(signature);
        LOGGER.debug("Signature: {}", msg.getSignature().getValue());
    }

    private void prepareSignatureLength(CertificateVerifyMessage msg) {
        msg.setSignatureLength(msg.getSignature().getValue().length);
        LOGGER.debug("SignatureLength: " + msg.getSignatureLength().getValue());
    }

    private void prepareSignatureHashAlgorithm(CertificateVerifyMessage msg) {
        msg.setSignatureHashAlgorithm(algorithm.getByteValue());
        LOGGER.debug("SignatureHasAlgorithm: {}", msg.getSignatureHashAlgorithm().getValue());
    }
}
