/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.protocol.message.extension.psk;

import de.rub.nds.modifiablevariable.util.IllegalStringAdapter;
import de.rub.nds.modifiablevariable.util.UnformattedByteArrayAdapter;
import de.rub.nds.tlsattacker.core.constants.CipherSuite;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/** Contains (TLS 1.3) PSK-related values */
@XmlAccessorType(XmlAccessType.FIELD)
public class PskSet implements Serializable {

    /** PreSharedKeyIdentity to be used as PSK Identifier */
    @XmlJavaTypeAdapter(UnformattedByteArrayAdapter.class)
    private byte[] preSharedKeyIdentity;

    /** PreSharedKeys for PSK-Extension */
    @XmlJavaTypeAdapter(UnformattedByteArrayAdapter.class)
    private byte[] preSharedKey;

    /** TicketAge value to be used to generate the obfuscated ticket age for the given PSKs */
    @XmlJavaTypeAdapter(IllegalStringAdapter.class)
    private String ticketAge;

    /** TicketAgeAdd value to be used to obfuscate the ticket age for the given PSKs */
    @XmlJavaTypeAdapter(UnformattedByteArrayAdapter.class)
    private byte[] ticketAgeAdd;

    /** ticket nonce used to derive PSK */
    @XmlJavaTypeAdapter(UnformattedByteArrayAdapter.class)
    private byte[] ticketNonce;

    private CipherSuite cipherSuite;

    public PskSet() {}

    public PskSet(
            byte[] preSharedKeyIdentity,
            byte[] preSharedKey,
            String ticketAge,
            byte[] ticketAgeAdd,
            byte[] ticketNonce,
            CipherSuite cipherSuite) {
        this.preSharedKeyIdentity = preSharedKeyIdentity;
        this.preSharedKey = preSharedKey;
        this.ticketAge = ticketAge;
        this.ticketAgeAdd = ticketAgeAdd;
        this.ticketNonce = ticketNonce;
        this.cipherSuite = cipherSuite;
    }

    /**
     * @return the preSharedKeyIdentity
     */
    public byte[] getPreSharedKeyIdentity() {
        return preSharedKeyIdentity;
    }

    /**
     * @param preSharedKeyIdentity the preSharedKeyIdentity to set
     */
    public void setPreSharedKeyIdentity(byte[] preSharedKeyIdentity) {
        this.preSharedKeyIdentity = preSharedKeyIdentity;
    }

    /**
     * @return the preSharedKey
     */
    public byte[] getPreSharedKey() {
        return preSharedKey;
    }

    /**
     * @param preSharedKey the preSharedKey to set
     */
    public void setPreSharedKey(byte[] preSharedKey) {
        this.preSharedKey = preSharedKey;
    }

    /**
     * @return the ticketAge
     */
    public String getTicketAge() {
        return ticketAge;
    }

    /**
     * @param ticketAge the ticketAge to set
     */
    public void setTicketAge(String ticketAge) {
        this.ticketAge = ticketAge;
    }

    /**
     * @return the ticketAgeAdd
     */
    public byte[] getTicketAgeAdd() {
        return ticketAgeAdd;
    }

    /**
     * @param ticketAgeAdd the ticketAgeAdd to set
     */
    public void setTicketAgeAdd(byte[] ticketAgeAdd) {
        this.ticketAgeAdd = ticketAgeAdd;
    }

    public byte[] getTicketNonce() {
        return ticketNonce;
    }

    public void setTicketNonce(byte[] ticketNonce) {
        this.ticketNonce = ticketNonce;
    }

    /**
     * @return the cipherSuite
     */
    public CipherSuite getCipherSuite() {
        return cipherSuite;
    }

    /**
     * @param cipherSuite the cipherSuite to set
     */
    public void setCipherSuite(CipherSuite cipherSuite) {
        this.cipherSuite = cipherSuite;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Arrays.hashCode(this.preSharedKeyIdentity);
        hash = 43 * hash + Arrays.hashCode(this.preSharedKey);
        hash = 43 * hash + Objects.hashCode(this.ticketAge);
        hash = 43 * hash + Arrays.hashCode(this.ticketAgeAdd);
        hash = 43 * hash + Objects.hashCode(this.ticketNonce);
        hash = 43 * hash + Objects.hashCode(this.cipherSuite);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PskSet other = (PskSet) obj;
        if (!Objects.equals(this.ticketAge, other.ticketAge)) {
            return false;
        }
        if (!Arrays.equals(this.preSharedKeyIdentity, other.preSharedKeyIdentity)) {
            return false;
        }
        if (!Arrays.equals(this.preSharedKey, other.preSharedKey)) {
            return false;
        }
        if (!Arrays.equals(this.ticketAgeAdd, other.ticketAgeAdd)) {
            return false;
        }
        if (!Arrays.equals(this.ticketNonce, other.ticketNonce)) {
            return false;
        }
        return this.cipherSuite == other.cipherSuite;
    }
}
