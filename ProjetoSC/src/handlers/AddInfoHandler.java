package handlers;

import java.io.File;
import java.io.IOException;

import catalogs.UserCatalog;
import catalogs.WineCatalog;
import entities.User;
import entities.Wine;

public class AddInfoHandler {

	public static boolean add(String wineName, File image) {
		return WineCatalog.getInstance().createWine(wineName, image);
	}

	public static boolean classify(User user, String wine, int stars) {
		Wine w = WineCatalog.getInstance().getWineByName(wine);
		if (w == null)
			return false;
		w.addClassification(user, stars);
		return true;
	}

	public static boolean talk(User sender, String recipient, String message) throws IOException {
		User destinatary = UserCatalog.getInstance().getUserByName(recipient);
		if (destinatary == null)
			return false;
		destinatary.addMessage(sender, message);
		return true;
	}

}