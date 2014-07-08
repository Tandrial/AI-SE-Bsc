/*
 * Copyright © 2014 Michael Krane <Michael.Krane@stud.uni-due.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package ram;

import java.io.*;
import dataTypes.Types;

public class Ram {

	private short[] data;

	public Ram(File f) {
		data = new short[Types.RAM_SIZE];
		// ;Code 00 ... 0D *
		// @00:2F00; LOAD 0x00

		try {
			BufferedReader br = new BufferedReader(new FileReader(f));

			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith(";"))
					continue;

				String[] words = line.split(";")[0].split(":");
				short a = (short) Integer.parseInt(words[0].substring(1), 16);
				short v = (short) Integer.parseInt(words[1], 16);
				data[a] = v;
			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Ram(short[] data) {
		if (data.length <= (Types.RAM_SIZE))
			this.data = data;
	}

	public short readData(byte adr) {
		return data[adr];
	}

	public void writeData(byte data, short adr) {
		this.data[adr] = data;
	}

	public void dumpMemory() {
		for (int i = 0; i < data.length; i++) {
			System.out.printf("0x%02X:0x%04X\n", i, data[i]);
		}
	}
}
