package application;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import catalogs.UserCatalog;
import catalogs.WineCatalog;
import entities.User;

public class MainServer {

	private static UserCatalog userCatalog;
	private static WineCatalog wineCatalog;

	public static void main(String[] args) {

//		users = new ArrayList<>();
		ServerSocket serverSocket = null;
		
		userCatalog = UserCatalog.getInstance();
		wineCatalog = new WineCatalog();
		
		try { // criar socket
			if (args != null)
				serverSocket = new ServerSocket(Integer.parseInt(args[0]));
			else
				serverSocket = new ServerSocket(12345);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try { // handler de cada cliente
			while (true) {
				Socket socket = serverSocket.accept();
				ServerThread st = new ServerThread(socket, userCatalog);
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
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private UserCatalog userCatalog;
	private WineCatalog wineCatalog;

	public ServerThread(Socket inSoc, UserCatalog userCatalog) {
		this.socket = inSoc;
		this.userCatalog = userCatalog;
	}

	public void run() {
		try {
			// iniciar streams
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());

			// fazer login do user
			String name = userCatalog.login(in, out);
			if (name != null)
				interact(new User(name));

			// fechar ligacoes
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void interact(User user) throws Exception {
		boolean exit = false;
		boolean result = true;
		while (!exit) {
			String command = (String) in.readObject();
			switch (command) {
			case "a":
				String name = (String) in.readObject();
				File image = (File) in.readObject();
				result = wineCatalog.addWine(name, image);

				break;
			case "s":
				break;
			case "v":
				break;
			case "b":
				break;
			case "w":
				out.writeObject(user.getBalance());
				break;
			case "c":
				break;
			case "t":
				String recipient = (String) in.readObject();
				String message = (String) in.readObject();
				result = user.talk(recipient, message);
				break;
			case "r":
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