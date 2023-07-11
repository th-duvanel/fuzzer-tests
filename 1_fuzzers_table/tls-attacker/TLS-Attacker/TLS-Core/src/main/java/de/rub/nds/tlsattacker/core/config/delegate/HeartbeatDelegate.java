/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.config.delegate;

import com.beust.jcommander.Parameter;
import de.rub.nds.tlsattacker.core.config.Config;
import de.rub.nds.tlsattacker.core.constants.HeartbeatMode;

public class HeartbeatDelegate extends Delegate {

    @Parameter(names = "-heartbeat_mode", description = "Sets the heartbeat mode")
    private HeartbeatMode heartbeatMode = null;

    public HeartbeatDelegate() {}

    public HeartbeatMode getHeartbeatMode() {
        return heartbeatMode;
    }

    public void setHeartbeatMode(HeartbeatMode heartbeatMode) {
        this.heartbeatMode = heartbeatMode;
    }

    @Override
    public void applyDelegate(Config config) {
        if (heartbeatMode != null) {
            config.setHeartbeatMode(heartbeatMode);
            config.setAddHeartbeatExtension(true);
        }
    }
}
