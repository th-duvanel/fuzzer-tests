/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.protocol.serializer;

import de.rub.nds.tlsattacker.core.constants.HandshakeByteLength;
import de.rub.nds.tlsattacker.core.constants.ProtocolVersion;
import de.rub.nds.tlsattacker.core.constants.SignatureAlgorithm;
import de.rub.nds.tlsattacker.core.constants.SignatureAndHashAlgorithm;
import de.rub.nds.tlsattacker.core.protocol.message.ECDHEServerKeyExchangeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ECDHEServerKeyExchangeSerializer<T extends ECDHEServerKeyExchangeMessage>
        extends ServerKeyExchangeSerializer<T> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final T msg;

    /**
     * Constructor for the ECDHServerKeyExchangerSerializer
     *
     * @param message Message that should be serialized
     * @param version Version of the Protocol
     */
    public ECDHEServerKeyExchangeSerializer(T message, ProtocolVersion version) {
        super(message, version);
        this.msg = message;
    }

    @Override
    public byte[] serializeHandshakeMessageContent() {
        LOGGER.debug("Serializing ECDHEServerKeyExchangeMessage");
        writeCurveType(msg);
        writeNamedGroup(msg);
        writeSerializedPublicKeyLength(msg);
        writeSerializedPublicKey(msg);
        if (isTLS12() || isDTLS12()) {
            writeSignatureAndHashAlgorithm(msg);
            SignatureAndHashAlgorithm sigHashAlg =
                    SignatureAndHashAlgorithm.getSignatureAndHashAlgorithm(
                            msg.getSignatureAndHashAlgorithm().getValue());
            if (sigHashAlg == null
                    || sigHashAlg.getSignatureAlgorithm() != SignatureAlgorithm.ANONYMOUS) {
                writeSignatureLength(msg);
                writeSignature(msg);
            }
        } else {
            writeSignatureLength(msg);
            writeSignature(msg);
        }

        return getAlreadySerialized();
    }

    protected byte[] serializeEcDheParams() {
        writeCurveType(msg);
        writeNamedGroup(msg);
        writeSerializedPublicKeyLength(msg);
        writeSerializedPublicKey(msg);
        return getAlreadySerialized();
    }

    /** Writes the CurveType of the ECDHEServerKeyExchangeMessage into the final byte[] */
    private void writeCurveType(T msg) {
        appendByte(msg.getGroupType().getValue());
        LOGGER.debug("CurveType: " + msg.getGroupType().getValue());
    }

    /** Writes the NamedCurve of the ECDHEServerKeyExchangeMessage into the final byte[] */
    private void writeNamedGroup(T msg) {
        appendBytes(msg.getNamedGroup().getValue());
        LOGGER.debug("NamedGroup: {}", msg.getNamedGroup().getValue());
    }

    /**
     * Writes the SerializedPublicKeyLength of the ECDHEServerKeyExchangeMessage into the final
     * byte[]
     */
    private void writeSerializedPublicKeyLength(T msg) {
        appendInt(msg.getPublicKeyLength().getValue(), HandshakeByteLength.ECDHE_PARAM_LENGTH);
        LOGGER.debug("SerializedPublicKeyLength: " + msg.getPublicKeyLength().getValue());
    }

    /** Writes the SerializedPublicKey of the ECDHEServerKeyExchangeMessage into the final byte[] */
    private void writeSerializedPublicKey(T msg) {
        appendBytes(msg.getPublicKey().getValue());
        LOGGER.debug("SerializedPublicKey: {}", msg.getPublicKey().getValue());
    }

    /**
     * Writes the SignatureAndHashAlgorithm of the ECDHEServerKeyExchangeMessage into the final
     * byte[]
     */
    private void writeSignatureAndHashAlgorithm(T msg) {
        appendBytes(msg.getSignatureAndHashAlgorithm().getValue());
        LOGGER.debug(
                "SignatureAndHashAlgorithm: {}", msg.getSignatureAndHashAlgorithm().getValue());
    }

    /** Writes the SignatureLength of the ECDHEServerKeyExchangeMessage into the final byte[] */
    private void writeSignatureLength(T msg) {
        appendInt(msg.getSignatureLength().getValue(), HandshakeByteLength.SIGNATURE_LENGTH);
        LOGGER.debug("SignatureLength: " + msg.getSignatureLength().getValue());
    }

    /** Writes the Signature of the ECDHEServerKeyExchangeMessage into the final byte[] */
    private void writeSignature(T msg) {
        appendBytes(msg.getSignature().getValue());
        LOGGER.debug("Signature: {}", msg.getSignature().getValue());
    }
}
