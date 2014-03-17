CC = cc
COPS = -D_GNU_SOURCE

make: mytee.c
	$(CC) $(COPS) -o mytee mytee.c 

run: mytee mytee.c
	cat mytee.c | ./mytee test.txt

show: 
	cat mytee.log

clean:
	rm mytee{,.log} *.txt
