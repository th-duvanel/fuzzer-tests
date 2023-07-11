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
import java.util.Objects;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.spec.DHParameterSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@XmlAccessorType(XmlAccessType.FIELD)
public class CustomDHPrivateKey extends CustomPrivateKey implements DHPrivateKey {

    private static final Logger LOGGER = LogManager.getLogger();

    private final BigInteger privateKey;
    private final BigInteger modulus;
    private final BigInteger generator;

    private CustomDHPrivateKey() {
        privateKey = null;
        modulus = null;
        generator = null;
    }

    public CustomDHPrivateKey(BigInteger privateKey, BigInteger modulus, BigInteger generator) {
        this.privateKey = privateKey;
        this.modulus = modulus;
        this.generator = generator;
    }

    @Override
    public BigInteger getX() {
        return privateKey;
    }

    @Override
    public DHParameterSpec getParams() {
        return new DHParameterSpec(modulus, generator);
    }

    @Override
    public String getAlgorithm() {
        return "DH";
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
    public void adjustInContext(TlsContext tlsContext, ConnectionEndType ownerOfKey) {
        LOGGER.debug("Adjusting DH private key in tlsContext");
        if (null == ownerOfKey) {
            throw new IllegalArgumentException("Owner of Key " + ownerOfKey + " is not supported");
        } else {
            switch (ownerOfKey) {
                case CLIENT:
                    tlsContext.setClientDhPrivateKey(privateKey);
                    break;
                case SERVER:
                    tlsContext.setServerDhPrivateKey(privateKey);
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
                    config.setDefaultClientDhPrivateKey(privateKey);
                    break;
                case SERVER:
                    config.setDefaultServerDhPrivateKey(privateKey);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Owner of Key " + ownerOfKey + " is not supported");
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.privateKey);
        hash = 23 * hash + Objects.hashCode(this.modulus);
        hash = 23 * hash + Objects.hashCode(this.generator);
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
        final CustomDHPrivateKey other = (CustomDHPrivateKey) obj;
        if (!Objects.equals(this.privateKey, other.privateKey)) {
            return false;
        }
        if (!Objects.equals(this.modulus, other.modulus)) {
            return false;
        }
        return Objects.equals(this.generator, other.generator);
    }
}
