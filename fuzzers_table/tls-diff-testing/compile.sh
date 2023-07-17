#!/bin/bash

git clone https://github.com/hso-esk/tls-diff-testing.git

cd tls-diff-testing

sudo apt-get install cryptopp
sudo apt-get update 
sudo apt-get install libcrypto++-dev libcrypto++-doc libcrypto++-utils

cd tls-diff-testing
cd tls-diff-testing

make

pushd generator
# Run input generation tool multiple times (adapt parameters to your needs within shell script)
./macros/generate_multi.sh
popd
popd



cd ..
cd ..

exit 0