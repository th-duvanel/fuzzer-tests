#!/bin/bash

git clone https://github.com/hso-esk/tls-diff-testing.git
git revert --no-commit 40f31a82121c9d8420e634228ce303e4200bcc24 HEAD



apt-get install libcrypto++-dev libcrypto++-doc libcrypto++-utils screen -y

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




