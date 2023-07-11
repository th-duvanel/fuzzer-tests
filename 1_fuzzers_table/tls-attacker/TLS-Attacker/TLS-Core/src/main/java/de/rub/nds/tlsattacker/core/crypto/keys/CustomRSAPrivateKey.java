/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.crypto.keys;

import de.rub.nds.tlsattacker.core.config.Config;
import de.rub.nds.tlsattacker.core.layer.context.TlsContext;
import de.rub.nds.tlsattacker.transport.ConnectionEndType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@XmlAccessorType(XmlAccessType.FIELD)
public class CustomRSAPrivateKey extends CustomPrivateKey implements RSAPrivateKey {

    private static final Logger LOGGER = LogManager.getLogger();

    private final BigInteger modulus;
    private final BigInteger privateExponent;

    private CustomRSAPrivateKey() {
        modulus = null;
        privateExponent = null;
    }

    public CustomRSAPrivateKey(BigInteger modulus, BigInteger privateExponent) {
        this.modulus = modulus;
        this.privateExponent = privateExponent;
    }

    @Override
    public BigInteger getPrivateExponent() {
        return privateExponent;
    }

    @Override
    public String getAlgorithm() {
        return "RSA";
    }

    @Override
    public String getFormat() {
        return "None";
    }

    @Override
    public byte[] getEncoded() {
        throw new UnsupportedOperationException("CustomKey cannot be encoded");
    }

    @Override
    public BigInteger getModulus() {
        return modulus;
    }

    @Override
    public void adjustInContext(TlsContext tlsContext, ConnectionEndType ownerOfKey) {
        LOGGER.debug("Adjusting RSA private key in context");
        if (null == ownerOfKey) {
            throw new IllegalArgumentException("Owner of Key " + ownerOfKey + " is not supported");
        } else {
            switch (ownerOfKey) {
                case CLIENT:
                    tlsContext.setClientRSAPrivateKey(privateExponent);
                    tlsContext.setClientRsaModulus(modulus);
                    break;
                case SERVER:
                    tlsContext.setServerRSAPrivateKey(privateExponent);
                    tlsContext.setServerRSAModulus(modulus);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Owner of Key " + ownerOfKey + " is not supported");
            }
        }
    }

    @Override
    public void adjustInConfig(Config config, ConnectionEndType ownerOfKey) {
        if (null == ownerOfKey) {
            throw new IllegalArgumentException("Owner of Key " + ownerOfKey + " is not supported");
        } else {
            switch (ownerOfKey) {
                case CLIENT:
                    config.setDefaultClientRSAPrivateKey(privateExponent);
                    config.setDefaultClientRSAModulus(modulus);
                    break;
                case SERVER:
                    config.setDefaultServerRSAPrivateKey(privateExponent);
                    config.setDefaultServerRSAModulus(modulus);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Owner of Key " + ownerOfKey + " is not supported");
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.modulus);
        hash = 73 * hash + Objects.hashCode(this.privateExponent);
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
        final CustomRSAPrivateKey other = (CustomRSAPrivateKey) obj;
        if (!Objects.equals(this.modulus, other.modulus)) {
            return false;
        }
        return Objects.equals(this.privateExponent, other.privateExponent);
    }

    @Override
    public String toString() {
        return "CustomRSAPrivateKey{"
                + "modulus="
                + modulus
                + ", privateExponent="
                + privateExponent
                + '}';
    }
}
