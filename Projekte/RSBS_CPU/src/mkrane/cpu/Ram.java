/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package mkrane.cpu;

import java.io.*;

public class Ram {

	private short[] data;

	public Ram() {
		this.data = new short[Ctrl.RAM_SIZE];
	}

	public Ram(short[] data) {
		if (data.length <= (Ctrl.RAM_SIZE))
			this.data = data;
	}

	public Ram(File f) {
		data = new short[Ctrl.RAM_SIZE];
		// ;Code 00 ... 0D *
		// @00:2F00; LOAD 0x00

		try {
			BufferedReader br = new BufferedReader(new FileReader(f));

			String line = null;
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("@"))
					continue;

				String[] words = line.split(";")[0].split(":");
				short a = (short) Integer.parseInt(
						words[0].substring(1).trim(), 16);
				short v = (short) Integer.parseInt(words[1].trim(), 16);

				if (a >= 0 && a < data.length)
					data[a] = v;
			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public short readData(byte adr) {
		if (adr < 0)
			return data[adr + data.length];
		else
			return data[adr];
	}

	public void writeData(byte adr, short value) {
		if (adr < 0)
			this.data[adr + data.length] = value;
		else
			this.data[adr] = value;
	}

	public void dumpMemory() {
		for (int i = 0; i < data.length; i++) {
			System.out.printf("0x%02X:0x%04X\n", i, data[i]);
		}
	}
}
