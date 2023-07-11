/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.protocol.parser.extension;

import de.rub.nds.modifiablevariable.util.ArrayConverter;
import de.rub.nds.tlsattacker.core.constants.ExtensionByteLength;
import de.rub.nds.tlsattacker.core.constants.HandshakeByteLength;
import de.rub.nds.tlsattacker.core.layer.context.TlsContext;
import de.rub.nds.tlsattacker.core.protocol.message.extension.EncryptedServerNameIndicationExtensionMessage;
import de.rub.nds.tlsattacker.core.protocol.message.extension.keyshare.KeyShareEntry;
import de.rub.nds.tlsattacker.transport.ConnectionEndType;
import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EncryptedServerNameIndicationExtensionParser
        extends ExtensionParser<EncryptedServerNameIndicationExtensionMessage> {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ConnectionEndType talkingConnectionEnd;

    public EncryptedServerNameIndicationExtensionParser(InputStream stream, TlsContext tlsContext) {
        super(stream, tlsContext);
        this.talkingConnectionEnd = tlsContext.getTalkingConnectionEndType();
    }

    @Override
    public void parse(EncryptedServerNameIndicationExtensionMessage msg) {
        if (getBytesLeft() == 0) {
            LOGGER.debug("Received empty ESNI Extension");
            return;
        }
        if (talkingConnectionEnd == ConnectionEndType.SERVER) {
            parseNonce(msg);
        } else {
            parseCipherSuite(msg);
            parseKeyShareEntry(msg);
            parseRecordDigestLength(msg);
            parseRecordDigest(msg);
            parseEncryptedSniLength(msg);
            parseEncryptedSni(msg);
        }
    }

    private void parseNonce(EncryptedServerNameIndicationExtensionMessage msg) {
        byte[] nonce = parseByteArrayField(ExtensionByteLength.NONCE);
        msg.setServerNonce(nonce);
        LOGGER.info("Received Nonce: " + ArrayConverter.bytesToHexString(nonce));
    }

    private void parseCipherSuite(EncryptedServerNameIndicationExtensionMessage msg) {
        byte[] cipherSuite = parseByteArrayField(HandshakeByteLength.CIPHER_SUITE);
        msg.setCipherSuite(cipherSuite);
        LOGGER.debug(
                "cipherSuite: " + ArrayConverter.bytesToHexString(msg.getCipherSuite().getValue()));
    }

    private void parseKeyShareEntry(EncryptedServerNameIndicationExtensionMessage msg) {
        KeyShareEntryParser parser = new KeyShareEntryParser(getStream(), false);
        KeyShareEntry keyShareEntry = new KeyShareEntry();
        parser.parse(keyShareEntry);
        msg.setKeyShareEntry(keyShareEntry);
    }

    private void parseRecordDigestLength(EncryptedServerNameIndicationExtensionMessage msg) {
        int digestLen = parseIntField(ExtensionByteLength.RECORD_DIGEST_LENGTH);
        msg.setRecordDigestLength(digestLen);
        LOGGER.debug("recordDigestLength: " + msg.getRecordDigestLength().getValue());
    }

    private void parseRecordDigest(EncryptedServerNameIndicationExtensionMessage msg) {
        byte[] recordDigest = parseByteArrayField(msg.getRecordDigestLength().getValue());
        msg.setRecordDigest(recordDigest);
        LOGGER.debug(
                "recordDigest: "
                        + ArrayConverter.bytesToHexString(msg.getRecordDigest().getValue()));
    }

    private void parseEncryptedSniLength(EncryptedServerNameIndicationExtensionMessage msg) {
        int encryptedSniLength = this.parseIntField(ExtensionByteLength.ENCRYPTED_SNI_LENGTH);
        msg.setEncryptedSniLength(encryptedSniLength);
        LOGGER.debug("encryptedSniLength: " + msg.getEncryptedSniLength());
    }

    private void parseEncryptedSni(EncryptedServerNameIndicationExtensionMessage msg) {
        byte[] encryptedSni = parseByteArrayField(msg.getEncryptedSniLength().getOriginalValue());
        msg.setEncryptedSni(encryptedSni);
        LOGGER.debug(
                "encryptedSni: "
                        + ArrayConverter.bytesToHexString(msg.getEncryptedSni().getValue()));
    }
}
