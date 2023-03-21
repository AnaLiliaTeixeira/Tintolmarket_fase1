package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import catalogs.UserCatalog;
import entities.User;
import exceptions.WrongCredentialsException;
import handlers.AddInfoHandler;
import handlers.ShowInfoHandler;
import handlers.TransactionHandler;

/**
 * Classe principal do servidor Tintolmarket.
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
			System.out.println("Erro na conexao com cliente");
		}

		try { // handler de cada cliente
			while (true) {
				Socket socket = serverSocket.accept();
				ServerThread st = new ServerThread(socket);
				st.start();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("Erro ao fechar socket.");
		}
	}

}

/**
 * 
 * Classe ServerThread que representa uma thread para comunicação com os
 * clientes.
 */
class ServerThread extends Thread {

	private Socket socket;

	public ServerThread(Socket inSoc) {
		this.socket = inSoc;
	}

	public void run() {

		DataOutputStream out = null;
		DataInputStream in = null;

		try {
			// iniciar streams
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());

			// fazer login do user
			UserCatalog userCatalog = UserCatalog.getInstance();
			String name = userCatalog.login(in, out);
			if (name != null) {
				out.writeBoolean(true);
				interact(userCatalog.getUserByName(name), in, out);
			}

		} catch (WrongCredentialsException e) {
			try {
				System.out.println(e.getMessage());
				out.writeBoolean(false);
			} catch (IOException e1) {
				System.out.println("Ocorreu um erro na comunicacao");
			}
		} catch (Exception e) {
			System.out.println("Cliente desconectado");
		} finally {
			try {
				// fechar ligacoes
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				System.out.println("Ocorreu um erro na comunicacao");
			}
		}
	}

	/**
	 * Metodo para interagir com o usuario apos a autenticacao bem sucedida.
	 * 
	 * @param user instancia do usuario logado
	 * @param in   ObjectInputStream para receber informacoes do cliente
	 * @param out  ObjectOutputStream para enviar informacoes ao cliente
	 * @throws Exception em caso de erro na comunicacao com o cliente
	 */
	private void interact(User user, DataInputStream in, DataOutputStream out) throws Exception {
		boolean exit = false;
		while (!exit) {
			String command = in.readUTF();
			String arg1 = null;
			String arg2 = null;
			int num;
			try {
				switch (command) {
				case "a":
					arg1 = in.readUTF();
					long fileSize = in.readLong(); // ler tamanho da imagem
					int bytesRead;
					long totalBytesRead = 0;
					File imgFiles = new File("imgFiles");
					if (!imgFiles.exists())
						imgFiles.mkdir();
					File image = new File("imgFiles//" + in.readUTF()); // ler nome da imagem
					FileOutputStream file = new FileOutputStream(image);
					byte[] bytes = new byte[16 * 1024];
					while (totalBytesRead < fileSize) {
						bytesRead = in.read(bytes);
						file.write(bytes, 0, bytesRead);
						totalBytesRead += bytesRead;
					}
					file.close();
					AddInfoHandler.add(arg1, image);
					out.writeUTF(String.format("Vinho %s adicionado com sucesso!", arg1));
					break;
				case "s":
					arg1 = in.readUTF();
					double price = Double.parseDouble(in.readUTF());
					num = Integer.parseInt(in.readUTF());
					TransactionHandler.sell(user, arg1, price, num);
					out.writeUTF(String.format("%d quantidade(s) de vinho %s colocada(s) a venda por %.2f com sucesso!",
							num, arg1, price));
					break;
				case "v":
					arg1 = in.readUTF();
					String[] result = ShowInfoHandler.view(arg1);
					out.writeUTF(result[0]); // enviar printWine
					File img = new File(result[1]);
					FileInputStream imgStream = new FileInputStream(img);
					out.writeLong(imgStream.getChannel().size()); // enviar tamanho
					out.writeUTF(img.getName()); // enviar nome
					byte[] buffer = new byte[16 * 1024];
					while (imgStream.read(buffer) > 0)
						out.write(buffer);
					imgStream.close();
					break;
				case "b":
					arg1 = in.readUTF();
					arg2 = in.readUTF();
					num = Integer.parseInt(in.readUTF());
					TransactionHandler.buy(user, arg1, arg2, num);
					out.writeUTF(String.format("O utilizador %s comprou %d unidades de vinho %s", arg2, num, arg1));
					break;
				case "w":
					out.writeUTF(ShowInfoHandler.wallet(user));
					break;
				case "c":
					arg1 = in.readUTF();
					num = Integer.parseInt(in.readUTF());
					AddInfoHandler.classify(user, arg1, num);
					out.writeUTF(String.format("Atribuiu %d estrelas ao vinho %s", num, arg1));
					break;
				case "t":
					String recipient = in.readUTF();
					String message = in.readUTF();
					AddInfoHandler.talk(user, recipient, message);
					out.writeUTF(String.format("Enviou a mensagem \"%s\" ao utilizador %s", message, recipient));
					break;
				case "r":
					out.writeUTF(ShowInfoHandler.read(user));
					break;
				default:
					exit = true;
					break;
				}
			} catch (Exception e) {
				out.writeUTF(e.getMessage());
			}
		}
	}

}