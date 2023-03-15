package entities;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import catalogs.WineAdCatalog;
import utils.Utils;

public class Wine {

	private String name;
	private File image;
	private HashMap<String, Integer> classifications;

	public Wine(String name, File image, HashMap<String, Integer> classifications) {
		this.name = name;
		this.image = image;
		this.classifications = classifications;
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
		String oldLine = this.toString();
		this.classifications.put(user.getName(), stars);
		String newLine = this.toString();
		File wineInfo = new File("wineCatalog.txt");
		Utils.replaceLine(wineInfo, oldLine, newLine);
	}

	public String printWine() {
		StringBuilder sb = new StringBuilder();
		sb.append("Nome do Vinho: " + name + " - " + image.getName() + "\n");
		double avg = 0.0;
		for (int i : classifications.values()) {
			avg += i;
		}
		if(avg != 0) {
			avg /= classifications.size();
		}
			
		sb.append("Media das classificacoes: " + avg + "\n");
		sb.append("Informacoes de venda do vinho: \n");

		if(getCurrentAds().isEmpty()) {
			sb.append("N/A");
		}
		else {
			for (WineAd ad : getCurrentAds())
				sb.append("\t" + ad + "\n");
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return this.name + " " + this.image.getName() + " " + this.classifications;
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
		return Objects.equals(name, other.name);
	}
}
