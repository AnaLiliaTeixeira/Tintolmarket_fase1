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

public class AddInfoHandler {

	public static void add(String wineName, File image) throws RepeatedWineException {
		WineCatalog.getInstance().createWine(wineName, image);
	}

	public static void classify(User user, String wine, int stars) throws WineNotFoundException {
		Wine w = WineCatalog.getInstance().getWineByName(wine);
		if (w == null || stars < 1 || stars > 6)
			throw new WineNotFoundException("O vinho nao existe");
		w.addClassification(user, stars);
	}

	public static void talk(User sender, String recipient, String message) throws IOException, UserNotFoundException {
		User destinatary = UserCatalog.getInstance().getUserByName(recipient);
		if (destinatary == null)
			throw new UserNotFoundException("O utilizador nao existe");
		destinatary.addMessage(sender, message);
	}
}