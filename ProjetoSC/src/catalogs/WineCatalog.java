package catalogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
		try {
			Scanner myReader = new Scanner(wineInfo);
			while(myReader.hasNextLine()) {
				String line[] = myReader.nextLine().split(" ");
				wines.add(new Wine(line[0]));
			}
			myReader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("An error ocurred.");
			e.printStackTrace();
		} 
    }
	/**
	 * @return the wines
	 */
	public List<Wine> getWines() {
		return this.wines;
	}

	public Wine getWine(String wineName) {
		for (Wine w : this.wines)
			if (w.getName().equals(wineName))
				return w;
		return null;
	}

	public boolean addWine(String wineName, File image) {
		if (getWine(wineName) != null) {
			return false;
		}
		this.wines.add(new Wine(wineName, image));
		return true;
	}

	public boolean view(String wineName) {
		Wine wine = this.getWine(wineName);
		if (wine == null) {
			return false;
		}
		System.out.println("Informações sobre o vinho: \n");
		System.out.println(wine.printWine());

		return true;
	}
}