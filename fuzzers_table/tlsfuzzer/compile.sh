#!/bin/bash


pip3 install --pre tlslite-ng
pip3 install gmpy2


# Precisa m2crypto e gmpy para ter maior velocidade.

git clone https://github.com/tlsfuzzer/tlsfuzzer.git
git revert --no-commit 4e68c41aa029b5303d3acf03050072a0e9be64d5 HEAD

exit 0