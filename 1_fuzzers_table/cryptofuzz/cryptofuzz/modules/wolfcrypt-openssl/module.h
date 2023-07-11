#pragma once

#include <cryptofuzz/components.h>
#include <cryptofuzz/module.h>
#include <optional>

namespace cryptofuzz {
namespace module {

class wolfCrypt_OpenSSL : public Module {
    public:
        wolfCrypt_OpenSSL(void);
        std::optional<component::Digest> OpDigest(operation::Digest& op) override;
        std::optional<component::MAC> OpHMAC(operation::HMAC& op) override;
        std::optional<component::Bignum> OpBignumCalc(operation::BignumCalc& op) override;
        std::optional<component::Ciphertext> OpSymmetricEncrypt(operation::SymmetricEncrypt& op) override;
        std::optional<component::Cleartext> OpSymmetricDecrypt(operation::SymmetricDecrypt& op) override;
        std::optional<component::MAC> OpCMAC(operation::CMAC& op) override;
        std::optional<component::Key> OpKDF_HKDF(operation::KDF_HKDF& op) override;
        std::optional<component::Key> OpKDF_PBKDF2(operation::KDF_PBKDF2& op) override;
        std::optional<bool> OpECC_ValidatePubkey(operation::ECC_ValidatePubkey& op) override;
        std::optional<component::ECC_PublicKey> OpECC_PrivateToPublic(operation::ECC_PrivateToPublic& op) override;
        std::optional<component::ECC_Point> OpECC_Point_Add(operation::ECC_Point_Add& op) override;
        std::optional<component::ECC_Point> OpECC_Point_Mul(operation::ECC_Point_Mul& op) override;
        std::optional<component::ECC_Point> OpECC_Point_Neg(operation::ECC_Point_Neg& op) override;
        std::optional<bool> OpECC_Point_Cmp(operation::ECC_Point_Cmp& op) override;
        std::optional<bool> OpECDSA_Verify(operation::ECDSA_Verify& op) override;
        std::optional<component::ECDSA_Signature> OpECDSA_Sign(operation::ECDSA_Sign& op) override;
};

} /* namespace module */
} /* namespace cryptofuzz */
