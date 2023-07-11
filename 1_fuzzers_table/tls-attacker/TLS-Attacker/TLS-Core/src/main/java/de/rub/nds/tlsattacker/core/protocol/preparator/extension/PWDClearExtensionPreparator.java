/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.protocol.preparator.extension;

import de.rub.nds.tlsattacker.core.protocol.message.extension.PWDClearExtensionMessage;
import de.rub.nds.tlsattacker.core.workflow.chooser.Chooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PWDClearExtensionPreparator extends ExtensionPreparator<PWDClearExtensionMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final PWDClearExtensionMessage msg;

    public PWDClearExtensionPreparator(Chooser chooser, PWDClearExtensionMessage message) {
        super(chooser, message);
        this.msg = message;
    }

    @Override
    public void prepareExtensionContent() {
        LOGGER.debug("Preparing PWDClearExtension");
        prepareUsername(msg);
        prepareUsernameLength(msg);
    }

    private void prepareUsername(PWDClearExtensionMessage msg) {
        msg.setUsername(chooser.getClientPWDUsername());
        LOGGER.debug("Username: " + msg.getUsername().getValue());
    }

    private void prepareUsernameLength(PWDClearExtensionMessage msg) {
        msg.setUsernameLength(msg.getUsername().getValue().length());
        LOGGER.debug("UsernameLength: " + msg.getUsernameLength().getValue());
    }
}
