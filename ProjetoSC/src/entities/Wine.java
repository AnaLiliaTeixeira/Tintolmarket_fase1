package entities;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import catalogs.WineAdCatalog;
import utils.Utils;

/**
 * A classe Wine representa um vinho no sistema. Cada vinho tem um nome, imagem
 * e classificacoes.
 * 
 * Os vinhos podem ter anuncios associados e classificacoes adicionadas por
 * utilizadores.
 */
public class Wine {

	private String name;
	private File image;
	private HashMap<String, Integer> classifications;

	/**
	 * Construtor da classe Wine.
	 * 
	 * @param name            O nome do vinho.
	 * @param image           A imagem do vinho.
	 * @param classifications As classificacoes do vinho.
	 */
	public Wine(String name, File image, HashMap<String, Integer> classifications) {
		this.name = name;
		this.image = image;
		this.classifications = classifications;
	}

	/**
	 * Obtem o nome do vinho.
	 * 
	 * @return O nome do vinho.
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * Define o nome do vinho.
	 * 
	 * @param name O nome a ser definido.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtem a imagem do vinho.
	 * 
	 * @return A imagem do vinho.
	 */
	public File getImage() {
		return image;
	}

	/**
	 * Obtem os anuncios atuais do vinho.
	 * 
	 * @return A lista de anuncios atuais do vinho.
	 */
	public List<WineAd> getCurrentAds() {
		WineAdCatalog wineAds = WineAdCatalog.getInstance();
		return wineAds.getWineAdsByWine(this);
	}

	/**
	 * Obtem as classificacoes do vinho.
	 * 
	 * @return As classificacoes do vinho.
	 */
	public HashMap<String, Integer> getClassifications() {
		return classifications;
	}

	/**
	 * Adiciona uma nova classificacao ao vinho.
	 * 
	 * @param user  O autor da classificacao.
	 * @param stars O valor numerico da classificacao.
	 */
	public void addClassification(User user, Integer stars) {
		String oldLine = this.toString();
		this.classifications.put(user.getName(), stars);
		String newLine = this.toString();
		File wineInfo = new File("wineCatalog.txt");
		Utils.replaceLine(wineInfo, oldLine, newLine);
	}

	/**
	 * Retorna uma representacao textual do vinho, incluindo media das
	 * classificacoes e informacoes de venda.
	 * 
	 * @return A representacao em texto do vinho.
	 */
	public String printWine() {
		StringBuilder sb = new StringBuilder();
		sb.append("Nome do Vinho: " + name + " - " + this.image.getName() + "\n");
		double avg = 0.0;
		for (int i : classifications.values()) {
			avg += i;
		}
		if (avg != 0) {
			avg /= classifications.size();
		}

		sb.append("Media das classificacoes: " + avg + "\n");
		sb.append("Informacoes de venda do vinho: \n");

		if (getCurrentAds().isEmpty()) {
			sb.append("N/A");
		} else {
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
