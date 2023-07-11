/*
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2023 Ruhr University Bochum, Paderborn University, Technology Innovation Institute, and Hackmanit GmbH
 *
 * Licensed under Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package de.rub.nds.tlsattacker.core.constants;

public class ExtensionByteLength {

    /** extensions byte length */
    public static final int EXTENSIONS_LENGTH = 2;

    /** extension type */
    public static final int TYPE = 2;

    /** EC point formats length field of the ec point format extension message */
    public static final int EC_POINT_FORMATS = 1;

    /** Supported Elliptic Curves length field of the elliptic curve extension message */
    public static final int SUPPORTED_GROUPS = 2;
    /** Heartbeat mode length in the heartbeat extension message */
    public static final int HEARTBEAT_MODE = 1;
    /** MaxFragment length field in the MaxFragmentExtension message */
    public static final int MAX_FRAGMENT = 1;
    /** ServerNameType length in the ServerNameIndicationExtension */
    public static final int SERVER_NAME_TYPE = 1;
    /** ServerName length in the ServerNameIndicationExtension */
    public static final int SERVER_NAME = 2;
    /** ServerNameListLength in the ServerNameIndicationExtension */
    public static final int SERVER_NAME_LIST = 2;
    /** ExtendedRandomLength in the ExtendedRandomExtension */
    public static final int EXTENDED_RANDOM_LENGTH = 2;
    /** KeyShareGroup length in the KeyShareExtension */
    public static final int KEY_SHARE_GROUP = 2;
    /** KeyShare length in the KeyShareExtension */
    public static final int KEY_SHARE_LENGTH = 2;
    /** KeyShareListLength in the KeyShareExtension */
    public static final int KEY_SHARE_LIST_LENGTH = 2;
    /** KeyExchangeModes length in the PSKKeyExchangeModesExtension */
    public static final int PSK_KEY_EXCHANGE_MODES_LENGTH = 1;
    /** PSKIdentity length in the PreSharedKeyExtension */
    public static final int PSK_IDENTITY_LENGTH = 2;
    /** PSKList length in the PreSharedKeyExtension */
    public static final int PSK_IDENTITY_LIST_LENGTH = 2;
    /** PSKBinder length in the PreSharedKeyExtension */
    public static final int PSK_BINDER_LENGTH = 1;
    /** PSKBinderList length in the PreSharedKeyExtension */
    public static final int PSK_BINDER_LIST_LENGTH = 2;
    /** SelectedIdentity length in the PreSharedKeyExtension */
    public static final int PSK_SELECTED_IDENTITY_LENGTH = 2;
    /** TicketAge length in the PreSharedKeyExtension */
    public static final int TICKET_AGE_LENGTH = 4;

    /** Based on the suggested length of the encrypted session ticket */
    public static final int ENCRYPTED_SESSION_TICKET_STATE_LENGTH = 2;

    /** MaxEarlyDataSize length in the EarlyDataExtension */
    public static final int MAX_EARLY_DATA_SIZE_LENGTH = 4;
    /**
     * Length of the Signature and HashAlgorithm Length field of the SignatureAndHashAlgorithms
     * Extension
     */
    public static final int SIGNATURE_AND_HASH_ALGORITHMS_LENGTH = 2;
    /** Supported Protocol Versions length field of the SupportedVersionsExtension message */
    public static final int SUPPORTED_PROTOCOL_VERSIONS_LENGTH = 1;

    /** Length of the Padding Length field of the Padding Extension */
    public static final int PADDING_LENGTH = 2;

    public static final int SIGNATURE_AND_HASH_ALGORITHMS = 2;

    /** Length of the version field as used by the token binding extension. */
    public static final int TOKENBINDING_VERSION = 2;

    /** Length of the token binding extension key parameter length field */
    public static final int TOKENBINDING_KEYPARAMETER_LENGTH = 1;

    /** Length of the certificate status request responder id list length field */
    public static final int CERTIFICATE_STATUS_REQUEST_RESPONDER_ID_LIST_LENGTH = 2;

    /** Length of the certificate status request "request extension" length field */
    public static final int CERTIFICATE_STATUS_REQUEST_REQUEST_EXTENSION_LENGTH = 2;

    /** Length of the certificate status request status type field */
    public static final int CERTIFICATE_STATUS_REQUEST_STATUS_TYPE = 1;

    /** Length of the application layer protocol extension length field */
    public static final int ALPN_EXTENSION_LENGTH = 2;

    public static final int ALPN_ENTRY_LENGTH = 1;

    /** Length of the SRP extension identifier length field */
    public static final int SRP_IDENTIFIER_LENGTH = 1;

    /** Length of the SRTP extension master key identifier length field */
    public static final int SRTP_MASTER_KEY_IDENTIFIER_LENGTH = 1;

    /** Length of the SRTP extension protection profiles length field length */
    public static final int SRTP_PROTECTION_PROFILES_LENGTH = 2;

    /** Length of the user mapping extension user mapping hint field */
    public static final int USER_MAPPING_MAPPINGTYPE = 1;

    /** Length of the certificate_type certificate_types length field */
    public static final int CERTIFICATE_TYPE_TYPE_LENGTH = 1;

    /** Length of the client authz extension length field */
    public static final int CLIENT_AUTHZ_FORMAT_LIST_LENGTH = 1;

    /** Length of the server authz extension length field */
    public static final int SERVER_AUTHZ_FORMAT_LIST_LENGTH = 1;

    /** Length of the cached information extension length field */
    public static final int CACHED_INFO_LENGTH = 2;

    /** Length of the CachedInfoType */
    public static final int CACHED_INFO_TYPE = 1;

    /** Length of the Cached Info extension hash value length */
    public static final int CACHED_INFO_HASH_LENGTH = 1;

    /** Length of the trusted ca indication authority type length */
    public static final int TRUSTED_AUTHORITY_TYPE = 1;

    /** Length of the trusted ca indication sha1 hash length */
    public static final int TRUSTED_AUTHORITY_HASH = 20;

    /** Length of the trusted ca indication distinguished name length field */
    public static final int TRUSTED_AUTHORITY_DISTINGUISHED_NAME_LENGTH = 2;

    /** Length of the trusted ca indication trusted authority list */
    public static final int TRUSTED_AUTHORITY_LIST_LENGTH = 2;

    /** Length of the status request v2 responder id length */
    public static final int CERTIFICATE_STATUS_REQUEST_V2_RESPONDER_ID = 2;

    /** Length of the status request v2 request extension length */
    public static final int CERTIFICATE_STATUS_REQUEST_V2_REQUEST_EXTENSION = 2;

    /** Length of the status request v2 request length */
    public static final int CERTIFICATE_STATUS_REQUEST_V2_REQUEST_LENGTH = 2;

    /** Length of the status request v2 list length */
    public static final int CERTIFICATE_STATUS_REQUEST_V2_LIST = 2;

    public static final int RENEGOTIATION_INFO = 1;

    /** PWD_NAME length field of the pwd_clear and pwd_protect extension messages */
    public static final int PWD_NAME = 1;

    public static final int PWD_SCALAR = 1;

    /**
     * PASSWORD_SALT length field of the password_salt extension message
     *
     * <p>Note that the field has a different length than the salt field in the ServerKeyExchange
     * for some reason
     */
    public static final int PASSWORD_SALT = 2;

    /** Fields in the DNS Record of the EncryptedServerNameIndicationExtension */
    public static final int ESNI_RECORD_VERSION = 2;

    public static final int ESNI_RECORD_CHECKSUM = 4;

    public static final int ESNI_RECORD_PUBLIC_NAME = 2;

    public static final int ESNI_RECORD_PADDED_LENGTH = 2;

    public static final int ESNI_RECORD_NOT_BEFORE = 8;

    public static final int ESNI_RECORD_NOT_AFTER = 8;

    public static final int ESNI_RECORD_EXTENSIONS = 2;

    /** Length of encryptedSni in the EncryptedServerNameIndicationExtension */
    public static final int ENCRYPTED_SNI_LENGTH = 2;

    /** Nonce in EncryptedServerNameIndicationExtension */
    public static final int NONCE = 16;

    /** PaddedLength in the ClientEsniInner of the EncryptedServerNameIndicationExtension */
    public static final int PADDED_LENGTH = 2;

    /** recordDigestLength in EncryptedServerNameIndicationExtension */
    public static final int RECORD_DIGEST_LENGTH = 2;

    /** Fields in the ECH Config of the EncryptedClientHelloExtension */
    public static final int ECH_CONFIG_LIST_LENGTH = 2;

    public static final int ECH_CONFIG_LENGTH = 2;

    public static final int ECH_CONFIG_PUBLIC_NAME = 1;

    public static final int ECH_CONFIG_PUBLIC_NAME_LONG = 2;

    public static final int ECH_CONFIG_PUBLIC_KEY = 2;

    public static final int ECH_CONFIG_MAX_NAME_LENGTH = 1;

    public static final int ECH_CONFIG_ID = 1;

    public static final int ECH_CONFIG_KEM_ID = 2;

    public static final int ECH_CONFIG_CIPHERSUITES = 2;

    public static final int ECH_CONFIG_KDF_ID = 2;

    public static final int ECH_CONFIG_AEAD_ID = 2;

    public static final int ECH_CLIENT_HELLO_TYPE = 1;

    public static final int ECH_ENC_LENGTH = 2;

    public static final int ECH_PAYLOAD_LENGTH = 2;

    public static final int ECH_ACCEPT_CONFIRMATION_LENGTH = 8;

    /** cookieLength in the CookieExtension */
    public static final int COOKIE_LENGTH = 2;

    /** RecordSizeLimit length in the RecordSizeLimitExtension */
    public static final int RECORD_SIZE_LIMIT_LENGTH = 2;

    /** connectionIdLength in ConnectionIdExtension */
    public static final int CONNECTION_ID_LENGTH = 1;

    /** SignatureAlgorithmsCert Extension Fields */
    public static final int SIGNATURE_ALGORITHMS_CERT_LENGTH = 2;

    public static final int SIGNATURE_ALGORITHMS_CERT = 2;

    private ExtensionByteLength() {}
}
