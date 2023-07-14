#!/bin/bash


pip3 install --pre tlslite-ng
pip3 install gmpy2


# Precisa m2crypto e gmpy para ter maior velocidade.

git clone https://github.com/tlsfuzzer/tlsfuzzer.git

exit 0