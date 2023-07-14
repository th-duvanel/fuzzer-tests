#!/bin/bash

if ! command -v java &> /dev/null; then
    apt-get install openjdk-11-jdk -y
    apt-get install maven -y
fi

git clone https://github.com/tls-attacker/TLS-Attacker.git

cd TLS-Attacker

mvn clean install -DskipTests=true

cd ..

exit 0