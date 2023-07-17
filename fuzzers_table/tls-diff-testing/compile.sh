#!/bin/bash

git clone https://github.com/hso-esk/tls-diff-testing.git

cd tls-diff-testing

sudo apt-get install cryptopp
sudo apt-get update 
sudo apt-get install libcrypto++-dev libcrypto++-doc libcrypto++-utils screen


cd tls-diff-testing
cd tls-diff-testing

make


# isso não é aqui 
pushd generator
./macros/generate_multi.sh
pid=$!
popd
popd
sleep 5
kill -9 $pid 2>/dev/null


cd ..
cd ..

exit 0




