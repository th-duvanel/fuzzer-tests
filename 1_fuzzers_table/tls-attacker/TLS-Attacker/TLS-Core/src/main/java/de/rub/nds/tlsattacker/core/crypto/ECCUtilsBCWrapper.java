/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.crypto;

import de.rub.nds.tlsattacker.core.constants.ECPointFormat;
import de.rub.nds.tlsattacker.core.constants.NamedGroup;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.tls.TlsECCUtils;

public class ECCUtilsBCWrapper {

    /**
     * Reads ECC domain parameters from an input stream, based on given named curves and point
     * formats. It uses the BC functionality.
     *
     * @param namedGroups The Array of namedGroups
     * @param pointFormats The Array of ECPointFormats
     * @param input The input stream to read from
     * @return ECDomainParameters
     * @throws IOException If something goes wrong while reading from the Stream
     */
    public static ECDomainParameters readECParameters(
            NamedGroup[] namedGroups, ECPointFormat[] pointFormats, InputStream input)
            throws IOException {
        int[] nc = convertNamedCurves(namedGroups);
        short[] pf = convertPointFormats(pointFormats);
        return TlsECCUtils.readECParameters(nc, pf, input);
    }

    public static ECDomainParameters readECParameters(
            NamedGroup namedGroup, ECPointFormat pointFormat, InputStream input)
            throws IOException {
        int[] nc = convertNamedCurves(new NamedGroup[] {namedGroup});
        short[] pf = convertPointFormats(new ECPointFormat[] {pointFormat});
        return TlsECCUtils.readECParameters(nc, pf, input);
    }

    /**
     * Reads ECC domain parameters from an InputStream, all named formats and point formats are
     * allowed
     *
     * @param input The input stream to read from
     * @return ECDomainParameters
     * @throws IOException If something goes wrong while reading from the Stream
     */
    public static ECDomainParameters readECParameters(InputStream input) throws IOException {
        NamedGroup[] namedCurves = NamedGroup.values();
        ECPointFormat[] pointFormats = ECPointFormat.values();
        return readECParameters(namedCurves, pointFormats, input);
    }

    /**
     * Converts named curves into BC style notation
     *
     * @param namedGroups The NamedCurves to convert
     * @return int[] of the NamedCurves in BC Style
     */
    private static int[] convertNamedCurves(NamedGroup[] namedGroups) {
        if (namedGroups == null || namedGroups.length == 0) {
            return new int[0];
        }
        int[] nc = new int[namedGroups.length];
        for (int i = 0; i < namedGroups.length; i++) {
            nc[i] = namedGroups[i].getIntValue();
        }
        return nc;
    }

    /**
     * Converts point formats into BC style notation
     *
     * @param pointFormats The pointFormats to convert
     * @return The converted PointFormats
     */
    private static short[] convertPointFormats(ECPointFormat[] pointFormats) {
        if (pointFormats == null || pointFormats.length == 0) {
            return new short[0];
        }
        short[] pf = new short[pointFormats.length];
        for (int i = 0; i < pointFormats.length; i++) {
            pf[i] = pointFormats[i].getShortValue();
        }
        return pf;
    }

    private ECCUtilsBCWrapper() {}
}
