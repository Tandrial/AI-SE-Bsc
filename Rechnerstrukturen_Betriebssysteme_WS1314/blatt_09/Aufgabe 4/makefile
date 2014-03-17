CC = cc
COPS = -D_GNU_SOURCE
LIBTIME = -lrt

make: A4.c
	$(CC) $(COPS) -o A4 A4.c $(LIBTIME)

runex1:
	./A4 2000000000 3 ${nice}

runex2:
	./A4 2000000000 2 ${nice}

runex3:
	./A4 2000000000 ${n}

clean:
	rm -rf core *.o A4
