#!/bin/bash

if [[ $# -lt 3 ]]; then
    echo "Use ./test.sh <num of iterations> <path to the desired fuzzer> <command to start the desired fuzzer>"
    exit 1
fi


time_results=()  # Time array.
cpu_use=()       # CPU uses array.
ram_use=()       # RAM uses array.

cd $2

if [[ ! -d tshark ]]; then
  mkdir tshark
fi

if [[ -f results.txt ]]; then
  rm results.txt
fi

echo $3

for ((i=0; i<$1; i++))
do  
    echo $PWD

    eval "$3 &"

    pid=$!              # Gets fuzzer process id.

    ini_time=$(date +%s)

    tshark -i lo -f "tcp port 4433" -w ./tshark/$i.pcapng -F pcapng -q &

    tshark_pid=$!
    disown

    ini_time=$(date +%s)

    # While process is alive.
    while kill -0 $pid 2>/dev/null
    do
      #echo $(top -bn 1 -p $pid | awk '{if ($1 == '$pid') print $10}')
      ram=$(top -bn 1 | grep "MiB Mem" | awk '{print $8}')

      #echo $(top -bn 1 | grep %Cpu\(s\) | cut -d ',' -f 4 | awk '{ gsub("[^0-9.]",""); print }')
      cpu=$(top -bn 1 | grep %Cpu\(s\) | cut -d ',' -f 4 | awk '{ gsub("[^0-9.]",""); print }')


      # If the fuzzer is too fast for the cpu reading, it doesn't count the "zero" percent usage.
      if [[ $cpu != 100.0 ]]; then
        cpu_use+=($(awk "BEGIN { print 100 - $cpu }"))  # % usage of CPU.
        ram_use+=($ram)
      fi

    done

    # Finished Capture
    kill -9 $tshark_pid 2>/dev/null


    ##########################TIME##################################
    end_time=$(date +%s)

    time=$(echo "scale=4; (${end_time} - ${ini_time})/1000" | bc -l)
    
    #echo "$i iteration duration: $time s"

    time_results+=($time)

done


# Time for each iteraction
for print in "${time_results[@]}"
do
  echo -n "$print " >> results.txt
done

echo >> results.txt

# CPU results print for python script.
for print in "${cpu_use[@]}"
do
  echo -n "$print " >> results.txt
done

echo >> results.txt

# RAM results print for python script.
for print in "${ram_use[@]}"
do
  echo -n "$print " >> results.txt
done

echo >> results.txt


cd ..
eval "python3 statistics.py < $2/results.txt"



exit 0
