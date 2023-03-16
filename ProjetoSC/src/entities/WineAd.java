package entities;

import java.io.File;
import java.util.Objects;

import utils.Utils;

/**
 * A classe WineAd representa um anuncio de vinho no sistema. Cada anuncio
 * contem informacoes sobre o utilizador que o criou, o vinho anunciado, o preco
 * e a quantidade disponivel.
 */
public class WineAd {

	private User user;
	private Wine wine;
	private double price;
	private int quantity;

	/**
	 * Construtor da classe WineAd.
	 * 
	 * @param user     O utilizador que criou o anuncio.
	 * @param wine     O vinho anunciado.
	 * @param price    O preco do vinho.
	 * @param quantity A quantidade disponivel.
	 */
	public WineAd(User user, Wine wine, double price, int quantity) {
		this.user = user;
		this.wine = wine;
		this.price = price;
		this.quantity = quantity;
	}

	/**
	 * Obtem o utilizador que criou o anuncio.
	 * 
	 * @return O utilizador que criou o anuncio.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Obtem o vinho anunciado.
	 * 
	 * @return O vinho anunciado.
	 */
	public Wine getWine() {
		return wine;
	}

	/**
	 * Obtem a quantidade disponivel do vinho no anuncio.
	 * 
	 * @return A quantidade disponivel.
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Ajusta a quantidade e o preco do anuncio.
	 * 
	 * @param quantity A nova quantidade.
	 * @param price    O novo preco.
	 */
	public void adjustQuantityAndPrice(int quantity, double price) {
		String oldLine = this.toString();
		this.quantity += quantity;
		this.price = price;
		String newLine = this.toString();
		Utils.replaceLine(new File("wineAdsCatalog.txt"), oldLine, newLine);
	}

	/**
	 * Obtem o preco do vinho no anuncio.
	 * 
	 * @return O preco do vinho.
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Ajusta a quantidade do anuncio.
	 * 
	 * @param i A nova quantidade.
	 */
	public void setQuantity(int i) {
		this.quantity = i;
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
}
