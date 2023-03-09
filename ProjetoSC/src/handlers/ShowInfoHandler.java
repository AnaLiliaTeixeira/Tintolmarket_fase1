package handlers;

import catalogs.WineCatalog;
import entities.User;
import entities.Wine;

public class ShowInfoHandler {

	// view
	public static boolean view(String wineName) {
		Wine wine = WineCatalog.getInstance().getWineByName(wineName);
		if (wine == null) {
			return false;
		}
		System.out.println("Informacoes sobre o vinho: \n");
		System.out.println(wine.printWine());

		return true;
	}

	// wallet
	public static String wallet(User user) {
		return "O saldo utilizador " + user.getName() + " e " + user.getBalance();
	}

	// read
	public static String read(User user) {
		String inbox = user.inboxToString();
		user.deleteMessages();
		return inbox;
	}

}