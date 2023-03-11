package catalogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entities.User;
import entities.Wine;
import entities.WineAd;
import utils.Utils;

public class WineAdCatalog {

	// Private static instance variable of the class
	private static WineAdCatalog instance;
	private List<WineAd> wineAds;

	private WineAdCatalog() {
		wineAds = new ArrayList<>();
		File wineAdsInfo = new File("storedFiles\\wineAdsCatalog.txt");
		try {
			if (!wineAdsInfo.exists())
				wineAdsInfo.createNewFile();
			else
				getWineAdsByTextFile(wineAdsInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static WineAdCatalog getInstance() {
		if (instance == null) {
			instance = new WineAdCatalog();
		}
		return instance;
	}

	private void getWineAdsByTextFile(File wineAdsInfo) {
		Scanner sc = null;
		try {
			sc = new Scanner(wineAdsInfo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (sc.hasNextLine()) {
			String[] line = sc.nextLine().split(" ");
			UserCatalog uc = UserCatalog.getInstance();
			WineCatalog wc = WineCatalog.getInstance();
			wineAds.add(new WineAd(uc.getUserByName(line[0]), wc.getWineByName(line[1]), Double.parseDouble(line[2]),
					Integer.parseInt(line[3])));
			break;
		}
		sc.close();
	}

	public List<WineAd> getWineAdsByUser(User user) {
		List<WineAd> list = new ArrayList<>();
		for (WineAd ad : wineAds) {
			if (ad.getUser().equals(user)) {
				list.add(ad);
			}
		}
		return list;
	}

	public List<WineAd> getWineAdsByWine(Wine wine) {
		List<WineAd> list = new ArrayList<>();
		for (WineAd ad : wineAds) {
			if (ad.getWine().equals(wine)) {
				list.add(ad);
			}
		}
		return list;
	}

	public void add(WineAd wineAd) {
		try {
			File wineAdInfo = new File("storedFiles\\wineAdsCatalog.txt");
			FileWriter fw = new FileWriter(wineAdInfo, true);
			fw.write(wineAd.toString() + "\r\n");
			this.wineAds.add(wineAd);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void remove(WineAd wineAd) {
		wineAds.remove(wineAd);
		File wineAdInfo = new File("storedFiles\\wineAdsCatalog.txt");
		Utils.replaceLine(wineAdInfo, wineAd.toString(), "");
	}
}
