package handlers;

import catalogs.WineCatalog;
import entities.User;
import entities.Wine;

public class ShowInfoHandler {

	public static String view(String wineName) {
		Wine wine = WineCatalog.getInstance().getWineByName(wineName);
		if (wine == null)
			return "O vinho nao existe";
		return wine.printWine();
	}

	public static String wallet(User user) {
		return "O saldo utilizador " + user.getName() + " e " + user.getBalance();
	}

	public static String read(User user) {
		String inbox = user.inboxToString();
		if (inbox.equals(""))
			return "Nao tem mensagens";
		user.deleteMessages();
		return inbox;
	}

}