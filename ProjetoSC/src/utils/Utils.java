package utils;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Utils {

	public static void replaceLine(File file, String oldLine, String newLine) {
		try {
			File newFile = new File("storedFiles//temp.txt");
			FileWriter fw = new FileWriter(newFile, true);
			newFile.createNewFile();
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String next = sc.nextLine();
				if (next.equals(oldLine))
					fw.append(newLine);
				else
					fw.append(next);
			}
			newFile.renameTo(file);
			fw.close();
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
