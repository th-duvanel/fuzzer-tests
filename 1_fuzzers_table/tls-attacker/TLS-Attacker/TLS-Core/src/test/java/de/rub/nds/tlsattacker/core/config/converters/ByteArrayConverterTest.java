/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.config.converters;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.beust.jcommander.ParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ByteArrayConverterTest {
    private ByteArrayConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new ByteArrayConverter();
    }

    @Test
    public void testConvert() {
        String testString = "00";
        assertArrayEquals(new byte[] {0x00}, converter.convert(testString));
        testString = "FF";
        assertArrayEquals(new byte[] {(byte) 0xff}, converter.convert(testString));
        testString = "FFFF";
        assertArrayEquals(new byte[] {(byte) 0xff, (byte) 0xff}, converter.convert(testString));
    }

    @Test
    public void testConvertError() {
        assertThrows(ParameterException.class, () -> converter.convert("hello world"));
    }
}
