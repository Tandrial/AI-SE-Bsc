package de.unidue.iem.tdr.nis.client;

/**
 * Stellt Konstanten für die verschiedenen Aufgabentypen zur Verfügung.
 * 
 */
public interface TaskDefs {
	static final int TASK_CLEARTEXT = 1;
	static final int TASK_XOR = 2;
	static final int TASK_MODULO = 3;
	static final int TASK_FACTORIZATION = 4;
	static final int TASK_VIGENERE = 5;
	static final int TASK_DES_KEYSCHEDULE = 6;
	static final int TASK_DES_RBLOCK = 7;
	static final int TASK_DES_FEISTEL = 8;
	static final int TASK_DES_ROUND = 9;
	static final int TASK_AES_GF8 = 10;
	static final int TASK_AES_KEYEXPANSION = 11;
	static final int TASK_AES_MIXCOLUMNS = 12;
	static final int TASK_AES_TRANSFORMATION = 13;
	static final int TASK_AES_3ROUNDS = 14;
	static final int TASK_RC4_LOOP = 15;
	static final int TASK_RC4_KEYSCHEDULE = 16;
	static final int TASK_RC4_ENCRYPTION = 17;
	static final int TASK_DIFFIEHELLMAN = 18;
	static final int TASK_RSA_ENCRYPTION = 19;
	static final int TASK_RSA_DECRYPTION = 20;
	static final int TASK_ELGAMAL_ENCRYPTION = 21;
	static final int TASK_ELGAMAL_DECRYPTION = 22;
}