/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.constants;

import de.rub.nds.modifiablevariable.util.ArrayConverter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bouncycastle.util.Arrays;

public enum SSL2CipherSuite implements Serializable {
    SSL_CK_RC4_128_WITH_MD5(0x010080, 16, 0, 0),
    SSL_CK_RC4_128_EXPORT40_WITH_MD5(0x020080, 5, 11, 0),
    SSL_CK_RC2_128_CBC_WITH_MD5(0x030080, 16, 0, 8),
    SSL_CK_RC2_128_CBC_EXPORT40_WITH_MD5(0x040080, 5, 11, 8),
    SSL_CK_IDEA_128_CBC_WITH_MD5(0x050080, 16, 0, 8),
    SSL_CK_DES_64_CBC_WITH_MD5(0x060040, 8, 0, 8),
    SSL_CK_DES_192_EDE3_CBC_WITH_MD5(0x0700C0, 24, 0, 8),
    SSL_UNKNOWN_CIPHER(0x999999, 0, 0, 0);

    // TODO: I'd rather not inherit from CipherSuite, as this is too different.
    // Robert, what do you think?

    private static final int SSL2CipherSuiteLength = 3;
    private int value;
    private int secretKeyByteNumber;
    private int clearKeyByteNumber;
    private int blockSize;

    private static final Map<Integer, SSL2CipherSuite> MAP;

    private SSL2CipherSuite(
            int value, int secretKeyByteNumber, int clearKeyByteNumber, int blockSize) {
        this.value = value;
        this.secretKeyByteNumber = secretKeyByteNumber;
        this.clearKeyByteNumber = clearKeyByteNumber;
        this.blockSize = blockSize;
    }

    static {
        MAP = new HashMap<>();
        for (SSL2CipherSuite c : SSL2CipherSuite.values()) {
            MAP.put(c.value, c);
        }
    }

    public static List<SSL2CipherSuite> getCipherSuites(byte[] values) {
        List<SSL2CipherSuite> cipherSuites = new LinkedList<>();
        int pointer = 0;
        while (pointer < values.length) {
            byte[] suiteBytes =
                    Arrays.copyOfRange(values, pointer, pointer + SSL2CipherSuiteLength);
            int suiteValue = ArrayConverter.bytesToInt(suiteBytes);
            cipherSuites.add(getCipherSuite(suiteValue));
            pointer += SSL2CipherSuiteLength;
        }
        return cipherSuites;
    }

    public static SSL2CipherSuite getCipherSuite(int value) {
        SSL2CipherSuite cs = MAP.get(value);
        if (cs == null) {
            return SSL_UNKNOWN_CIPHER;
        }
        return cs;
    }

    public int getValue() {
        return value;
    }

    public byte[] getByteValue() {
        return ArrayConverter.intToBytes(value, SSL2CipherSuiteLength);
    }

    public int getClearKeyByteNumber() {
        return clearKeyByteNumber;
    }

    public int getSecretKeyByteNumber() {
        return secretKeyByteNumber;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public boolean isWeak() {
        return this == SSL_CK_DES_64_CBC_WITH_MD5
                || this == SSL_CK_RC2_128_CBC_EXPORT40_WITH_MD5
                || this == SSL_CK_RC4_128_EXPORT40_WITH_MD5;
    }

    public boolean isExport() {
        return this == SSL_CK_RC2_128_CBC_EXPORT40_WITH_MD5
                || this == SSL_CK_RC4_128_EXPORT40_WITH_MD5;
    }
}
