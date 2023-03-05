package application;

import java.io.File;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import entities.User;
import entities.Wine;

public class MainServer {

	public static void main(String[] args) {

		ServerSocket serverSocket = null;

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
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public ServerThread(Socket inSoc) {
		this.socket = inSoc;
	}

	public void run() {
		try {
			// iniciar streams
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());

			// fazer login do user
			String name = login();
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

	/**
	 * Faz login do utilizador ou cria um utilizador novo
	 * 
	 * @return o username se login com sucesso ou null, caso contrario
	 * @throws Exception se ocorrer erro ao ler ou escrever
	 */
	private String login() throws Exception {
		File users = new File("userCreds.txt");
		if (!users.exists())
			users.createNewFile();
		Scanner sc = new Scanner(users);

		// le user e pass da socket
		String user = (String) in.readObject();
		String password = (String) in.readObject();

		// verifica se user existe e pass esta correta
		boolean newUser = true;
		String line;
		while (sc.hasNextLine()) {
			if ((line = sc.nextLine()).startsWith(user) && line.endsWith(password)) { // se for a pass certa
				newUser = false;
				break;
			} else if (line.startsWith(user)) { // se for a pass errada
				sc.close();
				return null;
			}
		}
		sc.close();

		// se o user nao existir faz o seu registo
		if (newUser) {
			FileWriter fw = new FileWriter("userCreds.txt");
			fw.write(user + ":" + password);
			fw.close();
		}

		return user;
	}

	private void interact(User user) throws Exception {
		boolean exit = false;
		while (!exit) {
			String command = (String) in.readObject();
			switch (command) {
			case "a":
				String name = (String) in.readObject();
				File image = (File) in.readObject();
				Wine w = new Wine(name, image);
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
				boolean result = user.talk(recipient, message);
				if (result)
					out.writeObject("OK");
				else
					out.writeObject("Erro, destinatario nao existe");
				break;
			case "r":
				break;
			default:
				exit = true;
				break;
			}
		}
	}

}