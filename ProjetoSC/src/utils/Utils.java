package utils;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**

	Classe Utils que fornece métodos úteis para manipular arquivos e outros auxiliares.
*/
public class Utils {

/**

	Substitui uma linha específica em um arquivo por outra.
 	Se a nova linha for nula, a linha antiga será removida.
	@param file    O arquivo onde a linha será substituída.
	@param oldLine A linha antiga que será substituída ou removida.
	@param newLine A nova linha que substituirá a antiga. Se for nulo, a linha antiga será removida.
*/
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
