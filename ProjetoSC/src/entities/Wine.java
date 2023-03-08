package entities;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import catalogs.WineAdCatalog;

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
	
	public List<WineAd> getCurrentAds() {
		WineAdCatalog wineAds = WineAdCatalog.getInstance();
		return wineAds.getWineAdsByWine(this);
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

	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Nome do Vinho: " + name + " - " + image.getName() + "\n");
		double avg = 0.0;
		for (int i : classifications.values()) {
			avg += i;
		}
		avg /= classifications.size();
		
		sb.append("Media das classificacoes: " + avg + "\n");
		sb.append("Informacoes de venda do vinho: ");
		
		for (WineAd ad : getCurrentAds()) {
			sb.append(ad + "\n");
		}
		
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Wine)) {
			return false;
		}
		Wine other = (Wine) obj;
		return Objects.equals(image, other.image) && Objects.equals(name, other.name);
	}
}
