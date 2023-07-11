#pragma once

namespace cryptofuzz {
namespace module {

template <class T>
class CTX_Copier {
    private:
        T* ctx = nullptr;
        Datasource& ds;

        T* newCTX(void) const;
        int copyCTX(T* dest, T* src) const;
        void freeCTX(T* ctx) const;

        T* copy(void) {
            return ctx;
            bool doCopyCTX = true;
            try {
                doCopyCTX = ds.Get<bool>();
            } catch ( fuzzing::datasource::Datasource::OutOfData ) { }

            if ( doCopyCTX == true ) {
                T* tmpCtx = newCTX();
                if ( tmpCtx != nullptr ) {
                    if ( copyCTX(tmpCtx, ctx) == 1 ) {
                        /* Copy succeeded, free the old ctx */
                        freeCTX(ctx);

                        /* Use the copied ctx */
                        ctx = tmpCtx;
                    } else {
                        freeCTX(tmpCtx);
                    }
                }
            }

            return ctx;
        }

    public:
        CTX_Copier(Datasource& ds) :
            ds(ds) {
            ctx = newCTX();
            if ( ctx == nullptr ) {
                abort();
            }
        }

        T* GetPtr(void) {
            return copy();
        }

        ~CTX_Copier() {
            freeCTX(ctx);
        }
};

class EC_GROUP_Copier {
    private:
        bool locked = false;
        const int curveNID;
        EC_GROUP* group = nullptr;
        Datasource& ds;

        EC_GROUP* newGroup(void) {
            return EC_GROUP_new_by_curve_name(curveNID);
        }

#if !defined(CRYPTOFUZZ_BORINGSSL) && !defined(CRYPTOFUZZ_WOLFCRYPT_OPENSSL)
        int copyGroup(EC_GROUP* dest, EC_GROUP* src) {
            return EC_GROUP_copy(dest, src);
        }
#endif
        void freeGroup(EC_GROUP* group) {
            /* noret */ EC_GROUP_free(group);
        }

        EC_GROUP* copy(void) {
            bool doCopyGroup = true;
            try {
                doCopyGroup = ds.Get<bool>();
            } catch ( fuzzing::datasource::Datasource::OutOfData ) { }

            if ( doCopyGroup == true ) {
#if !defined(CRYPTOFUZZ_BORINGSSL) && !defined(CRYPTOFUZZ_WOLFCRYPT_OPENSSL)
                EC_GROUP* tmpGroup = newGroup();
                if ( tmpGroup != nullptr ) {
                    if ( copyGroup(tmpGroup, group) == 1 ) {
                        /* Copy succeeded, free the old group */
                        freeGroup(group);

                        /* Use the copied group */
                        group = tmpGroup;
                    } else {
                        freeGroup(tmpGroup);
                    }
                }
#endif
            }

            return group;
        }

    public:
        EC_GROUP_Copier(Datasource& ds, const int curveNID) :
            curveNID(curveNID), ds(ds) {
            group = newGroup();
        }

        void Lock(void) {
            locked = true;
        }

        EC_GROUP* GetPtr(void) {
            if ( locked == true ) {
                return group;
            } else {
                return copy();
            }
        }

        ~EC_GROUP_Copier() {
            freeGroup(group);
        }
};

class EC_POINT_Copier {
    private:
        std::shared_ptr<EC_GROUP_Copier> group;
        EC_POINT* point = nullptr;
        Datasource& ds;

        EC_POINT* newPoint(void) {
            return EC_POINT_new(group->GetPtr());
        }

        int copyPoint(EC_POINT* dest, EC_POINT* src) {
            return EC_POINT_copy(dest, src);
        }
        void freePoint(EC_POINT* point) {
            /* noret */ EC_POINT_free(point);
        }

        EC_POINT* copy(void) {
            bool doCopyPoint = true;
            try {
                doCopyPoint = ds.Get<bool>();
            } catch ( fuzzing::datasource::Datasource::OutOfData ) { }

            if ( doCopyPoint == true ) {
                EC_POINT* tmpPoint = newPoint();
                if ( tmpPoint != nullptr ) {
                    if ( copyPoint(tmpPoint, point) == 1 ) {
                        /* Copy succeeded, free the old point */
                        freePoint(point);

                        /* Use the copied point */
                        point = tmpPoint;
                    } else {
                        freePoint(tmpPoint);
                    }
                }
            }

            return point;
        }

        bool set(OpenSSL_bignum::Bignum& pub_x, OpenSSL_bignum::Bignum& pub_y) {
            bool ret = false;

            CF_CHECK_NE(EC_POINT_set_affine_coordinates(group->GetPtr(), GetPtr(), pub_x.GetPtr(), pub_y.GetPtr(), nullptr), 0);

            ret = true;
end:
            return ret;
        }

    public:
        EC_POINT_Copier(Datasource& ds, std::shared_ptr<EC_GROUP_Copier> group) :
            group(group), ds(ds) {
            point = newPoint();
            if ( point == nullptr ) {
                abort();
            }
        }

        bool Set(OpenSSL_bignum::Bignum& pub_x, OpenSSL_bignum::Bignum& pub_y) {
            return set(pub_x, pub_y);
        }

        bool Set(const component::Bignum& pub_x, const component::Bignum& pub_y) {
            bool ret = false;

            OpenSSL_bignum::Bignum _pub_x(ds), _pub_y(ds);

            CF_CHECK_EQ(_pub_x.Set(pub_x.ToString(ds)), true);
            CF_CHECK_EQ(_pub_y.Set(pub_y.ToString(ds)), true);

            CF_CHECK_TRUE(Set(_pub_x, _pub_y));

            ret = true;
end:
            return ret;
        }

