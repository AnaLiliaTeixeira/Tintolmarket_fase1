package catalogs;

import java.io.File;
import java.util.List;

import entities.Wine;

public class WineCatalog {

	private List<Wine> wines;
	
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
		if(getWine(wineName) != null) {
			return false;
		}
		this.wines.add(new Wine(wineName, image));
		return true;
	}
	
	//TODO
	public boolean view(String wineName) {
		StringBuilder sb = new StringBuilder();
		Wine wine = this.getWine(wineName);
		
		sb.append("\t Informacoes sobre o vinho: \n");
		sb.append("Imagem: " + wine.getImage());
		//sb.append("Classificacao Media: " + wine);
		// available wines to sell (neste caso é quando tem >0 vinhos na lista)sb.append(null)
		// ter duas listas pode ser melhor
		return false;
	}
}