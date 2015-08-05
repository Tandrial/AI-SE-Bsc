CC			= gcc
CFLAGS      = -pipe -std=gnu99 -D _DEFAULT_SOURCE -m64 -ansi -fPIC -g -O3 -fstack-protector -Wl,-z,relro -Wl,-z,now -fvisibility=hidden -W -Wall -Wno-unused-parameter -Wno-unused-function -Wno-unused-label -Wpointer-arith -Wformat -Wreturn-type -Wsign-compare -Wmultichar -Wformat-nonliteral -Winit-self -Wuninitialized -Wno-deprecated -Wformat-security -Werror
OBJECTS		= *.o
SOURCES		= *.c
HEADERS		= *.h
TAR_LIST	= *.c *.h Makefile
ZIP_LIST	= $(TAR_LIST)
PRJ_NAME	= ant
AUTHOR		= SMEETS

all: $(PRJ_NAME)

$(PRJ_NAME): $(SOURCES) *.h Makefile
	$(CC) $(CFLAGS) $(SOURCES) -o $(PRJ_NAME)

clean:
	rm -f core $(PRJ_NAME) $(OBJECTS) *~ *.tgz *.zip

pretty: *.c *.h Makefile
	./psnup-pretty-print

tarball: $(TAR_LIST)
	rm -f $(PRJ_NAME).tgz $(PRJ_NAME).zip
	tar czvf $(PRJ_NAME).tgz $(TAR_LIST)
	zip $(PRJ_NAME).zip $(ZIP_LIST)
#	zip -D $(REPORT_NAME)_$(AUTHOR).zip $(DISTRO_LIST)

depend:
	makedepend $(SOURCES) $(HEADERS)
