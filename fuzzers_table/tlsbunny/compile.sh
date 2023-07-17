#!/bin/bash

git clone https://github.com/artem-smotrakov/tlsbunny.git

cd tlsbunny

echo "target.port=4433" >> tlsbunny.properties

mvn clean install -DskipTests

cd ..

exit 0