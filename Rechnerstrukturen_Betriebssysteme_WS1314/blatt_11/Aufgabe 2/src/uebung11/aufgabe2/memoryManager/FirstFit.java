package uebung11.aufgabe2.memoryManager;

import java.util.Arrays;
import java.util.HashMap;

import uebung11.aufgabe2.processManager.Process;

public class FirstFit extends MemoryManager {

	/*
	 * Datenstruktur:
	 * 
	 * free : Wieviel freien Speicher gibt es noch in memory 
	 * memory: Bitmap des RAM, true ==> belegt; false ==> frei
	 * pId : HashMap um über die ID eines Prozesses diese zufinden 
	 * pStart : HasphMap um den Start eines Prozesses in memory zufinden
	 * 
	 */
	
	int free;
	private boolean[] memory;
	private HashMap<Integer, Process> pId = new HashMap<>();
	private HashMap<Process, Integer> pStart = new HashMap<>();

	public FirstFit(int size) {
		super(size);
		free = size;
		memory = new boolean[size];
	}

	@Override
	public boolean allocateMemory(Process process) {
		int pSize = process.getSize();

		// Ist es überhaupt möglich ist einen Chunk zufinden, der groß genug ist
		if (free < pSize)
			return false;

		int i = 0;

		// Findet leeren Speicherchunks und überprüft ob der Prozess reinpasst
		while (i < memory.length) {
			int chunkSize = 0;
			int start = i;

			// Abbruch wenn memory zuende, Chunk zuende oder der Chunk groß
			// genug ist
			while (i < memory.length && !memory[i++] && chunkSize < pSize)
				chunkSize++;

			// Chunk groß genug
			if (chunkSize >= pSize) {
				// Eintragen in die Datenstruktur
				Arrays.fill(memory, start, start + pSize, true);
				pId.put(process.getId(), process);
				pStart.put(process, start);
				free -= pSize;
				return true;
			}
		}

		return false;
	}

	@Override
	public void freeMemory(Process process) {
		int id = process.getId();

		// Existiert der zulöschende Prozess
		if (pId.get(id) != null) {
			int start = pStart.get(process);
			int pSize = process.getSize();

			// Austragen aus der Datenstruktur
			Arrays.fill(memory, start, start + pSize, false);
			pId.remove(id);
			pStart.remove(process);
			free += pSize;
		}
	}

	@Override
	public void printAllProcesses() {
		System.out.println("id\tName\tSize\tstart\tEndPos");
		for (Process p : pId.values()) {
			int s = pStart.get(p);
			System.out.println(p.toString() + s + "\t" + (s + p.getSize() - 1));
		}
	}
}
