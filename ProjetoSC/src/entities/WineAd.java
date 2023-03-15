package entities;

import java.io.File;
import java.util.Objects;

import utils.Utils;

/**

A classe WineAd representa um anúncio de vinho no sistema. Cada anúncio contém informações sobre o utilizador que o criou, o vinho anunciado, o preço e a quantidade disponível.
*/
public class WineAd {

	private User user;
	private Wine wine;
	private double price;
	private int quantity;

/**

	Construtor da classe WineAd.
	@param user O utilizador que criou o anúncio.
	@param wine O vinho anunciado.
	@param price O preço do vinho.
	@param quantity A quantidade disponível.
*/
	public WineAd(User user, Wine wine, double price, int quantity) {
		this.user = user;
		this.wine = wine;
		this.price = price;
		this.quantity = quantity;
	}

/**

	Obtém o utilizador que criou o anúncio.
	@return O utilizador que criou o anúncio.
*/
	public User getUser() {
		return user;
	}

/**

	Obtém o vinho anunciado.
	@return O vinho anunciado.
*/
	public Wine getWine() {
		return wine;
	}

/**

	Obtém a quantidade disponível do vinho no anúncio.
	@return A quantidade disponível.
*/
	public int getQuantity() {
		return quantity;
	}

/**

	Ajusta a quantidade e o preço do anúncio.
	@param quantity A nova quantidade.
	@param price O novo preço.
*/
	public void adjustQuantityAndPrice(int quantity, double price) {
		String oldLine = this.toString();
		this.quantity += quantity;
		this.price = price;
		String newLine = this.toString();
		Utils.replaceLine(new File("wineAdsCatalog.txt"), oldLine, newLine);
	}

/**

	Obtem o preco do vinho no anuncio.
	@return O preco do vinho.
*/
	public double getPrice() {
		return price;
	}

/**

	Ajusta a quantidade do anuncio.
	@param i A nova quantidade.
*/
	public void setQuantity(int i) {
		this.quantity=i;
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
