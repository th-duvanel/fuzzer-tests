#!/bin/bash

apt-get install unzip
wget www.github.com/nezha-dt/nezha/archive/refs/tags/v0.1.zip > /dev/null
unzip v0.1.zip > /dev/null
cd nezha-0.1
OUTPUT_MESSAGE=$(./utils/build_helpers/setup.sh)
echo " "
echo $OUTPUT_MESSAGE
CHECK_ERROR=$(echo $OUTPUT_MESSAGE | grep "Did not find clang-3.8")
echo $CHECK_ERROR

if [[ -z $CHECK_ERROR ]]; then
    echo "WOW! You didn't got our errors!!"
else
    echo "OK"
fi