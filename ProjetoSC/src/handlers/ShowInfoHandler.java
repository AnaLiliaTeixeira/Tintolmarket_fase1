package handlers;

import catalogs.WineCatalog;
import entities.User;
import entities.Wine;
import exceptions.WineNotFoundException;

/**

	A classe ShowInfoHandler é responsável por lidar com operações que exibem informações sobre vinhos, saldo do utilizador e mensagens.
*/
public class ShowInfoHandler {

/**

	Exibe informações detalhadas sobre um vinho.
	@param wineName O nome do vinho cujas informações devem ser exibidas.
	@return Uma representação em string das informações do vinho.
	@throws WineNotFoundException Se o vinho não for encontrado no catálogo.
*/
	public static String view(String wineName) throws WineNotFoundException {
		Wine wine = WineCatalog.getInstance().getWineByName(wineName);
		if (wine == null)
			throw new WineNotFoundException("O vinho nao existe");
		return wine.printWine();
	}

/**

	Exibe o saldo do utilizador.
	@param user O utilizador cujo saldo deve ser exibido.
	@return Uma representação em string do saldo do utilizador.
*/
	public static String wallet(User user) {
		return "O saldo do utilizador " + user.getName() + " e " + user.getBalance();
	}
	
/**

	Lê e apaga todas as mensagens do utilizador.
	@param user O utilizador cujas mensagens devem ser lidas e apagadas.
	@return Uma representação em string das mensagens do utilizador.
*/
	public static String read(User user) {
		String inbox = user.inboxToString();
		if (inbox.equals(""))
			return "Nao tem mensagens";
		user.deleteMessages();
		return inbox;
	}
}