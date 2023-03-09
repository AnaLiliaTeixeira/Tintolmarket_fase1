package entities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class WineAd {

	private User user;
	private Wine wine;
	private int quantity;
	private double price;
	
	public WineAd(User user, Wine wine, int quantity, double price) {
		this.user = user;
		this.wine = wine;
		this.quantity = quantity;
		this.price = price;
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

	public void adjustQuantity(int quantity) {
		this.quantity+=quantity;
	}
	
	public double getPrice() {
		return price;
	}
	
	@Override
	public String toString() {
		return  user.getName() + " " + this.wine.getName() + " " + this.quantity + " " + this.price;
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
	

	
}
