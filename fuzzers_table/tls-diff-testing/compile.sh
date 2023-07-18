#!/bin/bash

git clone https://github.com/hso-esk/tls-diff-testing.git

sudo apt-get install cryptopp
sudo apt-get update 
sudo apt-get install libcrypto++-dev libcrypto++-doc libcrypto++-utils screen

cp -f stimulator.cpp tls-diff-testing/tls-diff-testing/apps/stimulator/src
cp -f generate_multi.sh tls-diff-testing/tls-diff-testing/generator/macros

cd tls-diff-testing
cd tls-diff-testing

make

cd generator
./macros/generate_multi.sh
cd ..

cd ..
cd ..

exit 0




