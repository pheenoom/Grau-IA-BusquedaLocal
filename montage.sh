max=$(ls | wc -l)
ls > files.txt
for i in $(seq 1 6 $max)
do
	echo "Mas"
	fics=$(tail -n +$i files.txt | head -6 | paste -s)
	montage $fics -geometry 1024x1024+2+2 montage$i.png
done
$i = 
