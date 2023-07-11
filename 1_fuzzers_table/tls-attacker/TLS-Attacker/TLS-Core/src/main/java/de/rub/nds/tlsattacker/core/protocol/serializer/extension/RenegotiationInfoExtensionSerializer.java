/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.protocol.serializer.extension;

import de.rub.nds.tlsattacker.core.constants.ExtensionByteLength;
import de.rub.nds.tlsattacker.core.protocol.message.extension.RenegotiationInfoExtensionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RenegotiationInfoExtensionSerializer
        extends ExtensionSerializer<RenegotiationInfoExtensionMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final RenegotiationInfoExtensionMessage message;

    public RenegotiationInfoExtensionSerializer(RenegotiationInfoExtensionMessage message) {
        super(message);
        this.message = message;
    }

    @Override
    public byte[] serializeExtensionContent() {
        appendInt(
                message.getRenegotiationInfoLength().getValue(),
                ExtensionByteLength.RENEGOTIATION_INFO);
        appendBytes(message.getRenegotiationInfo().getValue());
        LOGGER.debug(
                "Serialized RenegotiationInfo extension with info of length "
                        + message.getRenegotiationInfo().getValue().length);
        return getAlreadySerialized();
    }
}
