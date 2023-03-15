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

/**

	A classe TransactionHandler é responsável por tratar das operações de compra e venda de vinhos entre utilizadores.
*/
public class TransactionHandler {

/**

	Cria um novo anúncio de vinho para venda.
	@param user O utilizador que deseja vender o vinho.
	@param name O nome do vinho a ser vendido.
	@param price O preço unitário do vinho.
	@param quantity A quantidade disponível para venda.
	@throws WineNotFoundException Se o vinho não for encontrado.
*/
	public static void sell(User user, String name, double price, int quantity) throws WineNotFoundException {
		Wine w = WineCatalog.getInstance().getWineByName(name);
		if (w != null) 
			user.createWineAd(w, price, quantity);
		else
			throw new WineNotFoundException("O vinho nao existe");
	}

/**

	Realiza a compra de um vinho de um vendedor.
	@param buyer O utilizador que deseja comprar o vinho.
	@param wineName O nome do vinho a ser comprado.
	@param seller O nome do utilizador vendedor.
	@param quantity A quantidade desejada para compra.
	@throws NotEnoughStockException Se não houver stock suficiente.
	@throws UserNotFoundException Se o utilizador não for encontrado.
	@throws WineNotFoundException Se o vinho não for encontrado.
	@throws NotEnoughBalanceException Se não houver saldo suficiente.
*/	
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
