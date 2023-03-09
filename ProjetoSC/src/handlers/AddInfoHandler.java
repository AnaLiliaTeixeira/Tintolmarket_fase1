package handlers;

import java.io.File;
import java.io.IOException;

import catalogs.UserCatalog;
import catalogs.WineCatalog;
import entities.User;

public class AddInfoHandler {

	public static boolean add(String wineName, File image) {
		return WineCatalog.getInstance().createWine(wineName, image);
	}

	// classify

	public static boolean talk(User sender, String recipient, String message) throws IOException {
		User destinatary = UserCatalog.getInstance().getUserByName(recipient);
		if (destinatary == null)
			return false;
		destinatary.addMessage(sender, message);
		return true;
	}

}