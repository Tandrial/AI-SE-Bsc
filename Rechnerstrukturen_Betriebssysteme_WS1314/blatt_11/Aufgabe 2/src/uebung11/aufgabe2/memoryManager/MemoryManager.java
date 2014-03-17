package uebung11.aufgabe2.memoryManager;

import uebung11.aufgabe2.processManager.Process;

/**
 * This abstract class represents a memory management strategy.
 */
public abstract class MemoryManager {
	private int size;

	/**
	 * initializes the memory manager. The size allows to specify the amount of memory available in the system
	 * 
	 * @param size
	 */
	public MemoryManager(int size) {
		this.size = size;
	}

	/**
	 * This function reserves some memory for a process. For simplicity, in this case a process receives a continuous
	 * block of memory
	 * 
	 * @param process
	 * @return true if sufficient memory could be reserved, false otherwise
	 */
	public abstract boolean allocateMemory(Process process);

	/**
	 * This function frees some memory associated to a process
	 * @param process
	 */
	public abstract void freeMemory(Process process);

	/**
	 * This function prints out all reserved memory in the following format:
	 * 
	 * Id 	Name 	Size 	start 	EndPos
	 * 0	p1		100		0		100
	 * 1	p2		500		100		600
	 * .....
	 */
	public abstract void printAllProcesses();

	/**
	 * returns the size of memory managed
	 * @return size of memory managed
	 */
	public int getMemorySize() {
		return size;
	}
}
