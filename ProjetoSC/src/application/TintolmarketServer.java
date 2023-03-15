package application;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import catalogs.UserCatalog;
import entities.User;
import exceptions.WrongCredentialsException;
import handlers.AddInfoHandler;
import handlers.ShowInfoHandler;
import handlers.TransactionHandler;

/**

	Classe principal do servidor Tintolmarket.
 */
public class TintolmarketServer {

	public static void main(String[] args) {

		ServerSocket serverSocket = null;

		 // criar socket
		try {
			if (args.length != 0)
				serverSocket = new ServerSocket(Integer.parseInt(args[0]));
			else
				serverSocket = new ServerSocket(12345);
		} catch (IOException e1) {
			System.err.println("Erro na conexao com cliente");
		}


		try { // handler de cada cliente
			while (true) {
				Socket socket = serverSocket.accept();
				ServerThread st = new ServerThread(socket);
				st.start();
			}
		} catch (Exception e) {
			e.getMessage();
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("Erro ao fechar socket.");
		}
	}

}

/**

	Classe ServerThread que representa uma thread para comunicação com os clientes.
*/
class ServerThread extends Thread {

	private Socket socket;

	public ServerThread(Socket inSoc) {
		this.socket = inSoc;
	}

	public void run() {
	
		ObjectOutputStream out = null;
		ObjectInputStream in = null;

		try {
			// iniciar streams
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			// fazer login do user
			UserCatalog userCatalog = UserCatalog.getInstance();
			String name = userCatalog.login(in, out);
			if (name != null) {
				out.writeObject(true);
				interact(userCatalog.getUserByName(name), in, out);
			}

		} catch (WrongCredentialsException e) {
			try {
				System.out.println(e.getMessage());
				out.writeObject(false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// fechar ligacoes
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

/**
	Método para interagir com o usuário após a autenticação bem sucedida.
 
    @param user instância do usuário logado
    @param in   ObjectInputStream para receber informações do cliente
    @param out  ObjectOutputStream para enviar informações ao cliente
    @throws Exception em caso de erro na comunicação com o cliente
*/
	private void interact(User user, ObjectInputStream in, ObjectOutputStream out) throws Exception {
		boolean exit = false;
		while (!exit) {	
			String command = (String) in.readObject();
			String arg1 = null;
			String arg2 = null;
			int num;
			try {
				switch (command) {
				case "a":
					arg1 = (String) in.readObject();
					File image = (File) in.readObject();
					AddInfoHandler.add(arg1, image);
					out.writeObject(String.format("Vinho %s adicionado com sucesso!", arg1));
					break;
				case "s":
					arg1 = (String) in.readObject();
					double price = Double.parseDouble((String) in.readObject());
					num = Integer.parseInt((String) in.readObject());
					TransactionHandler.sell(user, arg1, price, num);
					out.writeObject(String.format("%d quantidade(s) de vinho %s colocada(s) a venda por %.2f com sucesso!", num, arg1, price));
					break;
				case "v":
					arg1 = (String) in.readObject();
					out.writeObject(ShowInfoHandler.view(arg1));
					break;
				case "b":
					arg1 = (String) in.readObject();
					arg2 = (String) in.readObject();
					num = Integer.parseInt((String) in.readObject());
					TransactionHandler.buy(user, arg1, arg2, num);
					out.writeObject(String.format("O utilizador %s comprou %d unidades de vinho %s", arg2, num, arg1));
					break;
				case "w":
					out.writeObject(ShowInfoHandler.wallet(user));
					break;
				case "c":
					arg1 = (String) in.readObject();
					num = Integer.parseInt((String) in.readObject());
					AddInfoHandler.classify(user, arg1, num);
					out.writeObject(String.format("Atribuiu %d estrelas ao vinho %s", num, arg1));
					break;
				case "t":
					String recipient = (String) in.readObject();
					String message = (String) in.readObject();
					AddInfoHandler.talk(user, recipient, message);
					out.writeObject(String.format("Enviou a mensagem \"%s\" ao utilizador %s", message, recipient));
					break;
				case "r":
					out.writeObject(ShowInfoHandler.read(user));
					break;
				default:
					exit = true;
					break;
				}
			} catch (Exception e) {
				out.writeObject(e.getMessage());
			}
		}
	}

}