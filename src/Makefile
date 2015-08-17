CC			= gcc
CFLAGS      = -std=gnu99 -D _DEFAULT_SOURCE -lm -ansi -fPIC -g -O3 -fstack-protector -Wl,-z,relro -Wl,-z,now -W -Wall -Wno-unused-parameter -Wno-unused-function -Wno-unused-label -Wpointer-arith -Wformat -Wreturn-type -Wsign-compare -Wmultichar -Wformat-nonliteral -Winit-self -Wuninitialized -Wno-deprecated -Wformat-security -Werror
OBJECTS		= *.o
SOURCES		= *.c
HEADERS		= *.h
TAR_LIST	= *.c *.h Makefile
ZIP_LIST	= $(TAR_LIST)
PRJ_NAME	= ant

all: $(PRJ_NAME)

$(PRJ_NAME): $(SOURCES) *.h Makefile
	$(CC) $(CFLAGS) $(SOURCES) -o $(PRJ_NAME)

clean:
	rm -f core $(PRJ_NAME) $(OBJECTS) *~ *.tgz *.zip

tarball: $(TAR_LIST)
	rm -f $(PRJ_NAME).tgz $(PRJ_NAME).zip
	tar czvf $(PRJ_NAME).tgz $(TAR_LIST)
	zip $(PRJ_NAME).zip $(ZIP_LIST)

depend:
	makedepend $(SOURCES) $(HEADERS)
