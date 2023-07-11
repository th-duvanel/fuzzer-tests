/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.protocol.preparator.extension;

import de.rub.nds.tlsattacker.core.constants.AuthzDataFormat;
import de.rub.nds.tlsattacker.core.protocol.message.extension.ClientAuthzExtensionMessage;
import de.rub.nds.tlsattacker.core.workflow.chooser.Chooser;

public class ClientAuthzExtensionPreparator
        extends ExtensionPreparator<ClientAuthzExtensionMessage> {

    private final ClientAuthzExtensionMessage msg;

    public ClientAuthzExtensionPreparator(Chooser chooser, ClientAuthzExtensionMessage message) {
        super(chooser, message);
        msg = message;
    }

    @Override
    public void prepareExtensionContent() {
        msg.setAuthzFormatListLength(
                chooser.getConfig().getClientAuthzExtensionDataFormat().size());
        msg.setAuthzFormatList(
                AuthzDataFormat.listToByteArray(
                        chooser.getConfig().getClientAuthzExtensionDataFormat()));
    }
}
