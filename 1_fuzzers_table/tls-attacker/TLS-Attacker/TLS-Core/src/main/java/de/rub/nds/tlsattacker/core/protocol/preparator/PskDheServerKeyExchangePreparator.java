/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.protocol.preparator;

import de.rub.nds.tlsattacker.core.protocol.message.PskDheServerKeyExchangeMessage;
import de.rub.nds.tlsattacker.core.workflow.chooser.Chooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PskDheServerKeyExchangePreparator
        extends DHEServerKeyExchangePreparator<PskDheServerKeyExchangeMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final PskDheServerKeyExchangeMessage msg;

    public PskDheServerKeyExchangePreparator(
            Chooser chooser, PskDheServerKeyExchangeMessage message) {
        super(chooser, message);
        this.msg = message;
    }

    @Override
    public void prepareHandshakeMessageContents() {
        msg.setIdentityHint(chooser.getPSKIdentityHint());
        msg.setIdentityHintLength(msg.getIdentityHint().getValue().length);
        setPskDheParams();
        preparePskPublicKey(msg);
        super.prepareDheParams();
    }

    private void setPskDheParams() {
        msg.prepareComputations();
        setComputedPskDhGenerator(msg);
        setComputedPskDhModulus(msg);
        setComputedPskDhPrivateKey(msg);
    }

    protected void setComputedPskDhPrivateKey(PskDheServerKeyExchangeMessage msg) {
        msg.getComputations().setPrivateKey(chooser.getPSKServerPrivateKey());
        LOGGER.debug("PrivateKey: " + msg.getComputations().getPrivateKey().getValue());
    }

    protected void setComputedPskDhModulus(PskDheServerKeyExchangeMessage msg) {
        msg.getComputations().setModulus(chooser.getPSKModulus());
        LOGGER.debug(
                "Modulus used for Computations: 0x"
                        + msg.getComputations().getModulus().getValue().toString(16));
    }

    protected void setComputedPskDhGenerator(PskDheServerKeyExchangeMessage msg) {
        msg.getComputations().setGenerator(chooser.getPSKGenerator());
        LOGGER.debug(
                "Generator used for Computations: 0x"
                        + msg.getComputations().getGenerator().getValue().toString(16));
    }

    private void preparePskPublicKey(PskDheServerKeyExchangeMessage msg) {
        msg.setPublicKey(chooser.getPSKServerPublicKey().toByteArray());
        LOGGER.debug("PublicKey: {}", msg.getPublicKey().getValue());
    }
}
