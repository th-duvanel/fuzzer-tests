#!/bin/bash

git clone https://github.com/hso-esk/tls-diff-testing.git

cd tls-diff-testing

#download crypto++

./setup_cryptopp.sh # deu bug no meu

pushd tls-diff-testing
# Build tls-diff-testing components
make


pushd tls-diff-testing/apps/stimulator
# Stimulate TLS servers (adapt "-s20" to the number chosen above)
for fin in $(ls ../../generator/iteration-*/stimuli.hex); do
    ./stimulator -S5 -s20 $fin | tee ${fin}.responses
done
popd

cd ..

exit 0