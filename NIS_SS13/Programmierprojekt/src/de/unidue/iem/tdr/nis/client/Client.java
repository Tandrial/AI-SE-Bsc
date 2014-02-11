package de.unidue.iem.tdr.nis.client;

/**Package mit allen selbst geschriebenen Funktionen**/
import de.unidue.mkrane.crypto.*;

/**
 * Diese Klasse ermöglicht das Abrufen von Aufgaben vom Server und die
 * Implementierung der dazugehörigen Lösungen.
 * <p>
 * Nähere Informationen zu den anderen Klassen und den einzelnen Aufgabentypen
 * entnehmen Sie bitte der entsprechenden Dokumentation im TMT und den Javadocs
 * zu den anderen Klassen.
 * 
 * @see Connection
 * @see TaskObject
 * 
 */
public class Client implements TaskDefs {
	private Connection con;
	private TaskObject currentTask;

	/* hier bitte die Matrikelnummer eintragen */
	private final int matrikelnr = 0;

	/* hier bitte das TMT-Passwort eintragen */
	private final String password = "";

	/* Aufgaben, die bearbeitet werden sollen */
	private final int[] tasks = { TASK_CLEARTEXT, TASK_XOR, TASK_MODULO,
			TASK_FACTORIZATION, TASK_VIGENERE, TASK_DES_KEYSCHEDULE,
			TASK_DES_RBLOCK, TASK_DES_FEISTEL, TASK_DES_ROUND, TASK_AES_GF8,
			TASK_AES_KEYEXPANSION, TASK_AES_MIXCOLUMNS,
			TASK_AES_TRANSFORMATION, TASK_AES_3ROUNDS, TASK_RC4_LOOP,
			TASK_RC4_KEYSCHEDULE, TASK_RC4_ENCRYPTION, TASK_DIFFIEHELLMAN,
			TASK_RSA_ENCRYPTION, TASK_RSA_DECRYPTION, TASK_ELGAMAL_ENCRYPTION,
			TASK_ELGAMAL_DECRYPTION };

	/**
	 * Klassenkonstruktor. Baut die Verbindung zum Server auf.
	 */
	public Client() {
		con = new Connection();
		if (con.auth(matrikelnr, password))
			System.out.println("Anmeldung erfolgreich.");
		else
			System.out.println("Anmeldung nicht erfolgreich.");
	}

	/**
	 * Besteht die Verbindung zum Server?
	 * 
	 * @return true, falls Verbindung bereit, andernfalls false
	 */
	public boolean isReady() {
		return con.isReady();
	}

	/**
	 * Beendet die Verbindungs zum Server.
	 */
	public void close() {
		con.close();
	}

	/**
	 * Durchläuft eine Liste von Aufgaben und fordert diese vom Server an.
	 */
	public void taskLoop() {
		String solution = "";
		String[] params;
		System.out.println("---------------");
		for (int i = 0; i < tasks.length; i++) {
			switch (tasks[i]) {
			case TASK_CLEARTEXT:
				currentTask = con.getTask(tasks[i]);
				solution = currentTask.getStringArray(0);
				break;

			case TASK_XOR:
				currentTask = con.getTask(tasks[i]);				
				solution = Aufgabe02.getSolution(currentTask);
				break;

			case TASK_MODULO:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe03.getSolution(currentTask);
				break;

			case TASK_FACTORIZATION:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe04.getSolution(currentTask);
				break;

			case TASK_VIGENERE:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe05.getSolution(currentTask);
				break;

			case TASK_DES_KEYSCHEDULE:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe06.getSolution(currentTask);
				break;

			case TASK_DES_RBLOCK:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe07.getSolution(currentTask);
				break;

			case TASK_DES_FEISTEL:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe08.getSolution(currentTask);
				break;

			case TASK_DES_ROUND:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe09.getSolution(currentTask);
				break;

			case TASK_AES_GF8:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe10.getSolution(currentTask);
				break;

			case TASK_AES_KEYEXPANSION:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe11.getSolution(currentTask);
				break;

			case TASK_AES_MIXCOLUMNS:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe12.getSolution(currentTask);
				break;

			case TASK_AES_TRANSFORMATION:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe13.getSolution(currentTask);
				break;

			case TASK_AES_3ROUNDS:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe14.getSolution(currentTask);
				break;

			case TASK_RC4_LOOP:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe15.getSolution(currentTask);
				break;

			case TASK_RC4_KEYSCHEDULE:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe16.getSolution(currentTask);
				break;

			case TASK_RC4_ENCRYPTION:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe17.getSolution(currentTask);
				break;

			case TASK_DIFFIEHELLMAN:
				currentTask = con.getTask(tasks[i]);
				params = Aufgabe18.getParams(currentTask);
				con.sendMoreParams(currentTask, Aufgabe18.sendParams(params));
				solution = Aufgabe18.getSolution(currentTask, params);
				break;

			case TASK_RSA_ENCRYPTION:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe19.getSolution(currentTask);
				break;

			case TASK_RSA_DECRYPTION:
				params = Aufgabe20.calcParams();
				currentTask = con.getTask(tasks[i],
						Aufgabe20.sendParams(params));
				solution = Aufgabe20.getSolution(currentTask, params);
				break;

			case TASK_ELGAMAL_ENCRYPTION:
				currentTask = con.getTask(tasks[i]);
				solution = Aufgabe21.getSolution(currentTask);
				break;

			case TASK_ELGAMAL_DECRYPTION:
				params = Aufgabe22.calcParams();
				currentTask = con.getTask(tasks[i],
						Aufgabe22.sendParams(params));
				solution = Aufgabe22.getSolution(currentTask, params);
				break;

			default:
				currentTask = con.getTask(tasks[i]);
				solution = "Nicht implementiert!";
			}

			if (con.sendSolution(solution))
				System.out.println("Aufgabe " + tasks[i] + ": Lösung korrekt ");
			else {
				System.out.println("Aufgabe " + tasks[i] + ": Lösung falsch ");
				System.out.println("Lösung: " + solution);
				currentTask.printTO();
				System.out.println("---------------");
			}
		}
	}

	public static void main(String[] args) {
		Client c = new Client();
		if (c.isReady()) {
			c.taskLoop();
		}
		c.close();
	}
}
