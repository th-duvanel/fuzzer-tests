#!/bin/bash

if [ $# -eq 0 ]; then
    echo "Nenhum argumento foi fornecido."
    exit -1
fi


iteracoes=30

resultados_cpu=()
resultados_ram=()
resultados_tempo=()

for ((i=0; i<iteracoes; i++))
do
    eval "$1 &"

    pid=$!

    uso_cpu=()
    uso_ram=()

    tempo_inicial=$(date +%s)
    while kill -0 $pid 2>/dev/null
    do
      echo $(top -bn 1 | grep %Cpu\(s\) | cut -d ',' -f 4 | awk '{ gsub("[^0-9.]",""); print }')
      uso=$(top -bn 1 | grep %Cpu\(s\) | cut -d ',' -f 4 | awk '{ gsub("[^0-9.]",""); print }')

      echo $(top -bn 1 -p $pid | awk '{if ($1 == '$pid') print $10}')
      ram=$(top -bn 1 -p $pid | awk '{if ($1 == '$pid') print $10}')

      resultados_cpu+=($uso)
      resultados_ram+=($ram)

    done

    tempo_final=$(date +%s)
    tempo_decorrido=$(echo "(${tempo_final} - ${tempo_inicial})/1000" | bc -l)
    echo $tempo_decorrido
    resultados_tempo+=($tempo_decorrido)

done

soma=0
for resultado in "${resultados_cpu[@]}"
do
  soma=$(echo "$soma + $resultado" | bc -l)
done
media_cpu=$(echo "scale=2; $soma / ${#resultados_cpu[@]}" | bc -l)

soma=0
for resultado in "${resultados_ram[@]}"
do
  soma=$(echo "$soma + $resultado" | bc -l)
done
media_ram=$(echo "scale=2; $soma / ${#resultados_ram[@]}" | bc -l)

soma=0
for resultado in "${resultados_tempo[@]}"
do
  soma=$(echo "$soma + $resultado" | bc -l)
done
echo "Soma: $soma"
media_tempo=$(echo "scale=5; $soma / ${#resultados_tempo[@]}" | bc -l)


echo "Idle médio de CPU: $media_cpu%"
echo "Uso médio de RAM: $media_ram%"

echo "Tempo decorrido: $media_tempo"
exit 0