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
import de.rub.nds.tlsattacker.core.protocol.message.SrpClientKeyExchangeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SrpClientKeyExchangeSerializer
        extends ClientKeyExchangeSerializer<SrpClientKeyExchangeMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final SrpClientKeyExchangeMessage msg;

    /**
     * Constructor for the DHClientKeyExchangeSerializer
     *
     * @param message Message that should be serialized
     */
    public SrpClientKeyExchangeSerializer(SrpClientKeyExchangeMessage message) {
        super(message);
        this.msg = message;
    }

    @Override
    public byte[] serializeHandshakeMessageContent() {
        LOGGER.debug("Serializing SRPClientKeyExchangeMessage");
        writeSerializedPublicKeyLength(msg);
        writeSerializedPublicKey(msg);
        return getAlreadySerialized();
    }

    /**
     * Writes the SerializedPublicKeyLength of the SrpClientKeyExchangeMessage into the final byte[]
     */
    private void writeSerializedPublicKeyLength(SrpClientKeyExchangeMessage msg) {
        appendInt(msg.getPublicKeyLength().getValue(), HandshakeByteLength.SRP_PUBLICKEY_LENGTH);
        LOGGER.debug("SerializedPublicKexLength: " + msg.getPublicKeyLength().getValue());
    }

    /** Writes the SerializedPublicKey of the SrpClientKeyExchangeMessage into the final byte[] */
    private void writeSerializedPublicKey(SrpClientKeyExchangeMessage msg) {
        appendBytes(msg.getPublicKey().getValue());
        LOGGER.debug("SerializedPublicKey: {}", msg.getPublicKey().getValue());
    }
}
