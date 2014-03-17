package uebung11.aufgabe2.processManager;

import java.util.HashSet;
import java.util.Set;

import uebung11.aufgabe2.memoryManager.MemoryManager;

/**
 * This class represents a very basic implementation for processes-manager. It is not intended to reflect the management
 * capabilities of a real system.
 * 
 */
public class ProcessManager {

	private Set<Integer> pids = new HashSet<Integer>();
	private MemoryManager mmu;

	public ProcessManager(MemoryManager mmu) {
		this.mmu = mmu;
	}

	/**
	 * This method creates a new process in the system. It will automatically request the memory manager for sufficient
	 * memory
	 * 
	 * @param name
	 *            the name of the new process
	 * @param size
	 *            the size of the new process
	 * @return true if there was sufficient memory left for this process, false otherwise
	 */
	public Process createProcess(String name, int size) {
		Process result = null;
		int pid = getNewProcessId();
		Process newProcess = new Process(name, size, pid, this);
		if (true == mmu.allocateMemory(newProcess)) {
			result = newProcess;
		} else {
			System.out.println("Warning: Short of Memory, new process \"" + name + "\" was not generated");
			newProcess.kill();
		}
		return result;
	}


	void removeProcess(Process process) {
		mmu.freeMemory(process);
		pids.remove(process.getId());
	}

	private int getNewProcessId() {
		int result = -1;
		for (int i = 0; i < 1000; i++) {
			if (false == pids.contains(i)) {
				pids.add(i);
				result = i;
				break;
			}
		}
		return result;
	}
}
