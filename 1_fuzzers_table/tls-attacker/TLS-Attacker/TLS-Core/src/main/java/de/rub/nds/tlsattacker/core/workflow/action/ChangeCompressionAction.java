/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.workflow.action;

import de.rub.nds.tlsattacker.core.constants.CompressionMethod;
import de.rub.nds.tlsattacker.core.exceptions.ActionExecutionException;
import de.rub.nds.tlsattacker.core.layer.context.TlsContext;
import de.rub.nds.tlsattacker.core.state.State;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@XmlRootElement
public class ChangeCompressionAction extends ConnectionBoundAction {

    private static final Logger LOGGER = LogManager.getLogger();

    private CompressionMethod newValue = null;
    private CompressionMethod oldValue = null;

    public ChangeCompressionAction(CompressionMethod newValue) {
        super();
        this.newValue = newValue;
    }

    public ChangeCompressionAction() {}

    public void setNewValue(CompressionMethod newValue) {
        this.newValue = newValue;
    }

    public CompressionMethod getNewValue() {
        return newValue;
    }

    public CompressionMethod getOldValue() {
        return oldValue;
    }

    @Override
    public void execute(State state) throws ActionExecutionException {
        TlsContext tlsContext = state.getContext(getConnectionAlias()).getTlsContext();

        if (isExecuted()) {
            throw new ActionExecutionException("Action already executed!");
        }
        oldValue = tlsContext.getSelectedCompressionMethod();
        tlsContext.setSelectedCompressionMethod(newValue);
        tlsContext.getRecordLayer().updateCompressor();
        tlsContext.getRecordLayer().updateDecompressor();
        LOGGER.info(
                "Changed selected CompressionMethod from "
                        + (oldValue == null ? "null" : oldValue.name())
                        + " to "
                        + newValue.name());
        setExecuted(true);
    }

    @Override
    public void reset() {
        oldValue = null;
        setExecuted(null);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.newValue);
        hash = 23 * hash + Objects.hashCode(this.oldValue);
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
        final ChangeCompressionAction other = (ChangeCompressionAction) obj;
        if (this.newValue != other.newValue) {
            return false;
        }
        return this.oldValue == other.oldValue;
    }

    @Override
    public boolean executedAsPlanned() {
        return isExecuted();
    }
}
