package application;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import catalogs.UserCatalog;
import entities.User;
import handlers.AddInfoHandler;
import handlers.ShowInfoHandler;
import handlers.TransactionHandler;

public class MainServer {

	public static void main(String[] args) {

		ServerSocket serverSocket = null;

		try { // criar socket
			if (args.length != 0)
				serverSocket = new ServerSocket(Integer.parseInt(args[0]));
			else
				serverSocket = new ServerSocket(12345);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try { // handler de cada cliente
			while (true) {
				Socket socket = serverSocket.accept();
				ServerThread st = new ServerThread(socket);
				st.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try { // fechar server
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

// Threads utilizadas para comunicacao com os clientes
class ServerThread extends Thread {

	private Socket socket;

	public ServerThread(Socket inSoc) {
		this.socket = inSoc;
	}

	public void run() {
		try {
			// iniciar streams
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

			// fazer login do user
			UserCatalog userCatalog = UserCatalog.getInstance();
			String name = userCatalog.login(in, out);
			if (name != null)
				interact(userCatalog.getUserByName(name), in, out);

			// fechar ligacoes
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void interact(User user, ObjectInputStream in, ObjectOutputStream out) throws Exception {
		boolean exit = false;
		boolean result = true;
		while (!exit) {
			String command = (String) in.readObject();
			String arg1 = null;
			String arg2 = null;
			int quantity;
			switch (command) {
			case "a":
				arg1 = (String) in.readObject();
				File image = (File) in.readObject();
				result = AddInfoHandler.add(arg1, image);
				break;
			case "s":
				arg1 = (String) in.readObject();
				double price = Double.parseDouble((String) in.readObject());
				quantity = Integer.parseInt((String) in.readObject());
				TransactionHandler.sell(user, arg1, price, quantity);
				break;
			case "v":
				arg1 = (String) in.readObject();
				out.writeObject(ShowInfoHandler.view(arg2));
				break;
			case "b":
				arg1 = (String) in.readObject();
				arg2 = (String) in.readObject();
				quantity = Integer.parseInt((String) in.readObject());
				TransactionHandler.buy(user, arg1, arg2, quantity);
				break;
			case "w":
				out.writeObject(ShowInfoHandler.wallet(user));
				break;
			case "c":
				break;
			case "t":
				String recipient = (String) in.readObject();
				String message = (String) in.readObject();
				result = AddInfoHandler.talk(user, recipient, message);
				break;
			case "r":
				out.writeObject(ShowInfoHandler.read(user));
				break;
			default:
				exit = true;
				break;
			}
			if (result)
				out.writeObject("OK");
			else
				out.writeObject("Erro, destinatario nao existe");
		}
	}

}