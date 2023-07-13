#!/bin/bash

git clone https://github.com/artem-smotrakov/tlsbunny.git

cd tlsbunny

mvn clean install -DskipTests

cd ..

exit 0