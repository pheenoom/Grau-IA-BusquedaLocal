#!/bin/bash
for fichero in medidas/*
do
	temp=$(basename $fichero)
	fout=$temp"_grafica.png"
	texto="set term png \n set output \"pngs/$fout\" \n plot '$fichero' with lines "
	echo $texto > plots/$fout".plot"
done

for fplot in plots/*
do
	gnuplot $fplot
done
