package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * Classe principal do cliente Tintolmarket. Esta classe a responsavel por
 * estabelecer a conexao com o servidor, autenticar o utilizador e permitir a
 * interacao com o servidor atraves de comandos de texto.
 *
 */
public class Tintolmarket {

	private static Socket socket;
	private static String name;

	public static void main(String[] args) {

		// retirar ip e port
		String[] serverInfo = args[0].split(":");

		try {
			// estabelecer ligacao
			if (serverInfo.length != 1)
				socket = new Socket(serverInfo[0], Integer.parseInt(serverInfo[1]));
			else
				socket = new Socket(serverInfo[0], 12345);

			// iniciar streams
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

			// enviar user e password
			name = args[1];
			out.writeObject(args[1]);
			out.writeObject(args[2]);
			if ((boolean) in.readObject()) {
				System.out.println("Autenticacao bem sucedida!");
				// interagir com o server
				interact(in, out);
			} else
				System.out.println("Ocorreu um erro na autenticacao.");

			// fechar ligacoes
			in.close();
			out.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Metodo que permite ao utilizador interagir com o servidor atraves de comandos
	 * de texto. Os comandos sao lidos da entrada padrao e enviados ao servidor para
	 * serem processados. As respostas do servidor sao apresentadas na saida padrao.
	 *
	 * @param in  ObjectInputStream para ler dados do servidor.
	 * @param out ObjectOutputStream para enviar dados para o servidor.
	 * @throws Exception Se ocorrer algum erro durante a interašao com o servidor.
	 */
	private static void interact(ObjectInputStream in, ObjectOutputStream out) throws Exception {
		System.out.println(
				"Comandos disponiveis: \n\tadd <wine> <image> - adiciona um novo vinho identificado por wine, associado a imagem\r\n"
						+ "image.\n"
						+ "\tsell <wine> <value> <quantity> - coloca a venda o numero indicado por quantity de\r\n"
						+ "unidades do vinho wine pelo valor value.\n"
						+ "\tview <wine> - obtem as informacoes associadas ao vinho identificado por wine,\r\n"
						+ "nomeadamente a imagem associada, a classificacao media e, caso existam unidades do\r\n"
						+ "vinho disponiveis para venda, a indicacao do utilizador que as disponibiliza, o preco e a\r\n"
						+ "quantidade disponivel.\n"
						+ "\tbuy <wine> <seller> <quantity> - compra quantity unidades do vinho wine ao utilizador\r\n"
						+ "seller.\n" + "\twallet - obtem o saldo atual da carteira.\r\n"
						+ "\tclassify <wine> <stars> - atribui ao vinho wine uma classificacao de 1 a 5.\r\n"
						+ "\ttalk <user> <message> - permite enviar uma mensagem privada ao utilizador user.\n"
						+ "\tread - permite ler as novas mensagens recebidas.");

		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.print("\nInsira um comando: ");
			String line = sc.nextLine();
			String[] tokens = line.split(" ");
			boolean wait = false;
			boolean image = false;
			if (tokens[0].equals("a") || tokens[0].equals("add")) {
				if (tokens.length != 3)
					System.out.println("O comando add e usado na forma \"add <wine> <image>\"");
				else {
					File img = new File(tokens[2]);
					if (img.exists()) {
						out.writeObject("a");
						out.writeObject(tokens[1]);
						FileInputStream file = new FileInputStream(img);
						out.writeObject(file.getChannel().size()); // enviar tamanho
						out.writeObject(img.getName()); // enviar nome
						byte[] bytes = new byte[16 * 1024];
						while (file.read(bytes) > 0)
							out.write(bytes);
						wait = true;
						file.close();
					} else {
						System.out.println("A imagem " + tokens[2] + " nao existe!");
					}
				}
			} else if (tokens[0].equals("s") || tokens[0].equals("sell")) {
				if (tokens.length != 4)
					System.out.println("O comando sell e usado na forma \"sell <wine> <value> <quantity>\"");
				else {
					out.writeObject("s");
					out.writeObject(tokens[1]);
					out.writeObject(tokens[2]);
					out.writeObject(tokens[3]);
					wait = true;
				}
			} else if (tokens[0].equals("v") || tokens[0].equals("view")) {
				if (tokens.length != 2)
					System.out.println("O comando view e usado na forma \"view <wine>\"");
				else {
					out.writeObject("v");
					out.writeObject(tokens[1]);
					wait = true;
					image = true;
				}
			} else if (tokens[0].equals("b") || tokens[0].equals("buy")) {
				if (tokens.length != 4)
					System.out.println("O comando buy e usado na forma \"buy <wine> <seller> <quantity>\"");
				else {
					out.writeObject("b");
					out.writeObject(tokens[1]);
					out.writeObject(tokens[2]);
					out.writeObject(tokens[3]);
					wait = true;
				}
			} else if (tokens[0].equals("w") || tokens[0].equals("wallet")) {
				if (tokens.length != 1)
					System.out.println("O comando wallet e usado na forma \"wallet\"");
				else {
					out.writeObject("w");
					wait = true;
				}
			} else if (tokens[0].equals("c") || tokens[0].equals("classify")) {
				if (tokens.length != 3)
					System.out.println("O comando classify e usado na forma \"classify <wine> <stars>\"");
				else {
					out.writeObject("c");
					out.writeObject(tokens[1]);
					out.writeObject(tokens[2]);
					wait = true;
				}
			} else if (tokens[0].equals("t") || tokens[0].equals("talk")) {
				if (tokens.length < 3)
					System.out.println("O comando talk e usado na forma \"talk <user> <message>\"");
				else {
					StringBuilder sb = new StringBuilder();
					for (int i = 2; i < tokens.length; i++) {
						sb.append(tokens[i] + " ");
					}
					out.writeObject("t");
					out.writeObject(tokens[1]);
					out.writeObject(sb.toString());
					wait = true;
				}
			} else if (tokens[0].equals("r") || tokens[0].equals("read")) {
				if (tokens.length != 1)
					System.out.println("O comando read e usado na forma \"read \"");
				else {
					out.writeObject("r");
					wait = true;
				}
			} else if (tokens[0].equals("exit")) {
				System.out.println("A encerrar programa...");
				out.writeObject("exit");
				break;
			} else {
				System.out.println("Comando nao reconhecido");
				System.out.println(
						"Comandos disponiveis: \n\tadd <wine> <image> - adiciona um novo vinho identificado por wine, associado a imagem\r\n"
								+ "image.\n"
								+ "\tsell <wine> <value> <quantity> - coloca a venda o numero indicado por quantity de\r\n"
								+ "unidades do vinho wine pelo valor value.\n"
								+ "\tview <wine> - obtem as informacoes associadas ao vinho identificado por wine,\r\n"
								+ "nomeadamente a imagem associada, a classificacao media e, caso existam unidades do\r\n"
								+ "vinho disponiveis para venda, a indicacao do utilizador que as disponibiliza, o preco e a\r\n"
								+ "quantidade disponivel.\n"
								+ "\tbuy <wine> <seller> <quantity> - compra quantity unidades do vinho wine ao utilizador\r\n"
								+ "seller.\n" + "\twallet - obtem o saldo atual da carteira.\r\n"
								+ "\tclassify <wine> <stars> - atribui ao vinho wine uma classificacao de 1 a 5.\r\n"
								+ "\ttalk <user> <message> - permite enviar uma mensagem privada ao utilizador user.\n"
								+ "\tread - permite ler as novas mensagens recebidas.");
			}

			if (wait)
				System.out.println((String) in.readObject());
			if (image) {
				long fileSize = (long) in.readObject(); // ler tamanho da imagem
				int bytesRead;
				long totalBytesRead = 0;
				File dir = new File(name);
				if (!dir.exists())
					dir.mkdir();
				File img = new File(name + "//" + (String) in.readObject());
				img.createNewFile();
				FileOutputStream file = new FileOutputStream(img);
				byte[] bytes = new byte[16 * 1024];
				while (totalBytesRead < fileSize) {
					bytesRead = in.read(bytes);
					file.write(bytes, 0, bytesRead);
					totalBytesRead += bytesRead;
				}
				file.close();
				while (in.available() > 0) // limpar stream depois de transferir ficheiro
					in.read(bytes);
			}
		}
		sc.close();
	}
}
