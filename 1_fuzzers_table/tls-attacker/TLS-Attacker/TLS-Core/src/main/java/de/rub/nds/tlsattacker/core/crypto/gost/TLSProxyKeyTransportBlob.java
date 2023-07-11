/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.crypto.gost;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.cryptopro.GostR3410KeyTransport;

public class TLSProxyKeyTransportBlob extends ASN1Object {
    public static TLSProxyKeyTransportBlob getInstance(Object obj) {
        if (obj instanceof TLSProxyKeyTransportBlob) {
            return (TLSProxyKeyTransportBlob) obj;
        }

        if (obj != null) {
            return new TLSProxyKeyTransportBlob(ASN1Sequence.getInstance(obj));
        }

        return null;
    }

    private final GostR3410KeyTransport keyBlob;
    private final DEROctetString cert;

    private TLSProxyKeyTransportBlob(ASN1Sequence seq) {
        this.keyBlob = GostR3410KeyTransport.getInstance(seq.getObjectAt(0));
        this.cert = (DEROctetString) DEROctetString.getInstance(seq.getObjectAt(1));
    }

    public TLSProxyKeyTransportBlob(GostR3410KeyTransport keyBlob, DEROctetString cert) {
        this.keyBlob = keyBlob;
        this.cert = cert;
    }

    public GostR3410KeyTransport getKeyBlob() {
        return keyBlob;
    }

    public DEROctetString getCert() {
        return cert;
    }

    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(keyBlob);
        v.add(cert);
        return new DERSequence(v);
    }
}
