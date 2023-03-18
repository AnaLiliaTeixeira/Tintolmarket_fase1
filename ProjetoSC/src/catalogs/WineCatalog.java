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

/**
 * A classe WineCatalog e responsavel por gerir o catalogo de vinhos. Esta
 * classe permite criar, obter e armazenar informacoes sobre vinhos.
 */
public class WineCatalog {

	private List<Wine> wines;
	private static WineCatalog instance;

	/**
	 * Construtor privado da classe WineCatalog.
	 */
	private WineCatalog() {
		this.wines = new ArrayList<>();
		File txtFolder = new File("txtFiles");
		File wineInfo = new File("txtFiles//wineCatalog.txt");
		try {
			if (!txtFolder.exists())
				txtFolder.mkdir();
			if (!wineInfo.exists())
				wineInfo.createNewFile();
			else
				getWinesByTextFile(wineInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retorna uma instancia do catalogo de vinhos.
	 * 
	 * @return uma instancia do catalogo de vinhos.
	 */
	public static WineCatalog getInstance() {
		if (instance == null) {
			instance = new WineCatalog();
		}
		return instance;
	}

	/**
	 * Le e armazena os vinhos do arquivo de texto wineInfo.
	 * 
	 * @param wineInfo O arquivo de texto com as informacoes dos vinhos.
	 */
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
	 * Retorna a lista de vinhos.
	 * 
	 * @return a lista de vinhos.
	 */
	public List<Wine> getWines() {
		return this.wines;
	}

	/**
	 * Retorna um vinho com base no seu nome.
	 * 
	 * @param wineName O nome do vinho a ser pesquisado.
	 * @return O vinho encontrado ou null se nao encontrado.
	 */
	public Wine getWineByName(String wineName) {
		for (Wine w : this.wines)
			if (wineName.equals(w.getName()))
				return w;
		return null;
	}

	/**
	 * Cria um novo vinho e adiciona-o ao catalogo e ao arquivo de texto.
	 * 
	 * @param wineName O nome do novo vinho.
	 * @param image    O arquivo de imagem do novo vinho.
	 * @throws RepeatedWineException Se ja existir um vinho com o mesmo nome.
	 */
	public synchronized void createWine(String wineName, File image) throws RepeatedWineException {
		if (getWineByName(wineName) != null) {
			throw new RepeatedWineException("Ja existe um vinho com o mesmo nome.");
		}
		try {
			File wineInfo = new File("txtFiles//wineCatalog.txt");
			FileWriter fw = new FileWriter(wineInfo, true);
			Wine newWine = new Wine(wineName, image, new HashMap<>());
			this.wines.add(newWine);
			fw.write(newWine + "\r\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Converte uma string em um HashMap.
	 * 
	 * @param line A string a ser convertida.
	 * @return O HashMap convertido.
	 */
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