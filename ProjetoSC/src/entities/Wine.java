package entities;

import java.io.File;
import java.util.HashMap;

public class Wine {

	private String name;
	private File image;
	private double price;
	private int quantityForSale;
	private HashMap<String, Byte> classifications;

	public Wine(String name, File image) {
		this.name = name;
		this.image = image;
		this.quantityForSale = 0;
		this.classifications = new HashMap<>();
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
	 * @return the image
	 */
	public File getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(File image) {
		this.image = image;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the classifications
	 */
	public HashMap<String, Byte> getClassifications() {
		return classifications;
	}

	/**
	 * Adds a new classification to the current wine
	 * 
	 * @param user  - the author of the classification
	 * @param stars - the numerical value
	 */
	public void addClassification(User user, Byte stars) {
		classifications.put(user.getName(), stars);
	}

	/**
	 * @return the quantityForSale
	 */
	public int getQuantityForSale() {
		return quantityForSale;
	}

	/**
	 * @param quantityForSale the quantityForSale to set
	 */
	public void setQuantityForSale(int quantityForSale) {
		this.quantityForSale = quantityForSale;
	}

}
