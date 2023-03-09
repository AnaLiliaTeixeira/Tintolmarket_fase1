package entities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import catalogs.WineAdCatalog;

public class User {

	private String name;
	private double balance;
	private HashMap<String, List<String>> inbox;

	public User(String name, double balance, HashMap<String, List<String>> inbox) throws IOException {
		this.name = name;
		this.balance = balance;
		this.inbox = inbox;
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
	
	@Override
	public String toString() {
		return this.name + " " + this.balance + " " + this.inbox;
	}

}
