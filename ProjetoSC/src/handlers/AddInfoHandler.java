package handlers;

import java.io.File;
import java.io.IOException;

import catalogs.UserCatalog;
import catalogs.WineCatalog;
import entities.User;
import entities.Wine;
import exceptions.RepeatedWineException;
import exceptions.UserNotFoundException;
import exceptions.WineNotFoundException;

/**
 * 
 * A classe AddInfoHandler a responsavel por lidar com as operacoes de adicao e
 * classificacao de vinhos e troca de mensagens entre utilizadores.
 */
public class AddInfoHandler {

	/**
	 * Adiciona um novo vinho ao catalogo de vinhos e associa uma imagem a ele.
	 * 
	 * @param wineName O nome do novo vinho.
	 * @param image    O arquivo de imagem do novo vinho.
	 * @throws RepeatedWineException Se ja existir um vinho com o mesmo nome.
	 */
	public static void add(String wineName, File image) throws RepeatedWineException {
		WineCatalog.getInstance().createWine(wineName, image);
	}

	/**
	 * Classifica um vinho com base na pontuacao fornecida por um utilizador.
	 * 
	 * @param user  O utilizador que classifica o vinho.
	 * @param wine  O nome do vinho a ser classificado.
	 * @param stars A pontuacao atribuida ao vinho (1 a 5 estrelas).
	 * @throws WineNotFoundException Se o vinho nao for encontrado no catalogo.
	 */
	public static void classify(User user, String wine, int stars) throws WineNotFoundException {
		Wine w = WineCatalog.getInstance().getWineByName(wine);
		if (stars < 1 || stars > 5)
			throw new IllegalArgumentException("Classificacao invalida. Sao permitidos valores entre 1 e 5.");
		if (w == null)
			throw new WineNotFoundException("O vinho nao existe");
		w.addClassification(user, stars);
	}

	/**
	 * Adiciona uma mensagem enviada por um utilizador a outro utilizador.
	 * 
	 * @param sender    O utilizador que envia a mensagem.
	 * @param recipient O nome do utilizador destinatário da mensagem.
	 * @param message   O contecado da mensagem.
	 * @throws IOException           Se ocorrer um erro de entrada/saida.
	 * @throws UserNotFoundException Se o utilizador destinatario nao for
	 *                               encontrado.
	 */
	public static void talk(User sender, String recipient, String message) throws IOException, UserNotFoundException {
		User destinatary = UserCatalog.getInstance().getUserByName(recipient);
		if (destinatary == null)
			throw new UserNotFoundException("O utilizador nao existe");
		destinatary.addMessage(sender, message);
	}
}