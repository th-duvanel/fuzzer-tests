#!/bin/bash

# TLS-Attacker
cd tls-attacker
chmod +x compile.sh
./compile.sh
cd ..
echo "[ RUNNING TLS ATTACKER!!!! ]"
./delivery.sh 30 "tls-attacker" "java -jar ./TLS-Attacker/apps/TLS-Client.jar -connect 127.0.0.1:4433"

sleep 5

cd tlsfuzzer
chmod +x compile.sh
./compile.sh
cd ..
echo "[ RUNNING TLS FUZZER! ]"
./delivery.sh 30 "tlsfuzzer" "PYTHONPATH=tlsfuzzer python3 tlsfuzzer/scripts/test-fuzzed-plaintext.py"

sleep 5

cd tlsbunny
chmod +x compile.sh
./compile.sh
echo "[ RUNNING TLSBUNNY!!!! ]"
java -cp target/tlsbunny-1.0-SNAPSHOT-all.jar \
    com.gypsyengineer.tlsbunny.tls13.client.fuzzer.DeepHandshakeFuzzyClient
echo "[ IT'S SUPPOSED TO EXIST AN ERROR THAT IS IMPOSSIBLE TO FIND FUZZER'S OWN JAVA CLASS. ]"

cd ..

sleep 5

cd tls-diff-testing
./compile.sh
cd ..
echo "[ RUNNING TLS-DIFF!!!! ]"
./delivery.sh 30 "tls-diff-testing" "for fin in \$(ls tls-diff-testing/tls-diff-testing/generator/iteration-*/stimuli.hex); do ./tls-diff-testing/tls-diff-testing/apps/stimulator/stimulator -s1 \$fin | tee \${fin}.responses; done"

sleep 5

cd nezha
./compile.sh
cd ..

sleep 5

cd cryptofuzz
./compile.sh
cd ..

sleep 5


python3 statistics.py < results.txt

exit 0
