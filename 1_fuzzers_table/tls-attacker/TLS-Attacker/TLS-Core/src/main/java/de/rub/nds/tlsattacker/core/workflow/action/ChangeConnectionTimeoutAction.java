/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.workflow.action;

import de.rub.nds.tlsattacker.core.exceptions.ActionExecutionException;
import de.rub.nds.tlsattacker.core.layer.context.TlsContext;
import de.rub.nds.tlsattacker.core.state.State;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@XmlRootElement
public class ChangeConnectionTimeoutAction extends ConnectionBoundAction {

    private static final Logger LOGGER = LogManager.getLogger();

    private long newValue;
    private long oldValue;

    public ChangeConnectionTimeoutAction(long newValue) {
        super();
        this.newValue = newValue;
    }

    public ChangeConnectionTimeoutAction() {}

    public void setNewValue(long newValue) {
        this.newValue = newValue;
    }

    public long getNewValue() {
        return newValue;
    }

    public long getOldValue() {
        return oldValue;
    }

    @Override
    public void execute(State state) throws ActionExecutionException {
        TlsContext tlsContext = state.getContext(getConnectionAlias()).getTlsContext();

        if (isExecuted()) {
            throw new ActionExecutionException("Action already executed!");
        }
        oldValue = tlsContext.getContext().getTransportHandler().getTimeout();
        tlsContext.getContext().getTransportHandler().setTimeout(newValue);
        LOGGER.info(
                "Changed Timeout from " + oldValue == null ? oldValue : null + " to " + newValue);
        setExecuted(true);
    }

    @Override
    public void reset() {
        setExecuted(null);
    }

    @Override
    public boolean executedAsPlanned() {
        return isExecuted();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (int) (this.newValue ^ (this.newValue >>> 32));
        hash = 53 * hash + (int) (this.oldValue ^ (this.oldValue >>> 32));
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
        final ChangeConnectionTimeoutAction other = (ChangeConnectionTimeoutAction) obj;
        if (this.newValue != other.newValue) {
            return false;
        }
        if (this.oldValue != other.oldValue) {
            return false;
        }
        return true;
    }
}
