/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.constants;

/** Symmetric cipher algorithm and its mapping to Java names */
public enum CipherAlgorithm {
    NULL(0, 0, 0, 0),
    RC2_128(16, 8, 0, 8, "RC2/CBC/NoPadding"),
    RC4_128(16, 0, 0, 0, "RC4"),
    DES_CBC(8, 8, 0, 8, "DES/CBC/NoPadding"),
    DES_EDE_CBC(24, 8, 0, 8, "DESede/CBC/NoPadding"),
    AES_128_CBC(16, 16, 0, 16, "AES/CBC/NoPadding"),
    AES_256_CBC(32, 16, 0, 16, "AES/CBC/NoPadding"),
    AES_128_GCM(16, 4, 8, 16, "AES/GCM/NoPadding"),
    AES_256_GCM(32, 4, 8, 16, "AES/GCM/NoPadding"),
    CAMELLIA_128_CBC(16, 16, 0, 16, "Camellia/CBC/NoPadding"),
    CAMELLIA_256_CBC(32, 16, 0, 16, "Camellia/CBC/NoPadding"),
    CAMELLIA_128_GCM(16, 16, 8, 16, "Camellia/GCM/NoPadding"),
    CAMELLIA_256_GCM(32, 16, 8, 16, "Camellia/GCM/NoPadding"),
    IDEA_128(16, 8, 0, 8, "IDEA/CBC/NoPadding"),
    SEED_CBC(16, 16, 0, 16, "SEED/CBC/NoPadding"),
    AES_128_CCM(16, 4, 8, 16, "AES/CCM/NoPadding"),
    AES_256_CCM(32, 4, 8, 16, "AES/CCM/NoPadding"),
    CHACHA20_POLY1305(32, 12, 0, 0, "ChaCha20-Poly1305"),
    UNOFFICIAL_CHACHA20_POLY1305(32, 12, 0, 0, "ChaCha20-Poly1305"),
    DES40_CBC(8, 8, 0, 8, "DES/CBC/NoPadding"), // currently uses des 56bit
    ARIA_128_CBC(16, 16, 0, 16, "ARIA/CBC/NoPadding"),
    ARIA_256_CBC(32, 16, 0, 16, "ARIA/CBC/NoPadding"),
    ARIA_128_GCM(16, 16, 8, 16, "ARIA/GCM/NoPadding"),
    ARIA_256_GCM(32, 16, 8, 16, "ARIA/GCM/NoPadding"),
    GOST_28147_CNT(32, 8, 0, 8, "GOST28147/ECB/NoPadding"),
    FORTEZZA_CBC(0, 0, 0, 0), // TODO
    AES_128_CTR(16, 16, 0, 0, "AES/CTR/NoPadding"),
    AES_256_CTR(32, 16, 0, 0, "AES/CTR/NoPadding"),
    SM4_GCM(16, 4, 8, 16, "SM4/GCM/NoPadding"),
    SM4_CCM(16, 4, 8, 16, "SM4/CCM/NoPadding");

    CipherAlgorithm(
            int keySize,
            int nonceBytesFromHandshake,
            int nonceBytesFromRecord,
            int blocksize,
            String javaName) {
        this.keySize = keySize;
        this.javaName = javaName;
        this.nonceBytesFromHandshake = nonceBytesFromHandshake;
        this.nonceBytesFromRecord = nonceBytesFromRecord;
        this.blocksize = blocksize;
    }

    CipherAlgorithm(
            int keySize, int nonceBytesFromHandshake, int nonceBytesFromRecord, int blocksize) {
        this.keySize = keySize;
        this.javaName = null;
        this.nonceBytesFromHandshake = nonceBytesFromHandshake;
        this.nonceBytesFromRecord = nonceBytesFromRecord;
        this.blocksize = blocksize;
    }

    /** Key size for the underlying cipher */
    private final int keySize;

    /**
     * Number of bytes taken from the handshake and used as an initialization vector / nonce input
     * into the cipher (i.e., number of bytes in server_write_IV / client_write_IV)
     */
    private final int nonceBytesFromHandshake;

    /** Number of bytes generated with each new record. */
    private final int nonceBytesFromRecord;

    private final int blocksize;

    /** java name mapping */
    private final String javaName;

    public int getKeySize() {
        return keySize;
    }

    public String getJavaName() {
        return javaName;
    }

    public int getNonceBytesFromHandshake() {
        return nonceBytesFromHandshake;
    }

    public int getNonceBytesFromRecord() {
        return nonceBytesFromRecord;
    }

    public int getBlocksize() {
        return blocksize;
    }
}
