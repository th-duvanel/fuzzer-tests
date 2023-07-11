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
import de.rub.nds.tlsattacker.core.protocol.message.PskRsaClientKeyExchangeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PskRsaClientKeyExchangeSerializer
        extends RSAClientKeyExchangeSerializer<PskRsaClientKeyExchangeMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final PskRsaClientKeyExchangeMessage msg;

    /**
     * Constructor for the PSKRSAClientKeyExchangeSerializer
     *
     * @param message Message that should be serialized
     * @param version Version of the Protocol
     */
    public PskRsaClientKeyExchangeSerializer(
            PskRsaClientKeyExchangeMessage message, ProtocolVersion version) {
        super(message, version);
        this.msg = message;
    }

    @Override
    public byte[] serializeHandshakeMessageContent() {
        LOGGER.debug("Serializing PSKRSAClientKeyExchangeMessage");
        writePSKIdentityLength(msg);
        writePSKIdentity(msg);
        super.serializeRsaParams();
        return getAlreadySerialized();
    }

    /**
     * Writes the SerializedPublicKeyLength of the PskRsaClientKeyExchangeMessage into the final
     * byte[]
     */
    private void writePSKIdentityLength(PskRsaClientKeyExchangeMessage msg) {
        appendInt(msg.getIdentityLength().getValue(), HandshakeByteLength.PSK_IDENTITY_LENGTH);
        LOGGER.debug("SerializedPSKIdentityLength: " + msg.getIdentityLength().getValue());
    }

    /**
     * Writes the SerializedPublicKey of the PskRsaClientKeyExchangeMessage into the final byte[]
     */
    private void writePSKIdentity(PskRsaClientKeyExchangeMessage msg) {
        appendBytes(msg.getIdentity().getValue());
        LOGGER.debug("SerializedPSKIdentity: {}", msg.getIdentity().getValue());
    }
}
