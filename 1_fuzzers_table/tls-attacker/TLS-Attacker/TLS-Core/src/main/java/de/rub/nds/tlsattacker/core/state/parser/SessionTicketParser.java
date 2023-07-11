/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.state.parser;

import static de.rub.nds.modifiablevariable.util.ArrayConverter.bytesToHexString;

import de.rub.nds.tlsattacker.core.constants.CipherAlgorithm;
import de.rub.nds.tlsattacker.core.constants.ExtensionByteLength;
import de.rub.nds.tlsattacker.core.constants.MacAlgorithm;
import de.rub.nds.tlsattacker.core.layer.data.Parser;
import de.rub.nds.tlsattacker.core.state.SessionTicket;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SessionTicketParser extends Parser<SessionTicket> {

    private static final Logger LOGGER = LogManager.getLogger();
    private final byte[] configTicketKeyName;
    private final CipherAlgorithm configCipherAlgorithm;
    private final MacAlgorithm configMacAlgorithm;
    private final SessionTicket sessionTicket;

    public SessionTicketParser(
            int startposition,
            byte[] array,
            SessionTicket sessionTicket,
            byte[] configTicketKeyName,
            CipherAlgorithm configCipherAlgorithm,
            MacAlgorithm configMacAlgorithm) {
        super(new ByteArrayInputStream(array, startposition, array.length - startposition));
        this.configTicketKeyName = configTicketKeyName;
        this.configCipherAlgorithm = configCipherAlgorithm;
        this.configMacAlgorithm = configMacAlgorithm;
        this.sessionTicket = sessionTicket;
    }

    private void parseKeyName(SessionTicket sessionTicket) {
        sessionTicket.setKeyName(parseByteArrayField(configTicketKeyName.length));
        LOGGER.debug(
                "Parsed session ticket key name {} ",
                () -> bytesToHexString(sessionTicket.getKeyName().getValue()));
        if (!Arrays.equals(sessionTicket.getKeyName().getValue(), configTicketKeyName)) {
            LOGGER.warn(
                    "Parsed session ticket key name does not match expected key name - subsequent parsing will probably fail");
        }
    }

    private void parseIV(SessionTicket sessionTicket) {
        sessionTicket.setIV(parseByteArrayField(configCipherAlgorithm.getBlocksize()));
        LOGGER.debug("Parsed session ticket IV {}", () -> sessionTicket.getIV().getValue());
    }

    private void parseEncryptedStateLength(SessionTicket sessionTicket) {
        sessionTicket.setEncryptedStateLength(
                parseIntField(ExtensionByteLength.ENCRYPTED_SESSION_TICKET_STATE_LENGTH));
        LOGGER.debug(
                "Parsed encrypted state length {}",
                () -> sessionTicket.getEncryptedStateLength().getValue());
    }

    private void parseEncryptedState(SessionTicket sessionTicket) {
        sessionTicket.setEncryptedState(
                parseByteArrayField(sessionTicket.getEncryptedStateLength().getValue()));
        LOGGER.debug(
                "Parsed session ticket encrypted state {}",
                () -> sessionTicket.getEncryptedState().getValue());
    }

    private void parseMAC(SessionTicket sessionTicket) {
        sessionTicket.setMAC(parseByteArrayField(configMacAlgorithm.getSize()));
        LOGGER.debug("Parsed session ticket MAC {}", () -> sessionTicket.getMAC().getValue());
    }

    @Override
    public void parse(SessionTicket sessionTicket) {
        parseKeyName(sessionTicket);
        parseIV(sessionTicket);
        parseEncryptedStateLength(sessionTicket);
        parseEncryptedState(sessionTicket);
        parseMAC(sessionTicket);
    }
}
