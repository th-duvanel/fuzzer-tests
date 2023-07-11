/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.protocol.serializer.extension;

import de.rub.nds.tlsattacker.core.protocol.message.extension.ServerCertificateTypeExtensionMessage;
import de.rub.nds.tlsattacker.core.protocol.parser.extension.ServerCertificateTypeExtensionParserTest;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class ServerCertificateTypeExtensionSerializerTest
        extends AbstractExtensionMessageSerializerTest<
                ServerCertificateTypeExtensionMessage, ServerCertificateTypeExtensionSerializer> {

    public ServerCertificateTypeExtensionSerializerTest() {
        super(
                ServerCertificateTypeExtensionMessage::new,
                ServerCertificateTypeExtensionSerializer::new,
                List.of(
                        (msg, obj) -> {
                            if (obj != null) {
                                msg.setCertificateTypesLength((Integer) obj);
                            }
                        },
                        (msg, obj) -> msg.setCertificateTypes((byte[]) obj),
                        (msg, obj) -> msg.setIsClientMessage((Boolean) obj)));
    }

    public static Stream<Arguments> provideTestVectors() {
        return ServerCertificateTypeExtensionParserTest.provideTestVectors();
    }
}
