package catalogs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import entities.Wine;

public class WineCatalog {
	
	/*
	 * um ficheiro com user:password de todos os users
	 * um ficheiro por user com saldo, anuncios etc.
	 * um ficheiro com todos os vinhos
	 * 
	 */

	private List<Wine> wines;
	private static WineCatalog instance;

	private WineCatalog() {
		this.wines = new ArrayList<>();
		// chamar metodos
	}

	public static WineCatalog getInstance() {
		if (instance == null) {
			instance = new WineCatalog();
			// inicializar
		}
		return instance;
	}

	// Construtor WineCatalog (tirar duvidas com o stor primeiro)

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
		System.out.println(wine);

		return true;
	}
}