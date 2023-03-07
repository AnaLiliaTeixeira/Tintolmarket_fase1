package handlers;

import java.util.List;

import catalogs.WineCatalog;
import entities.User;
import entities.Wine;
import entities.WineAd;

public class TransactionHandler {

	private WineCatalog wineCatalog;
	
	public TransactionHandler(WineCatalog wineCatalog) {
		this.wineCatalog = wineCatalog;
	}
	
	public boolean sell(User user, String name, double price, int quantity) {
		
		Wine w = wineCatalog.getWine(name);
		if(w != null) {
			user.createWineAd(w, quantity, price);
			return true;
		}
		return false;
	}
	
	public boolean buy(User buyer, String wineName, User seller, int quantity) {
		double balance = buyer.getBalance();
		Wine wine = wineCatalog.getWine(wineName);
		if(wine == null) return false;
		
		WineAd wad=null;
		int availableQuantity=0;
		double priceToPay=0;
		List<WineAd> wineAds = seller.getWineAds();
		for(WineAd wa : wineAds) {
			if(wa.getWine().equals(wine)) {
				wad = wa;
				availableQuantity = wa.getQuantity();
				priceToPay = wa.getPrice()*quantity;
				break;
			}
		}
		
		if(availableQuantity < quantity) return false;
		if(priceToPay > balance) return false;
		
		buyer.adjustBalance(-priceToPay);
		seller.adjustBalance(priceToPay);
		
		// add a lista do comprador (buyer) c/ a qnt que comprou do seller?? 
		wad.adjustQuantity(-quantity);

		return true;		
	}
}
