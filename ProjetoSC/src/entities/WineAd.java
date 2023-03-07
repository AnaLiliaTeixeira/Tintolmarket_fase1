package entities;

public class WineAd {

	private Wine wine;
	private int quantity;
	private double price;
	
	public WineAd(Wine wine, int quantity, double price) {
		this.wine = wine;
		this.quantity = quantity;
		this.price = price;
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
}
