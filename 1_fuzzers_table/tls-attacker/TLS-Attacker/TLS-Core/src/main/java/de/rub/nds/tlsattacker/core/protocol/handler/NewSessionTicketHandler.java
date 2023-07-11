/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.protocol.handler;

import de.rub.nds.tlsattacker.core.constants.AlgorithmResolver;
import de.rub.nds.tlsattacker.core.constants.DigestAlgorithm;
import de.rub.nds.tlsattacker.core.constants.HKDFAlgorithm;
import de.rub.nds.tlsattacker.core.constants.Tls13KeySetType;
import de.rub.nds.tlsattacker.core.crypto.HKDFunction;
import de.rub.nds.tlsattacker.core.exceptions.CryptoException;
import de.rub.nds.tlsattacker.core.layer.context.TlsContext;
import de.rub.nds.tlsattacker.core.protocol.message.NewSessionTicketMessage;
import de.rub.nds.tlsattacker.core.protocol.message.extension.psk.PskSet;
import de.rub.nds.tlsattacker.core.state.session.TicketSession;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import javax.crypto.Mac;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NewSessionTicketHandler extends HandshakeMessageHandler<NewSessionTicketMessage> {

    private static final Logger LOGGER = LogManager.getLogger();

    public NewSessionTicketHandler(TlsContext tlsContext) {
        super(tlsContext);
    }

    @Override
    public void adjustContext(NewSessionTicketMessage message) {
        if (tlsContext.getChooser().getSelectedProtocolVersion().isTLS13()) {
            adjustPskSets(message);
        } else {
            byte[] ticket = message.getTicket().getIdentity().getValue();
            LOGGER.debug("Adding Session for Ticket resumption using dummy SessionID");
            TicketSession session =
                    new TicketSession(tlsContext.getChooser().getMasterSecret(), ticket);
            tlsContext.addNewSession(session);
        }
    }

    private void adjustPskSets(NewSessionTicketMessage message) {
        LOGGER.debug("Adjusting PSK-Sets");
        List<PskSet> pskSets = tlsContext.getPskSets();
        if (pskSets == null) {
            pskSets = new LinkedList<>();
        }
        PskSet pskSet = new PskSet();
        pskSet.setCipherSuite(tlsContext.getChooser().getSelectedCipherSuite());
        if (message.getTicket().getTicketAgeAdd() != null) {
            pskSet.setTicketAgeAdd(message.getTicket().getTicketAgeAdd().getValue());
        } else {
            LOGGER.warn("No TicketAge specified in SessionTicket");
        }
        if (message.getTicket().getIdentity() != null) {
            pskSet.setPreSharedKeyIdentity(message.getTicket().getIdentity().getValue());
        } else {
            LOGGER.warn("No Identity in ticket. Using new byte[0] instead");
            pskSet.setPreSharedKeyIdentity(new byte[0]);
        }
        pskSet.setTicketAge(getTicketAge());
        if (message.getTicket().getTicketNonce() != null) {
            pskSet.setTicketNonce(message.getTicket().getTicketNonce().getValue());
        } else {
            LOGGER.warn("No nonce in ticket. Using new byte[0] instead");
            pskSet.setTicketNonce(new byte[0]);
        }
        // only derive PSK if client finished was already sent, because full handshake transcript is
        // required
        if (tlsContext.getActiveClientKeySetType() == Tls13KeySetType.APPLICATION_TRAFFIC_SECRETS) {
            pskSet.setPreSharedKey(derivePsk(pskSet));
        }

        LOGGER.debug("Adding PSK Set");
        pskSets.add(pskSet);
        tlsContext.setPskSets(pskSets);
    }

    private String getTicketAge() {
        DateTimeFormatter dateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime ticketDate = LocalDateTime.now();

        return ticketDate.format(dateTimeFormatter);
    }

    // TODO: this should be outsourced into a separate class
    protected byte[] derivePsk(PskSet pskSet) {
        try {
            LOGGER.debug("Deriving PSK from current session");
            HKDFAlgorithm hkdfAlgorithm =
                    AlgorithmResolver.getHKDFAlgorithm(
                            tlsContext.getChooser().getSelectedCipherSuite());
            DigestAlgorithm digestAlgo =
                    AlgorithmResolver.getDigestAlgorithm(
                            tlsContext.getChooser().getSelectedProtocolVersion(),
                            tlsContext.getChooser().getSelectedCipherSuite());
            int macLength =
                    Mac.getInstance(hkdfAlgorithm.getMacAlgorithm().getJavaName()).getMacLength();
            byte[] resumptionMasterSecret =
                    HKDFunction.deriveSecret(
                            hkdfAlgorithm,
                            digestAlgo.getJavaName(),
                            tlsContext.getChooser().getMasterSecret(),
                            HKDFunction.RESUMPTION_MASTER_SECRET,
                            tlsContext.getDigest().getRawBytes());
            tlsContext.setResumptionMasterSecret(resumptionMasterSecret);
            LOGGER.debug("Derived ResumptionMasterSecret: {}", resumptionMasterSecret);
            LOGGER.debug(
                    "Handshake Transcript Raw Bytes: {}", tlsContext.getDigest().getRawBytes());
            byte[] psk =
                    HKDFunction.expandLabel(
                            hkdfAlgorithm,
                            resumptionMasterSecret,
                            HKDFunction.RESUMPTION,
                            pskSet.getTicketNonce(),
                            macLength);
            LOGGER.debug("New derived pre-shared-key: {}", psk);
            return psk;

        } catch (NoSuchAlgorithmException | CryptoException ex) {
            LOGGER.error("DigestAlgorithm for psk derivation unknown");
            return new byte[0];
        }
    }
}
