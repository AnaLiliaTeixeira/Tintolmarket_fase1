package entities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import catalogs.WineAdCatalog;

public class Wine {

	private String name;
	private File image;
	private HashMap<String, Integer> classifications;

	public Wine(String name, File image) {
		this.name = name;
		this.image = image;
		this.classifications = new HashMap<>();
		
		try {
			File wineInfo = new File("storedFiles\\wineCatalog.txt");	
			FileWriter fw = new FileWriter(wineInfo, true);
			fw.write(this.toString() + "\r\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public Wine(String name) {
		this.name = name;
		
		File wineInfo = new File("storedFiles\\wineCatalog.txt");
		Scanner sc = null;
		try {
			sc = new Scanner(wineInfo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(sc.hasNextLine()) {
			String[] line = sc.nextLine().split("(?!\\{.*)\\s(?![^{]*?\\})");
			if(line[0].equals(name)) {
				this.image = new File(line[1]);
				this.classifications = stringToHashMap(line[2]);
				break;
			}
		}
		sc.close();
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

	public String printWine() {
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
	
	private HashMap<String, Integer> stringToHashMap(String line) {
		HashMap<String, Integer> result = new HashMap<>();
		line = line.substring(1, line.length() - 1);
		String[] hashContents = line.split(", ");
		if(hashContents[0].contains("=")) {
			for (String s : hashContents) {
				String[] item = s.split("=");
				result.put(item[0], Integer.parseInt(item[1]));
			}
		}
		return result;
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
		return Objects.equals(image, other.image) && Objects.equals(name, other.name);
	}
}
