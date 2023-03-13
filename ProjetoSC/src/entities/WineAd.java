package entities;

import java.io.File;
import java.util.Objects;

import utils.Utils;

public class WineAd {

	private User user;
	private Wine wine;
	private double price;
	private int quantity;

	public WineAd(User user, Wine wine, double price, int quantity) {
		this.user = user;
		this.wine = wine;
		this.price = price;
		this.quantity = quantity;
	}

	public User getUser() {
		return user;
	}

	public Wine getWine() {
		return wine;
	}

	public int getQuantity() {
		return quantity;
	}

	public void adjustQuantityAndPrice(int quantity, double price) {
		String oldLine = this.toString();
		this.quantity = quantity;
		this.price = price;
		String newLine = this.toString();
		Utils.replaceLine(new File("storedFiles\\wineAdsCatalog.txt"), oldLine, newLine);
	}

	public double getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return user.getName() + " " + this.wine.getName() + " " + this.price + " " + this.quantity;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WineAd other = (WineAd) obj;
		return Objects.equals(user, other.user) && Objects.equals(wine, other.wine);
	}

	public void setQuantity(int i) {
		this.quantity=i;
	}

}
