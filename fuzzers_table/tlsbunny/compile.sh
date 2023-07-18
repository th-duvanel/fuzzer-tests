#!/bin/bash

git clone https://github.com/artem-smotrakov/tlsbunny.git
git revert --no-commit 27f8509403ac0f4767a869812d99688521e01fc1 HEAD

cd tlsbunny

echo "target.port=4433" >> tlsbunny.properties

mvn clean install -DskipTests

cd ..

exit 0