/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.protocol.message.extension.trustedauthority;

import de.rub.nds.modifiablevariable.ModifiableVariableFactory;
import de.rub.nds.modifiablevariable.ModifiableVariableProperty;
import de.rub.nds.modifiablevariable.bytearray.ModifiableByteArray;
import de.rub.nds.modifiablevariable.integer.ModifiableInteger;
import de.rub.nds.modifiablevariable.singlebyte.ModifiableByte;
import de.rub.nds.modifiablevariable.util.UnformattedByteArrayAdapter;
import de.rub.nds.tlsattacker.core.protocol.ModifiableVariableHolder;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class TrustedAuthority extends ModifiableVariableHolder implements Serializable {

    @ModifiableVariableProperty private ModifiableByte identifierType;
    @ModifiableVariableProperty private ModifiableByteArray sha1Hash;
    @ModifiableVariableProperty private ModifiableInteger distinguishedNameLength;
    @ModifiableVariableProperty private ModifiableByteArray distinguishedName;

    private byte identifierTypeConfig;

    @XmlJavaTypeAdapter(UnformattedByteArrayAdapter.class)
    private byte[] sha1HashConfig;

    private Integer distinguishedNameLengthConfig;

    @XmlJavaTypeAdapter(UnformattedByteArrayAdapter.class)
    private byte[] distinguishedNameConfig;

    public TrustedAuthority() {}

    public TrustedAuthority(
            byte preparatorIdentifierType,
            byte[] preparatorSha1Hash,
            Integer preparatorDistinguishedNameLength,
            byte[] preparatorDistinguishedName) {
        this.identifierTypeConfig = preparatorIdentifierType;
        this.sha1HashConfig = preparatorSha1Hash;
        this.distinguishedNameLengthConfig = preparatorDistinguishedNameLength;
        this.distinguishedNameConfig = preparatorDistinguishedName;
    }

    public ModifiableByte getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(ModifiableByte identifierType) {
        this.identifierType = identifierType;
    }

    public void setIdentifierType(byte identifierType) {
        this.identifierType =
                ModifiableVariableFactory.safelySetValue(this.identifierType, identifierType);
    }

    public ModifiableByteArray getSha1Hash() {
        return sha1Hash;
    }

    public void setSha1Hash(ModifiableByteArray sha1Hash) {
        this.sha1Hash = sha1Hash;
    }

    public void setSha1Hash(byte[] sha1Hash) {
        this.sha1Hash = ModifiableVariableFactory.safelySetValue(this.sha1Hash, sha1Hash);
    }

    public ModifiableInteger getDistinguishedNameLength() {
        return distinguishedNameLength;
    }

    public void setDistinguishedNameLength(ModifiableInteger distinguishedNameLength) {
        this.distinguishedNameLength = distinguishedNameLength;
    }

    public void setDistinguishedNameLength(int distinguishedNameLength) {
        this.distinguishedNameLength =
                ModifiableVariableFactory.safelySetValue(
                        this.distinguishedNameLength, distinguishedNameLength);
    }

    public ModifiableByteArray getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(ModifiableByteArray distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public void setDistinguishedName(byte[] distinguishedName) {
        this.distinguishedName =
                ModifiableVariableFactory.safelySetValue(this.distinguishedName, distinguishedName);
    }

    public byte getIdentifierTypeConfig() {
        return identifierTypeConfig;
    }

    public void setIdentifierTypeConfig(byte identifierTypeConfig) {
        this.identifierTypeConfig = identifierTypeConfig;
    }

    public byte[] getSha1HashConfig() {
        return sha1HashConfig;
    }

    public void setSha1HashConfig(byte[] sha1HashConfig) {
        this.sha1HashConfig = sha1HashConfig;
    }

    public Integer getDistinguishedNameLengthConfig() {
        return distinguishedNameLengthConfig;
    }

    public void setPreparatorDistinguishedNameLength(int preparatorDistinguishedNameLength) {
        this.distinguishedNameLengthConfig = preparatorDistinguishedNameLength;
    }

    public byte[] getDistinguishedNameConfig() {
        return distinguishedNameConfig;
    }

    public void setDistinguishedNameConfig(byte[] distinguishedNameConfig) {
        this.distinguishedNameConfig = distinguishedNameConfig;
    }
}
