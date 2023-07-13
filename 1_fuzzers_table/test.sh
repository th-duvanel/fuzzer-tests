#!/bin/bash

# TLS-Attacker
cd tls-attacker
chmod +x ./compile.sh
./compile.sh
cd ..
./delivery.sh 30 "tls-attacker" "java -jar ./TLS-Attacker/apps/TLS-Client.jar -connect 127.0.0.1"

cd tlsfuzzer
chmod +x ./compile.sh
./compile.sh
cd ..
./delivery 30 "tlsfuzzer" # nao sei ainda

cd tlsbunny
chmod +x ./compile.sh
./compile.sh
cd ..
./delivery.sh 30 "tlsbunny" "java -cp target/tlsbunny-1.0-SNAPSHOT-all.jar \
    com.gypsyengineer.tlsbunny.tls13.client.fuzzer.DeepHandshakeFuzzyClient"
echo "[ IT'S SUPPOSED TO EXIST AN ERROR THAT IS IMPOSSIBLE TO FIND FUZZER'S OWN JAVA CLASS. ]"

cd cryptofuzz
chmod +x ./compile.sh
./compile.sh
echo "[ IT'S SUPPOSED TO EXIST AND ERROR THAT THE OWN CLANG CAN'T COMPILE THE STD++17 LIB. ]"
cd ..

exit 0