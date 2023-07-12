#!/bin/bash

if [[ $# -lt 2 ]]; then
    echo "Use ./test.sh <num of iterations> "<command to start the desired fuzzer>.""
    exit -1
fi

iterations=$1

total_ram=$(top -bn 1 | grep "MiB Mem" | awk '{print $4}')

time_results=()      # Time array.
cpu_results=()       # CPU uses array.
ram_results=()       # RAM uses array.

for ((i=0; i<iterations; i++))
do
    cpu_use=()
    ram_use=()

    eval "$2 &"

    pid=$!              # Gets fuzzer process id.

    ini_time=$(date +%s)

    # While process is alive.
    while kill -0 $pid 2>/dev/null
    do

      cpu=$(top -bn 1 | grep %Cpu\(s\) | cut -d ',' -f 4 | awk '{ gsub("[^0-9.]",""); print }')

      ram=$(top -bn 1 -p $pid | awk '{if ($1 == '$pid') print $10}')

      cpu_use+=$(awk "BEGIN { print 100 - $cpu }")  # % usage of CPU.
      
      ram_uses+=$((ram * total_ram))    # MiB usage of RAM.

    done


    ##########################TIME##################################
    end_time=$(date +%s)

    time=$(echo "(${end_time} - ${ini_time})/1000" | bc -l)
    
    #echo "$i iteration duration: $time s"

    time_results+=($time)


    ##########################CPU#####################################
    sum=0
    for result in "${cpu_use[@]}"
    do
      sum=$(echo "$sum + $result" | bc -l)
    done
    avg_cpu=$(echo "scale=4; $sum / ${#cpu_use[@]}" | bc -l)

    #echo "$i iteration CPU usage: $time s"

    cpu_results+=$(avg_cpu)


    ##########################RAM######################################
    sum=0
    for result in "${ram_use[@]}"
    do
      sum=$(echo "$sum + $result" | bc -l)
    done
    avg_ram=$(echo "scale=4; $sum / ${#ram_results[@]}" | bc -l)

    #echo "$i iteration RAM usage: $time s"

    ram_results+=$(avg_ram)

done

sum=0
for time in "${time_results[@]}"
do
  sum=$(echo "$sum + $tine" | bc -l)
done
avg_time=$(echo "scale=4; $sum / ${#time_results[@]}" | bc -l)


# CPU results print for python script.
for print in "${cpu_results[@]}"
do
  echo "$print "
done

# RAM results print for python script.
for print in "${ram_results[@]}"
do
  echo "$print "
done

echo "Average total duration of fuzzer: $avg_time"


exit 0