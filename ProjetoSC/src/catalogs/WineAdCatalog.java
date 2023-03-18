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

/**
 * A classe WineAdCatalog e responsavel por gerir o catalogo de anuncios de
 * vinho. Esta classe permite adicionar, remover e obter anuncios de vinho por
 * utilizador ou vinho.
 */
public class WineAdCatalog {

	private static WineAdCatalog instance;
	private List<WineAd> wineAds;

	/**
	 * Construtor privado da classe WineAdCatalog.
	 */
	private WineAdCatalog() {
		wineAds = new ArrayList<>();
		File txtFolder = new File("txtFiles");
		File wineAdsInfo = new File("txtFiles//wineAdsCatalog.txt");
		try {
			if (!txtFolder.exists())
				txtFolder.mkdir();
			if (!wineAdsInfo.exists())
				wineAdsInfo.createNewFile();
			else
				getWineAdsByTextFile(wineAdsInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retorna uma instancia do catalogo de anuncios de vinho.
	 * 
	 * @return uma instancia do catalogo de anuncios de vinho.
	 */
	public static WineAdCatalog getInstance() {
		if (instance == null) {
			instance = new WineAdCatalog();
		}
		return instance;
	}

	/**
	 * Le e armazena os anuncios de vinho do arquivo de texto wineAdsInfo.
	 * 
	 * @param wineAdsInfo O arquivo de texto com as informacoes dos anuncios de
	 *                    vinho.
	 */
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

	/**
	 * Retorna uma lista de anuncios de vinho associados a um utilizador especifico.
	 * 
	 * @param user O utilizador a ser pesquisado.
	 * @return Uma lista de anuncios de vinho associados ao utilizador.
	 */
	public List<WineAd> getWineAdsByUser(User user) {
		List<WineAd> list = new ArrayList<>();
		for (WineAd ad : wineAds) {
			if (ad.getUser().equals(user)) {
				list.add(ad);
			}
		}
		return list;
	}

	/**
	 * Retorna uma lista de anúncios de vinho associados a um vinho específico.
	 * 
	 * @param wine O vinho a ser pesquisado.
	 * @return Uma lista de anúncios de vinho associados ao vinho.
	 */
	public List<WineAd> getWineAdsByWine(Wine wine) {
		List<WineAd> list = new ArrayList<>();
		for (WineAd ad : wineAds) {
			if (ad.getWine().equals(wine)) {
				list.add(ad);
			}
		}
		return list;
	}

	/**
	 * Adiciona um novo anuncio de vinho ao catalogo e ao arquivo de texto.
	 * 
	 * @param wineAd O anuncio de vinho a ser adicionado.
	 */
	public synchronized void addWineAd(WineAd wineAd) {
		try {
			File wineAdInfo = new File("txtFiles//wineAdsCatalog.txt");
			FileWriter fw = new FileWriter(wineAdInfo, true);
			fw.write(wineAd.toString() + "\r\n");
			this.wineAds.add(wineAd);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remove um anuncio de vinho do catalogo e do arquivo de texto.
	 * 
	 * @param wineAd O anuncio de vinho a ser removido.
	 */
	public void remove(WineAd wineAd) {
		wineAds.remove(wineAd);
		File wineAdInfo = new File("txtFiles//wineAdsCatalog.txt");
		Utils.replaceLine(wineAdInfo, wineAd.toString(), null);
	}
}
