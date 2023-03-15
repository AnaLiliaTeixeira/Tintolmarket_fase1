package entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import catalogs.WineAdCatalog;
import utils.Utils;

/**

	A classe User representa um utilizador no sistema. Cada utilizador tem um nome, saldo e caixa de mensagens (inbox).
	
	Os utilizadores podem criar anúncios de vinhos, ajustar o saldo, adicionar mensagens à caixa de mensagens e apagar todas as mensagens.
*/

public class User {

	private String name;
	private double balance;
	private HashMap<String, List<String>> inbox;

/**

	Construtor da classe User.
	@param name O nome do utilizador.
	@param balance O saldo do utilizador.
	@param inbox A caixa de mensagens do utilizador.
	@throws IOException Lança exceção caso ocorra um erro de leitura/escrita.
*/
	public User(String name, double balance, HashMap<String, List<String>> inbox) throws IOException {
		this.name = name;
		this.balance = balance;
		this.inbox = inbox;
	}

/**

	Obtém o nome do utilizador.
	@return O nome do utilizador.
*/
	public String getName() {
		return name;
	}

/**

	Define o nome do utilizador.
	@param name O nome a ser definido.
*/
	public void setName(String name) {
		this.name = name;
	}

/**

	Obtém o saldo do utilizador.
	@return O saldo do utilizador.
*/
	public double getBalance() {
		return this.balance;
	}

/**

	Ajusta o saldo do utilizador.
	@param value O valor a ser ajustado.
*/
	public void adjustBalance(double value) {
		String oldLine = this.toString();
		this.balance += value;
		String newLine = this.toString();
		Utils.replaceLine(new File("userCatalog.txt"), oldLine, newLine);
	}

/**

	Obtém a caixa de mensagens do utilizador.
	@return A caixa de mensagens do utilizador.
*/
	public HashMap<String, List<String>> getInbox() {
		return inbox;
	}

/**

	Cria um anúncio de vinho.
	@param wine O vinho a ser anunciado.
	@param price O preço do vinho.
	@param quantity A quantidade disponível.
*/
	public void createWineAd(Wine wine, double price, int quantity) {
		WineAdCatalog wineAds = WineAdCatalog.getInstance();
		WineAd newWineAd = new WineAd(this, wine, price, quantity);
		for (WineAd wad : wineAds.getWineAdsByUser(this)) {
			if (wad.equals(newWineAd)) {
				newWineAd.setQuantity(wad.getQuantity()+quantity);
				wineAds.remove(wad);
				break;
			}
		}
		wineAds.add(newWineAd);
	}

/**

	Converte a caixa de mensagens para uma string.
	@return A representação em string da caixa de mensagens.
*/
	public String inboxToString() {
		StringBuilder sb = new StringBuilder();
		for (String key : inbox.keySet()) {
			sb.append(key + ": " + inbox.get(key) + "\r\n");
		}
		return sb.toString();
	}

/**

	Apaga todas as mensagens da caixa de mensagens do utilizador.
*/
	public void deleteMessages() {
		String oldLine = this.toString();
		this.inbox.clear();
		String newLine = this.toString();
		Utils.replaceLine(new File("userCatalog.txt"), oldLine, newLine);
	}

/**

	Coloca na caixa de mensagens do utilizador a mensagem enviada por outro cliente do servico
	@param sender O utilizador que envia a mensagem.
	@param message A mensagem a ser enviada.
*/
	public void addMessage(User sender, String message) {
		String oldLine = this.toString();
		List<String> senderMessages = this.inbox.getOrDefault(sender.getName(), new ArrayList<>());
		senderMessages.add(message);
		this.inbox.put(sender.getName(), senderMessages);
		String newLine = this.toString();
		Utils.replaceLine(new File("userCatalog.txt"), oldLine, newLine);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return this.name + " " + this.balance + " " + this.inbox;
	}

}
