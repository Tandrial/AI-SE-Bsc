package uebung11.aufgabe2.processManager;

/**
 * This class represents a very simple wrapper for a process. It does not necessarily reflect how a process is
 * handled in real systems
 * 
 */
public class Process {
	private ProcessManager processManager;
	private String name;
	private int size;
	private int id;


	Process(String name, int size, int id, ProcessManager processManager) {
		this.name = name;
		this.size = size;
		this.id = id;
		this.processManager = processManager;
	}

	/**
	 * Function to kill a process. A killed process will be removed from memory
	 * 
	 * @return true if the process was alive and could be killed
	 */
	public boolean kill() {
		boolean result = false;
		if (-1 != this.id) {
			processManager.removeProcess(this);
			this.name = "invalid";
			this.size = 0;
			this.id = -1;
			result = true;
		} else {
			System.out.println("Warning: invalid process id, no process was removed");
		}
		return result;
	}

	public String toString() {
		String result;
		result = "" + id + "\t" + name + "\t" + size + "\t";
		return result;
	}

	/**
	 * returns the Process id
	 * 
	 * @return process id
	 */
	public int getId() {
		return id;
	}

	/**
	 * returns the name of the process
	 * 
	 * @return the name of the process
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns the memory required by this process
	 * 
	 * @return memory required
	 */
	public int getSize() {
		return size;
	}
}
