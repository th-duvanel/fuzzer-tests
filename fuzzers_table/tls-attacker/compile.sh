#!/bin/bash

if ! command -v java &> /dev/null; then
    echo "[ INSTALLING JAVA JDK-11 AND MAVEN!!!! ]"
    apt-get install openjdk-11-jdk -y
    apt-get install maven -y
fi

echo "[ CLONING FUZZER REPO!!!! ]"
git clone https://github.com/tls-attacker/TLS-Attacker.git
git revert --no-commit 9dfb02e7b1e2861b9973c5af86c5529c2f657c8d HEAD
cd TLS-Attacker


echo "[ COMPILLING REPO!!!! ]"
mvn clean install -DskipTests=true

cd ..

exit 0