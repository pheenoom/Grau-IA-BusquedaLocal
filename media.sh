#!/bin/bash
for f in ./medidas/*
do
	echo $f
	suma=$(paste -s -d + $f | bc)
	linias=$(cat $f | wc -l)

	echo $suma / $linias | bc
done
