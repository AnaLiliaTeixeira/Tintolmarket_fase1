package handlers;

import catalogs.WineCatalog;
import entities.User;
import entities.Wine;
import exceptions.WineNotFoundException;

/**
 * A classe ShowInfoHandler e responsavel por lidar com operacoes que exibem
 * informacoes sobre vinhos, saldo do utilizador e mensagens.
 */
public class ShowInfoHandler {

	/**
	 * Exibe informacoes detalhadas sobre um vinho.
	 * 
	 * @param wineName O nome do vinho cujas informacoes devem ser exibidas.
	 * @return Um vetor de strings com as informacoes do vinho
	 * @throws WineNotFoundException Se o vinho nao for encontrado no catalogo.
	 */
	public static String[] view(String wineName) throws WineNotFoundException {
		Wine wine = WineCatalog.getInstance().getWineByName(wineName);
		if (wine == null)
			throw new WineNotFoundException("O vinho nao existe");
		String[] info = { wine.printWine(), wine.getImage().getAbsolutePath() };
		return info;
	}

	/**
	 * Exibe o saldo do utilizador.
	 * 
	 * @param user O utilizador cujo saldo deve ser exibido.
	 * @return Uma representacao em string do saldo do utilizador.
	 */
	public static String wallet(User user) {
		return "O saldo do utilizador " + user.getName() + " e " + user.getBalance();
	}

	/**
	 * Le e apaga todas as mensagens do utilizador.
	 * 
	 * @param user O utilizador cujas mensagens devem ser lidas e apagadas.
	 * @return Uma representacao em string das mensagens do utilizador.
	 */
	public static String read(User user) {
		String inbox = user.inboxToString();
		if (inbox.equals(""))
			return "Nao tem mensagens";
		user.deleteMessages();
		return inbox;
	}
}