        bool Get(OpenSSL_bignum::Bignum& pub_x, OpenSSL_bignum::Bignum& pub_y) {
            bool ret = false;

            CF_CHECK_NE(EC_POINT_get_affine_coordinates(group->GetPtr(), GetPtr(), pub_x.GetDestPtr(), pub_y.GetDestPtr(), nullptr), 0);

            ret = true;
end:
            return ret;
        }

        std::optional<component::ECC_Point> Get(void) {
            std::optional<component::ECC_Point> ret = std::nullopt;

            char* x_str = nullptr;
            char* y_str = nullptr;

            OpenSSL_bignum::Bignum x(ds);
            OpenSSL_bignum::Bignum y(ds);

            CF_CHECK_EQ(x.New(), true);
            CF_CHECK_EQ(y.New(), true);

            CF_CHECK_TRUE(Get(x, y));

            CF_CHECK_NE(x_str = BN_bn2dec(x.GetPtr()), nullptr);
            CF_CHECK_NE(y_str = BN_bn2dec(y.GetPtr()), nullptr);

            ret = { std::string(x_str), std::string(y_str) };

end:
            OPENSSL_free(x_str);
            OPENSSL_free(y_str);

            return ret;
        }

        EC_POINT* GetPtr(void) {
            return copy();
        }

        ~EC_POINT_Copier() {
            freePoint(point);
        }
};

#if !defined(CRYPTOFUZZ_OPENSSL_102)
template<> EVP_MD_CTX* CTX_Copier<EVP_MD_CTX>::newCTX(void) const { return EVP_MD_CTX_new(); }
#else
template<> EVP_MD_CTX* CTX_Copier<EVP_MD_CTX>::newCTX(void) const {
    EVP_MD_CTX* ret = (EVP_MD_CTX*)malloc(sizeof(*ret));
    EVP_MD_CTX_init(ret);
    return ret;
}
#endif

template<> int CTX_Copier<EVP_MD_CTX>::copyCTX(EVP_MD_CTX* dest, EVP_MD_CTX* src) const { return EVP_MD_CTX_copy(dest, src); }

#if !defined(CRYPTOFUZZ_OPENSSL_102)
template<> void CTX_Copier<EVP_MD_CTX>::freeCTX(EVP_MD_CTX* ctx) const { EVP_MD_CTX_free(ctx); }
#else
template<> void CTX_Copier<EVP_MD_CTX>::freeCTX(EVP_MD_CTX* ctx) const { EVP_MD_CTX_cleanup(ctx); free(ctx); }
#endif

template<> EVP_CIPHER_CTX* CTX_Copier<EVP_CIPHER_CTX>::newCTX(void) const { return EVP_CIPHER_CTX_new(); }
template<> int CTX_Copier<EVP_CIPHER_CTX>::copyCTX(EVP_CIPHER_CTX* dest, EVP_CIPHER_CTX* src) const {
#if !defined(CRYPTOFUZZ_WOLFCRYPT_OPENSSL)
    return EVP_CIPHER_CTX_copy(dest, src);
#else
    (void)dest;
    (void)src;

    /* EVP_CIPHER_CTX_copy is not implemented in wolfCrypt; return failure */
    return 0;
#endif
}
template<> void CTX_Copier<EVP_CIPHER_CTX>::freeCTX(EVP_CIPHER_CTX* ctx) const { return EVP_CIPHER_CTX_free(ctx); }

#if !defined(CRYPTOFUZZ_OPENSSL_102)
template<> HMAC_CTX* CTX_Copier<HMAC_CTX>::newCTX(void) const { return HMAC_CTX_new(); }
template<> int CTX_Copier<HMAC_CTX>::copyCTX(HMAC_CTX* dest, HMAC_CTX* src) const { return HMAC_CTX_copy(dest, src); }
template<> void CTX_Copier<HMAC_CTX>::freeCTX(HMAC_CTX* ctx) const { return HMAC_CTX_free(ctx); }
#endif

template<> CMAC_CTX* CTX_Copier<CMAC_CTX>::newCTX(void) const { return CMAC_CTX_new(); }
template<> int CTX_Copier<CMAC_CTX>::copyCTX(CMAC_CTX* dest, CMAC_CTX* src) const {
    (void)dest;
    (void)src;
    return 0;
}
template<> void CTX_Copier<CMAC_CTX>::freeCTX(CMAC_CTX* ctx) const { return CMAC_CTX_free(ctx); }

template<> EC_KEY* CTX_Copier<EC_KEY>::newCTX(void) const { return EC_KEY_new(); }
template<> int CTX_Copier<EC_KEY>::copyCTX(EC_KEY* dest, EC_KEY* src) const {
#if !defined(CRYPTOFUZZ_BORINGSSL) && !defined(CRYPTOFUZZ_WOLFCRYPT_OPENSSL)
    return EC_KEY_copy(dest, src) == nullptr ? 0 : 1;
#else
    (void)dest;
    (void)src;
    return 0;
#endif
}
template<> void CTX_Copier<EC_KEY>::freeCTX(EC_KEY* ctx) const { return EC_KEY_free(ctx); }

using CF_EVP_MD_CTX = CTX_Copier<EVP_MD_CTX>;
using CF_EVP_CIPHER_CTX = CTX_Copier<EVP_CIPHER_CTX>;
using CF_HMAC_CTX = CTX_Copier<HMAC_CTX>;
using CF_CMAC_CTX = CTX_Copier<CMAC_CTX>;
using CF_EC_KEY = CTX_Copier<EC_KEY>;
using CF_EC_POINT = EC_POINT_Copier;
using CF_EC_GROUP = EC_GROUP_Copier;
} /* namespace module */
} /* namespace cryptofuzz */
