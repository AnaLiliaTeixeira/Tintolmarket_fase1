package entities;

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
	
	//TODO
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\tVendedor: " + user.getName() + "\n");
		//sb.append("Nome do Vinho: " + wine.getName() + " - " + wine.getImage().getName() + "\n");
		sb.append("\tPreço do Vinho: " + this.price + "\n");
		sb.append("\tQuantidade disponível: " + this.quantity + "\n");
		return sb.toString();
	}
	
	//TODO
	// Remover vinho da lista de anuncios (pode nao ser preciso)
	public boolean equals(Object o) {
		return false;
		//return this == 0 || o instanceof WineAd && ;
	}
	
}
