#!/bin/bash
for f in ./medidas/*
do
	suma=$(paste -s -d + $f | bc)
	linias=$(cat $f | wc -l)

	echo "$f: $(echo $suma / $linias | bc | cat)"
done
