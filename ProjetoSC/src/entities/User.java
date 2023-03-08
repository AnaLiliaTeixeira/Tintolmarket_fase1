package entities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import catalogs.WineAdCatalog;

public class User {

	private String name;
	private double balance;
	private HashMap<String, List<String>> inbox;

	public User(String name) throws IOException {
		this.name = name;
		File userInfo = new File("storedFiles\\" + name + ".txt");
		userInfo.createNewFile();
		
		if (!userInfo.exists()) { // se user nao existe
			userInfo.createNewFile();
			FileWriter fw = new FileWriter(userInfo);
			BufferedWriter bw = new BufferedWriter(fw);
			this.balance = 200;
			bw.append(balance + "\r\n");
			this.inbox = new HashMap<>();
			bw.close();
			fw.close();
		} else { // se user ja existe
			FileReader fr = new FileReader(userInfo);
			BufferedReader br = new BufferedReader(fr);
			this.balance = Double.parseDouble(br.readLine()); // obter saldo a partir do ficheiro

			String inboxString = br.readLine();
			if (inboxString != null)
				this.inbox = stringToHashMap(inboxString); // obter inbox a partir do ficheiro
			else
				this.inbox = new HashMap<>();

			// TODO
			// falta lista de vinhos

			fr.close();
			br.close();
		}

	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return this.balance;
	}

	public void adjustBalance(double value) {
		this.balance += value;
	}

	/**
	 * @return the inbox
	 */
	public HashMap<String, List<String>> getInbox() {
		return inbox;
	}

	public void createWineAd(Wine wine, int quantity, double price) {
		WineAdCatalog wineAds = WineAdCatalog.getInstance();
		wineAds.add(new WineAd(this, wine, quantity, price));
	}

	public boolean talk(String recipient, String message) throws IOException {
		File f = new File(recipient + ".txt");
		if (!f.exists()) // verificar se destinatario existe
			return false;
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String[] contents = { br.readLine(), br.readLine(), br.readLine() }; // conteudo do destinatatio

		HashMap<String, List<String>> messages = stringToHashMap(contents[1]); // hashmap das mensagens de destino
		List<String> myMessages = messages.getOrDefault(this.name, new ArrayList<String>());
		myMessages.add(message);
		messages.put(this.name, myMessages);
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

	public String read() throws Exception {
		File userInfo = new File(name + ".txt");
		FileReader fr = new FileReader(userInfo);
		BufferedReader br = new BufferedReader(fr);
		String[] contents = { br.readLine(), br.readLine(), br.readLine() };

		FileWriter fw = new FileWriter(userInfo);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.flush(); // apagar conteudo do ficheiro

		// apagar mensagens j� lidas
		bw.write(contents[0] + "\r\n" + new HashMap<>().toString() + "\r\n" + contents[2]);

		fr.close();
		br.close();
		fw.close();
		bw.close();
		return contents[1]; // retornar hashmap em string
	}

	private HashMap<String, List<String>> stringToHashMap(String line) {
		HashMap<String, List<String>> result = new HashMap<>();
		line = line.substring(1, line.length() - 1);
		String[] hashContents = line.split(", ");
		for (String s : hashContents) {
			String[] item = s.split("=");
			item[1] = item[1].substring(1, item[1].length() - 1);
			List<String> value = Arrays.asList(item[1].split("\"|, \""));
			result.put(item[0], value);
		}
		return result;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		return Objects.equals(name, other.name);
	}

}
