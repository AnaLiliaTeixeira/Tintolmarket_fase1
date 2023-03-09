package handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import catalogs.UserCatalog;
import catalogs.WineCatalog;
import entities.User;

public class AddInfoHandler {

	// add
	public static boolean add(String wineName, File image) {
		return WineCatalog.getInstance().createWine(wineName, image);
	}

	// classify

	// talk
	public static boolean talk(User sender, String recipient, String message) throws IOException {
		File f = new File(recipient + ".txt");
		if (!f.exists()) // verificar se destinatario existe
			return false;
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String[] contents = { br.readLine(), br.readLine(), br.readLine() }; // conteudo do destinatatio

		HashMap<String, List<String>> messages = UserCatalog.getInstance().stringToHashMap(contents[1]);
		List<String> myMessages = messages.getOrDefault(sender.getName(), new ArrayList<String>());
		myMessages.add(message);
		messages.put(sender.getName(), myMessages);
		contents[1] = messages.toString();

		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.flush(); // apagar conteudo do ficheiro
		for (String s : contents) // e reescrever com mensagem nova
			bw.write(s);

		br.close();
		fr.close();
		fw.close();
		bw.close();
		return true;
	}

}