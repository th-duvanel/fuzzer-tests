#!/bin/bash

if [[ $# -lt 3 ]]; then
    echo "Use ./test.sh <num of iterations> "<path to tshark>" "<command to start the desired fuzzer>""
    exit 1
fi

eval "./delivery.sh $1 $2 $3 > statistics.py"