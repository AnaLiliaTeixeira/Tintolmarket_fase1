package entities;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import catalogs.WineAdCatalog;
import utils.Utils;

/**

	A classe Wine representa um vinho no sistema. Cada vinho tem um nome, imagem e classificações.

	Os vinhos podem ter anúncios associados e classificações adicionadas por utilizadores.
*/
public class Wine {

	private String name;
	private File image;
	private HashMap<String, Integer> classifications;

/**

	Construtor da classe Wine.
	@param name O nome do vinho.
	@param image A imagem do vinho.
	@param classifications As classificações do vinho.
*/
	public Wine(String name, File image, HashMap<String, Integer> classifications) {
		this.name = name;
		this.image = image;
		this.classifications = classifications;
	}

/**

	Obtém o nome do vinho.
	@return O nome do vinho.
*/
	public String getName() {
		return name;
	}

/**

	Define o nome do vinho.
	@param name O nome a ser definido.
*/
	public void setName(String name) {
		this.name = name;
	}

/**

	Obtém a imagem do vinho.
	@return A imagem do vinho.
*/
	public File getImage() {
		return image;
	}

/**

	Define a imagem do vinho.
	@param image A imagem a ser definida.
*/
	public void setImage(File image) {
		this.image = image;
	}

/**

	Obtém os anúncios atuais do vinho.
	@return A lista de anúncios atuais do vinho.
*/
	public List<WineAd> getCurrentAds() {
		WineAdCatalog wineAds = WineAdCatalog.getInstance();
		return wineAds.getWineAdsByWine(this);
	}

/**

	Obtém as classificações do vinho.
	@return As classificações do vinho.
*/
	public HashMap<String, Integer> getClassifications() {
		return classifications;
	}

/**

	Adiciona uma nova classificação ao vinho.
	@param user O autor da classificação.
	@param stars O valor numérico da classificação.
*/
	public void addClassification(User user, Integer stars) {
		String oldLine = this.toString();
		this.classifications.put(user.getName(), stars);
		String newLine = this.toString();
		File wineInfo = new File("wineCatalog.txt");
		Utils.replaceLine(wineInfo, oldLine, newLine);
	}

/**

	Retorna uma representação textual do vinho, incluindo média das classificações e informações de venda.

	@return A representação em texto do vinho.
*/
	public String printWine() {
		StringBuilder sb = new StringBuilder();
		sb.append("Nome do Vinho: " + name + " - " + image.getName() + "\n");
		double avg = 0.0;
		for (int i : classifications.values()) {
			avg += i;
		}
		if(avg != 0) {
			avg /= classifications.size();
		}
			
		sb.append("Media das classificacoes: " + avg + "\n");
		sb.append("Informacoes de venda do vinho: \n");

		if(getCurrentAds().isEmpty()) {
			sb.append("N/A");
		}
		else {
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
