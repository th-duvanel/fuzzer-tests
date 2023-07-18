#!/bin/bash

# Script para compilação do fuzzer cryptofuzz. Inclui dependências e exports.
# Tudo de acordo com a própria página do fuzzer.
git clone https://github.com/guidovranken/cryptofuzz.git
git revert --no-commit 9008c12e70028d0d56cb4870c1516873137311e4 HEAD
cd cryptofuzz/

python3 gen_repository.py

apt-get install libc++-dev -y
apt-get install libc++abi-dev -y

pip install jsonschema
pip install jinja2

export CC=clang
export CXX=clang++

export CFLAGS="-fsanitize=address,undefined,fuzzer-no-link -O2 -g"
export CXXFLAGS="-fsanitize=address,undefined,fuzzer-no-link -D_GLIBCXX_DEBUG -O2 -g"

# OpenSSL
if [[ ! -d openssl ]]
then
    git clone --depth 1 https://github.com/openssl/openssl.git
    cd openssl/
    ./config enable-md2 enable-rc5
    make -j$(nproc)
    export OPENSSL_INCLUDE_PATH=`realpath include/`
    export OPENSSL_LIBCRYPTO_A_PATH=`realpath libcrypto.a`
    export CXXFLAGS="$CXXFLAGS -I $OPENSSL_INCLUDE_PATH"
fi

cd ../modules/openssl/

apt-get install libboost-all-dev -y

make

cd ..
cd ..

# mBedTLS
if [[ ! -d mbedtls ]]
then
    git clone --depth 1 -b development https://github.com/ARMmbed/mbedtls.git
    cd mbedtls/
    scripts/config.pl set MBEDTLS_PLATFORM_MEMORY
    scripts/config.pl set MBEDTLS_CMAC_C
    scripts/config.pl set MBEDTLS_NIST_KW_C
    scripts/config.pl set MBEDTLS_ARIA_C
    mkdir build/
    cd build/
    cmake .. -DENABLE_PROGRAMS=0 -DENABLE_TESTING=0
    make -j$(nproc)
    export MBEDTLS_LIBMBEDCRYPTO_A_PATH=$(realpath library/libmbedcrypto.a)
    export MBEDTLS_INCLUDE_PATH=$(realpath ../include)
    export CXXFLAGS="$CXXFLAGS -DCRYPTOFUZZ_MBEDTLS"
fi

cd ../../modules/openssl/
make

cd ..
cd ..

export LIBFUZZER_LINK="-fsanitize=fuzzer"

export CXXFLAGS="$CXXFLAGS -ferror-limit=0 -stdlib=libc++"

make

echo "[ IT'S SUPPOSED TO EXIST a linking error due to undefined references]"
