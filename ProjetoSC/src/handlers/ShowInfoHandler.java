package handlers;

import catalogs.WineCatalog;
import entities.User;
import entities.Wine;
import exceptions.WineNotFoundException;

public class ShowInfoHandler {

	public static String view(String wineName) throws WineNotFoundException {
		Wine wine = WineCatalog.getInstance().getWineByName(wineName);
		if (wine == null)
			throw new WineNotFoundException("O vinho nao existe");
		return wine.printWine();
	}

	public static String wallet(User user) {
		return "O saldo do utilizador " + user.getName() + " e " + user.getBalance();
	}

	public static String read(User user) {
		String inbox = user.inboxToString();
		if (inbox.equals(""))
			return "Nao tem mensagens";
		user.deleteMessages();
		return inbox;
	}
}