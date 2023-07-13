#!/bin/bash

python get-pip.py

pip install --pre tlslite-ng

# Precisa m2crypto e gmpy para ter maior velocidade.

git clone https://github.com/tlsfuzzer/tlsfuzzer.git

cd tlsfuzzer