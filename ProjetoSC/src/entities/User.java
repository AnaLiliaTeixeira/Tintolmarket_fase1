package entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import catalogs.WineAdCatalog;
import utils.Utils;

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
		String oldLine = this.toString();
		this.balance += value;
		String newLine = this.toString();
		Utils.replaceLine(new File("storedFiles\\userCatalog.txt"), oldLine, newLine);
	}

	/**
	 * @return the inbox
	 */
	public HashMap<String, List<String>> getInbox() {
		return inbox;
	}

	public WineAd createWineAd(Wine wine, double price, int quantity) {
		WineAdCatalog wineAds = WineAdCatalog.getInstance();
		WineAd wa = new WineAd(this, wine, price, quantity);
		for (WineAd wad : wineAds.getWineAdsByUser(this)) {
			if (wad.equals(wa)) {
				wa.setQuantity(wad.getQuantity()+quantity);
				String oldLine = wad.toString();
				String newLine = wa.toString();
				Utils.replaceLine(new File("storedFiles\\wineAdsCatalog.txt"), oldLine, newLine);
				wineAds.remove(wa);
				break;
			}
		}
		wineAds.add(wa);
		return wa;
	}

	public String inboxToString() {
		StringBuilder sb = new StringBuilder();
		for (String key : inbox.keySet()) {
			sb.append(key + ": " + inbox.get(key) + "\r\n");
		}
		return sb.toString();
	}

	public void deleteMessages() {
		String oldLine = this.toString();
		this.inbox.clear();
		String newLine = this.toString();
		Utils.replaceLine(new File("storedFiles\\userCatalog.txt"), oldLine, newLine);
	}

	public void addMessage(User sender, String message) {
		String oldLine = this.toString();
		List<String> senderMessages = this.inbox.getOrDefault(sender.getName(), new ArrayList<>());
		senderMessages.add(message);
		this.inbox.put(sender.getName(), senderMessages);
		String newLine = this.toString();
		Utils.replaceLine(new File("storedFiles\\userCatalog.txt"), oldLine, newLine);
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
