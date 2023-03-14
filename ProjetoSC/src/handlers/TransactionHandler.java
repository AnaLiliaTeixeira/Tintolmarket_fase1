package handlers;

import java.util.List;

import catalogs.UserCatalog;
import catalogs.WineAdCatalog;
import catalogs.WineCatalog;
import entities.User;
import entities.Wine;
import entities.WineAd;
import exceptions.NotEnoughBalanceException;
import exceptions.NotEnoughStockException;
import exceptions.UserNotFoundException;
import exceptions.WineNotFoundException;

public class TransactionHandler {

	public static void sell(User user, String name, double price, int quantity) throws WineNotFoundException {
		Wine w = WineCatalog.getInstance().getWineByName(name);
		if (w != null) 
			//WineAd wad = 
			user.createWineAd(w, price, quantity);
			//wad.adjustQuantityAndPrice(quantity, price);
		else
			throw new WineNotFoundException("O vinho nao existe");
	}

	public static void buy(User buyer, String wineName, String seller, int quantity) throws NotEnoughStockException, UserNotFoundException, WineNotFoundException, NotEnoughBalanceException {
		double balance = buyer.getBalance();

		User sellerUser = UserCatalog.getInstance().getUserByName(seller);
		if (sellerUser == null)
			throw new UserNotFoundException("O utilizador nao existe!");

		Wine wine = WineCatalog.getInstance().getWineByName(wineName);
		if (wine == null)
			throw new WineNotFoundException("O vinho nao existe!");

		WineAd wad = null;
		int availableQuantity = 0;
		double priceToPay = 0;
		List<WineAd> wineAds = wine.getCurrentAds();
		for (WineAd wa : wineAds) {
			if (wa.getUser().equals(sellerUser)) {
				wad = wa;
				availableQuantity = wa.getQuantity();
				priceToPay = wa.getPrice() * quantity;
				break;
			}
		}

		if (availableQuantity < quantity)
			throw new NotEnoughStockException("Nao existem unidades suficientes");
		if (priceToPay > balance)
			throw new NotEnoughBalanceException("Nao existe saldo suficiente");

		buyer.adjustBalance(-priceToPay);
		sellerUser.adjustBalance(priceToPay);
		wad.adjustQuantityAndPrice(-quantity, wad.getPrice());
		if (wad.getQuantity() == 0) {
			WineAdCatalog wineAdCatalog = WineAdCatalog.getInstance();
			wineAdCatalog.remove(wad);
		}
	}
}
