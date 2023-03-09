package catalogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import entities.Wine;

public class WineCatalog {

	private List<Wine> wines;
	private static WineCatalog instance;

	private WineCatalog() {
		this.wines = new ArrayList<>();
		File wineInfo = new File("storedFiles\\wineCatalog.txt");
		try {
			if(!wineInfo.exists())
				wineInfo.createNewFile();
			else
				getWinesByTextFile(wineInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static WineCatalog getInstance() {
		if (instance == null) {
			instance = new WineCatalog();
		}
		return instance;
	}

	
    private void getWinesByTextFile(File wineInfo) {
		Scanner sc = null;
		try {
			sc = new Scanner(wineInfo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(sc.hasNextLine()) {
			String[] line = sc.nextLine().split("(?!\\{.*)\\s(?![^{]*?\\})");
			this.wines.add(new Wine(line[0], new File(line[1]), stringToHashMap(line[2])));
		}
		sc.close();
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
    
	/**
	 * @return the wines
	 */
	public List<Wine> getWines() {
		return this.wines;
	}

	public Wine getWineByName(String wineName) {
		for (Wine w : this.wines)
			if (w.getName().equals(wineName))
				return w;
		return null;
	}

	public boolean addWine(String wineName, File image) {
		if (getWineByName(wineName) != null) {
			return false;
		}
		
		try {
			File wineInfo = new File("storedFiles\\wineCatalog.txt");	
			FileWriter fw = new FileWriter(wineInfo, true);
			fw.write(this.toString() + "\r\n");
			this.wines.add(new Wine(wineName, image, new HashMap<>()));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public boolean view(String wineName) {
		Wine wine = this.getWineByName(wineName);
		if (wine == null) {
			return false;
		}
		System.out.println("Informações sobre o vinho: \n");
		System.out.println(wine.printWine());

		return true;
	}
}