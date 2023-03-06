package entities;

import java.io.File;
import java.util.HashMap;

public class Wine {

	private String name;
	private File image;
	private HashMap<String, Integer> classifications;

	public Wine(String name, File image) {
		this.name = name;
		this.image = image;
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
	 * @return the classifications
	 */
	public HashMap<String, Integer> getClassifications() {
		return classifications;
	}

	/**
	 * Adds a new classification to the current wine
	 * 
	 * @param user  - the author of the classification
	 * @param stars - the numerical value
	 */
	public void addClassification(User user, Integer stars) {
		classifications.put(user.getName(), stars);
	}
}
