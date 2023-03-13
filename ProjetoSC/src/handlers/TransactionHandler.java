package handlers;

import java.util.List;

import catalogs.UserCatalog;
import catalogs.WineAdCatalog;
import catalogs.WineCatalog;
import entities.User;
import entities.Wine;
import entities.WineAd;

public class TransactionHandler {

	public static boolean sell(User user, String name, double price, int quantity) {
		Wine w = WineCatalog.getInstance().getWineByName(name);
		if (w != null) {
			WineAd wad = user.createWineAd(w, price, quantity);
			wad.adjustQuantityAndPrice(quantity, price);
			return true;
		}
		return false;
	}

	public static boolean buy(User buyer, String wineName, String seller, int quantity) {
		double balance = buyer.getBalance();

		User sellerUser = UserCatalog.getInstance().getUserByName(seller);
		if (sellerUser == null)
			return false;

		Wine wine = WineCatalog.getInstance().getWineByName(wineName);
		if (wine == null)
			return false;

		WineAd wad = null;
		int availableQuantity = 0;
		double priceToPay = 0;
		List<WineAd> wineAds = wine.getCurrentAds();
		for (WineAd wa : wineAds) 
		{
			if (wa.getUser().equals(sellerUser)) {
				wad = wa;
				availableQuantity = wa.getQuantity();
				priceToPay = wa.getPrice() * quantity;
				break;
			}
		}

		if (availableQuantity < quantity)
			return false;
		if (priceToPay > balance)
			return false;

		buyer.adjustBalance(-priceToPay);
		sellerUser.adjustBalance(priceToPay);
		wad.adjustQuantityAndPrice(-quantity, wad.getPrice());
		if (wad.getQuantity() == 0) {
			WineAdCatalog wineAdCatalog = WineAdCatalog.getInstance();
			wineAdCatalog.remove(wad);
		}

		return true;
	}

}
