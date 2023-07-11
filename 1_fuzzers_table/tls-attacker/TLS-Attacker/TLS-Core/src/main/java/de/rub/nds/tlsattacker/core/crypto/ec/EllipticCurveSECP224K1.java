/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.crypto.ec;

import java.math.BigInteger;

@SuppressWarnings("SpellCheckingInspection")
public class EllipticCurveSECP224K1 extends EllipticCurveOverFp {
    public EllipticCurveSECP224K1() {
        super(
                BigInteger.ZERO,
                new BigInteger("5"),
                new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFE56D", 16),
                new BigInteger("A1455B334DF099DF30FC28A169A467E9E47075A90F7E650EB6B7A45C", 16),
                new BigInteger("7E089FED7FBA344282CAFBD6F7E319F7C0B0BD59E2CA4BDB556D61A5", 16),
                new BigInteger("010000000000000000000000000001DCE8D2EC6184CAF0A971769FB1F7", 16));
    }
}
