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
	
	//TODO
	public String toString;
	
	//TODO
	// Remover vinho da lista de anuncios (pode nao ser preciso)
	public boolean equals(Object o) {
		return false;
		//return this == 0 || o instanceof WineAd && ;
	}
	
}
