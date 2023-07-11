/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.protocol.message.computations;

import de.rub.nds.modifiablevariable.ModifiableVariableFactory;
import de.rub.nds.modifiablevariable.ModifiableVariableProperty;
import de.rub.nds.modifiablevariable.biginteger.ModifiableBigInteger;
import de.rub.nds.modifiablevariable.bytearray.ModifiableByteArray;
import java.math.BigInteger;

public class SRPServerComputations extends KeyExchangeComputations {

    /** dh modulus used for computations */
    @ModifiableVariableProperty(type = ModifiableVariableProperty.Type.PUBLIC_KEY)
    private ModifiableBigInteger modulus;

    /** dh generator used for computations */
    @ModifiableVariableProperty(type = ModifiableVariableProperty.Type.PUBLIC_KEY)
    private ModifiableBigInteger generator;

    /** SRP salt */
    private ModifiableByteArray salt;

    private ModifiableByteArray srpIdentity;
    private ModifiableByteArray srpPassword;

    public SRPServerComputations() {}

    public ModifiableByteArray getSRPIdentity() {
        return srpIdentity;
    }

    public void setSRPIdentity(ModifiableByteArray srpIdentity) {
        this.srpIdentity = srpIdentity;
    }

    public void setSRPIdentity(byte[] srpIdentity) {
        this.srpIdentity = ModifiableVariableFactory.safelySetValue(this.srpIdentity, srpIdentity);
    }

    public ModifiableByteArray getSRPPassword() {
        return srpPassword;
    }

    public void setSRPPassword(ModifiableByteArray srpPassword) {
        this.srpPassword = srpPassword;
    }

    public void setSRPPassword(byte[] srpPassword) {
        this.srpPassword = ModifiableVariableFactory.safelySetValue(this.srpPassword, srpPassword);
    }

    public ModifiableByteArray getSalt() {
        return salt;
    }

    public void setSalt(ModifiableByteArray salt) {
        this.salt = salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = ModifiableVariableFactory.safelySetValue(this.salt, salt);
    }

    public ModifiableBigInteger getModulus() {
        return modulus;
    }

    public void setModulus(ModifiableBigInteger modulus) {
        this.modulus = modulus;
    }

    public void setModulus(BigInteger modulus) {
        this.modulus = ModifiableVariableFactory.safelySetValue(this.modulus, modulus);
    }

    public ModifiableBigInteger getGenerator() {
        return generator;
    }

    public void setGenerator(ModifiableBigInteger generator) {
        this.generator = generator;
    }

    public void setGenerator(BigInteger generator) {
        this.generator = ModifiableVariableFactory.safelySetValue(this.generator, generator);
    }
}
