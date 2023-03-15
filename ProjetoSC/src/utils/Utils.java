package utils;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Utils {

	public static void replaceLine(File file, String oldLine, String newLine) {
		try {
			File newFile = new File("temp.txt");
			newFile.createNewFile();
			FileWriter fw = new FileWriter(newFile, true);
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String next = sc.nextLine();
				if (next.equals(oldLine)) {
					if(newLine != null)
						fw.append(newLine + "\n");
				} else
					fw.append(next + "\n");
			}
			fw.close();
			sc.close();
			file.delete();
			newFile.renameTo(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
