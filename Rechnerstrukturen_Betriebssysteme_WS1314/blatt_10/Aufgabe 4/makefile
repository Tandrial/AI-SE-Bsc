CC = cc
COPS = -D_GNU_SOURCE
LIB = -lpthread

make: makea makeb

tests: testa testb

makea: flock.o testa.c
	$(CC) $(COPS) -o testa testa.c flock.o $(LIB)
	
makeb: flock.o testb.c
	$(CC) $(COPS) -o testb testb.c flock.o $(LIB)

lock: flock.c
	$(CC) $(COPS) -c flock.c	

testa: makea
	./testa

testb: makeb
	./testb

clean:
	rm -rf *o lock testa testb test.txt
