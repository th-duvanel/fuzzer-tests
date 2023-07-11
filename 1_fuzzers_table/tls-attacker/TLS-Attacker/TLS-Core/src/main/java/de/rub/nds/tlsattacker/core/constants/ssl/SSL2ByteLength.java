/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.constants.ssl;

/** Length of fields in SSL2 Messages */
public class SSL2ByteLength {
    public static final int LENGTH = 2;

    public static final int LONG_LENGTH = 3;

    // Strictly speaking, this depends on the cipher, but all ciphers use an MD5
    // digest with a length of 16 bytes
    public static final int MAC_DATA = 16;

    public static final int MESSAGE_TYPE = 1;

    public static final int VERSION = 2;

    public static final int CIPHERSUITE_LENGTH = 2;

    public static final int SESSIONID_LENGTH = 2;

    public static final int CHALLENGE_LENGTH = 2;

    public static final int SESSION_ID_HIT = 1;

    public static final int CERTIFICATE_TYPE = 1;

    public static final int CERTIFICATE_LENGTH = 2;

    public static final int CIPHERKIND_LENGTH = 3;

    public static final int CLEAR_KEY_LENGTH = 2;

    public static final int ENCRYPTED_KEY_LENGTH = 2;

    public static final int KEY_ARG_LENGTH = 2;

    private SSL2ByteLength() {}
}
