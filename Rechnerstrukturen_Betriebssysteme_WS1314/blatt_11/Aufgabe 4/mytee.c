#include <stdio.h>
#include <time.h>

#define BUFFER_SIZE 20
char *LOG_FILE = "mytee.log";

int main(int argc, char *argv[]) {
	/* fpDest: Filehandler für die Zieldatei*/
	FILE *fpDest;
	char bufDest[BUFFER_SIZE];

	/* fpLog: Filehandler für die Logdatei*/
	FILE *fpLog;
	char bufLog[BUFFER_SIZE];

	/* Datumsvariablen */
	time_t rawtime;
	struct tm *info;

	/* Errorcheck falls keine Zieldatei vorhanden */
	if (argc < 2) {
		fprintf(stderr, "Usage: %s [filename.txt]\n", argv[0]);
		return 1;
	}

	/* Zieldatei wird immer überschrieben */	
	if ((fpDest = fopen(argv[1], "w")) == NULL) {
		printf("Error trying to open %s\n", argv[1]);
		return 1;
	}
	/* An das Logfile wird nur angehängt */	;
	if ((fpLog = fopen(LOG_FILE, "a")) == NULL) {
		printf("Error trying to open %s\n", LOG_FILE);
		return 1;
	}

	/* Input wird verarbeitet */
	while (1) {
		/* Liest 20 chars ein */
		size_t bytes = fread(bufDest, sizeof(char), BUFFER_SIZE, stdin);
		
		/* Schreibe die gelesenen Chars in die Zieldatei und stdout */
		fwrite(bufDest, sizeof(char), bytes, fpDest);
		fwrite(bufDest, sizeof(char), bytes, stdout);

		// Abbruchbedingung EOF in der stdin gefunden
		if (feof(stdin))
			break;
	}

	/* Speichert die aktuelle Zeit + Datum in rawtime */
	time(&rawtime);

	/* Formatiert Zeit und Datum */
	info = localtime(&rawtime);
	strftime(bufLog, BUFFER_SIZE, "%d.%m.%Y %X", info);

	/* Schreibt Datum + Zieldatei in das Log */
	fprintf(fpLog, "%s %s\n", bufLog, argv[1]);

	/* Filehandler schließen */
	fclose(fpDest);
	fclose(fpLog);

	return 0;
}
