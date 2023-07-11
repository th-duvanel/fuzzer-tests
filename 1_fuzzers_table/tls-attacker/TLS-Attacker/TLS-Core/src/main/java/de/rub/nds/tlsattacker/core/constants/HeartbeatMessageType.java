/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.constants;

import java.util.HashMap;
import java.util.Map;

public enum HeartbeatMessageType {
    HEARTBEAT_REQUEST((byte) 1),
    HEARTBEAT_RESPONSE((byte) 2);

    private byte value;

    private static final Map<Byte, HeartbeatMessageType> MAP;

    private HeartbeatMessageType(byte value) {
        this.value = value;
    }

    static {
        MAP = new HashMap<>();
        for (HeartbeatMessageType cm : HeartbeatMessageType.values()) {
            MAP.put(cm.value, cm);
        }
    }

    public static HeartbeatMessageType getHeartbeatMessageType(byte value) {
        return MAP.get(value);
    }

    public byte getValue() {
        return value;
    }

    public byte[] getArrayValue() {
        return new byte[] {value};
    }
}
