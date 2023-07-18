#!/bin/bash

cd tls-attacker
rm -rf TLS-Attacker
rm -rf tshark
rm -f results.txt
cd ..

cd tlsfuzzer
rm -rf tlsfuzzer
rm -rf tshark
rm -f results.txt
cd ..

cd tlsbunny
rm -rf tlsbunny
rm -rf tshark
rm -f results.txt
cd ..

cd cryptofuzz
rm -rf cryptofuzz
rm -rf tshark
rm -f results.txt
cd ..

cd tls-diff-testing
rm -rf tls-diff-testing
rm -rf tshark
rm -rf *.responses
rm -f results.txt
cd ..

rm -f key.pem
rm -f cert.pem

exit 0