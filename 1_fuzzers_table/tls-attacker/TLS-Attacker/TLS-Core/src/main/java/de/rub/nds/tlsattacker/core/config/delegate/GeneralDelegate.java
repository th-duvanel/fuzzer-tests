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
import java.security.Provider;
import java.security.Security;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class GeneralDelegate extends Delegate {

    private static final Logger LOGGER = LogManager.getLogger();

    @Parameter(
            names = {"-h", "-help"},
            help = true,
            description = "Prints usage for all the existing commands.")
    private boolean help;

    @Parameter(names = "-debug", description = "Show extra debug output (sets logLevel to DEBUG)")
    private boolean debug;

    @Parameter(names = "-quiet", description = "No output (sets logLevel to NONE)")
    private boolean quiet;

    @Parameter(names = "-keylogfile", description = "Path to the keylogfile")
    protected String keylogfile = null;

    public GeneralDelegate() {}

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public String getKeylogfile() {
        return keylogfile;
    }

    public void setKeylogfile(String keylogfile) {
        this.keylogfile = keylogfile;
    }

    @Override
    public void applyDelegate(Config config) {
        Security.addProvider(new BouncyCastleProvider());
        if (isDebug()) {
            Configurator.setAllLevels("de.rub.nds.tlsattacker", Level.DEBUG);
        } else if (quiet) {
            Configurator.setAllLevels("de.rub.nds.tlsattacker", Level.OFF);
        }
        LOGGER.debug("Using the following security providers");
        for (Provider p : Security.getProviders()) {
            LOGGER.debug("Provider {}, version, {}", p.getName(), p.getVersion());
        }

        if (keylogfile != null) {
            config.setKeylogFilePath(keylogfile);
            config.setWriteKeylogFile(true);
        }
    }
}
