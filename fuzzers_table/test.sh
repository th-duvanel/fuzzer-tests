#!/bin/bash

# TLS-Attacker
cd tls-attacker
chmod +x ./compile.sh
./compile.sh
cd ..
./delivery.sh 30 "tls-attacker" "java -jar ./TLS-Attacker/apps/TLS-Client.jar -connect 127.0.0.1"

sleep 5

cd tlsfuzzer
chmod +x ./compile.sh
./compile.sh
cd ..
./delivery.sh 30 "tlsfuzzer" "PYTHONPATH=. python3 scripts/test-tls13-finished-plaintext.py -d" "tlsfuzzer"

sleep 5

cd tlsbunny
chmod +x ./compile.sh
./compile.sh
cd ..
./delivery.sh 1 "tlsbunny" "java -cp target/tlsbunny-1.0-SNAPSHOT-all.jar \
    com.gypsyengineer.tlsbunny.tls13.client.fuzzer.DeepHandshakeFuzzyClient"
echo "[ IT'S SUPPOSED TO EXIST AN ERROR THAT IS IMPOSSIBLE TO FIND FUZZER'S OWN JAVA CLASS. ]"

sleep 5

cd cryptofuzz
./compile.sh
cd ..
echo "[ IT'S SUPPOSED TO EXIST AN ERROR - There was a linking error due to undefined references]"



sleep 5

cd tls-diff-testing
./compile.sh
cd ..

./delivery.sh 30 "tls-diff-testing" "for fin in $(ls tls-diff-testing/tls-diff-testing/generator/iteration-*/stimuli.hex); do
    ./tls-diff-testing/tls-diff-testing/apps/stimulator -S5 -s20 $fin | tee ${fin}.responses
done"

sleep 5

exit 0
