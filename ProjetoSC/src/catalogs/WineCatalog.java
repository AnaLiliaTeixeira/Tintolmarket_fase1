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
import exceptions.RepeatedWineException;

public class WineCatalog {

	private List<Wine> wines;
	private static WineCatalog instance;

	private WineCatalog() {
		this.wines = new ArrayList<>();
		File wineInfo = new File("storedFiles\\wineCatalog.txt");
		try {
			if (!wineInfo.exists())
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

		while (sc.hasNextLine()) {
			String[] line = sc.nextLine().split("(?!\\{.*)\\s(?![^{]*?\\})");
			this.wines.add(new Wine(line[0], new File(line[1]), stringToHashMap(line[2])));
		}
		sc.close();
	}

	/**
	 * @return the wines
	 */
	public List<Wine> getWines() {
		return this.wines;
	}

	public Wine getWineByName(String wineName) {
		for (Wine w : this.wines)
			if (wineName.equals(w.getName()))
				return w;
		return null;
	}

	public void createWine(String wineName, File image) throws RepeatedWineException {
		if (getWineByName(wineName) != null) {
			throw new RepeatedWineException("Ja existe um vinho com o mesmo nome.");
		}
		try {
			File wineInfo = new File("storedFiles\\wineCatalog.txt");
			FileWriter fw = new FileWriter(wineInfo, true);
			Wine newWine = new Wine(wineName, image, new HashMap<>());
			this.wines.add(newWine);
			fw.write(newWine + "\r\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private HashMap<String, Integer> stringToHashMap(String line) {
		HashMap<String, Integer> result = new HashMap<>();
		line = line.substring(1, line.length() - 1);
		String[] hashContents = line.split(", ");
		if (hashContents[0].contains("=")) {
			for (String s : hashContents) {
				String[] item = s.split("=");
				result.put(item[0], Integer.parseInt(item[1]));
			}
		}
		return result;
	}
}