package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());

			// enviar user e password
			name = args[1];
			out.writeUTF(args[1]);
			out.writeUTF(args[2]);
			if (in.readBoolean()) {
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
	 * @throws Exception Se ocorrer algum erro durante a interaçao com o servidor.
	 */
	private static void interact(DataInputStream in, DataOutputStream out) throws Exception {
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
		boolean exit = false;
		while (!exit) {
			System.out.print("\nInsira um comando: ");
			String line = sc.nextLine();
			String[] tokens = line.split(" ");
			boolean image = false;
			if (tokens[0].equals("a") || tokens[0].equals("add")) {
				if (tokens.length != 3)
					System.out.println("O comando add e usado na forma \"add <wine> <image>\"");
				else {
					File img = new File(tokens[2]);
					if (img.exists()) {
						out.writeUTF("a");
						out.writeUTF(tokens[1]);
						FileInputStream file = new FileInputStream(img);
						out.writeLong(file.getChannel().size()); // enviar tamanho
						out.writeUTF(img.getName()); // enviar nome
						byte[] bytes = new byte[16 * 1024];
						while (file.read(bytes) > 0)
							out.write(bytes);
						file.close();
					} else {
						System.out.println("A imagem " + tokens[2] + " nao existe!");
					}
				}
			} else if (tokens[0].equals("s") || tokens[0].equals("sell")) {
				if (tokens.length != 4)
					System.out.println("O comando sell e usado na forma \"sell <wine> <value> <quantity>\"");
				else {
					out.writeUTF("s");
					out.writeUTF(tokens[1]);
					out.writeUTF(tokens[2]);
					out.writeUTF(tokens[3]);
				}
			} else if (tokens[0].equals("v") || tokens[0].equals("view")) {
				if (tokens.length != 2)
					System.out.println("O comando view e usado na forma \"view <wine>\"");
				else {
					out.writeUTF("v");
					out.writeUTF(tokens[1]);
					image = true;
				}
			} else if (tokens[0].equals("b") || tokens[0].equals("buy")) {
				if (tokens.length != 4)
					System.out.println("O comando buy e usado na forma \"buy <wine> <seller> <quantity>\"");
				else {
					out.writeUTF("b");
					out.writeUTF(tokens[1]);
					out.writeUTF(tokens[2]);
					out.writeUTF(tokens[3]);
				}
			} else if (tokens[0].equals("w") || tokens[0].equals("wallet")) {
				if (tokens.length != 1)
					System.out.println("O comando wallet e usado na forma \"wallet\"");
				else {
					out.writeUTF("w");
				}
			} else if (tokens[0].equals("c") || tokens[0].equals("classify")) {
				if (tokens.length != 3)
					System.out.println("O comando classify e usado na forma \"classify <wine> <stars>\"");
				else {
					out.writeUTF("c");
					out.writeUTF(tokens[1]);
					out.writeUTF(tokens[2]);
				}
			} else if (tokens[0].equals("t") || tokens[0].equals("talk")) {
				if (tokens.length < 3)
					System.out.println("O comando talk e usado na forma \"talk <user> <message>\"");
				else {
					StringBuilder sb = new StringBuilder();
					for (int i = 2; i < tokens.length; i++) {
						sb.append(tokens[i] + " ");
					}
					out.writeUTF("t");
					out.writeUTF(tokens[1]);
					out.writeUTF(sb.toString());
				}
			} else if (tokens[0].equals("r") || tokens[0].equals("read")) {
				if (tokens.length != 1)
					System.out.println("O comando read e usado na forma \"read \"");
				else {
					out.writeUTF("r");
				}
			} else if (tokens[0].equals("exit")) {
				System.out.println("A encerrar programa...");
				out.writeUTF("exit");
				exit = true;
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

			if (!exit) {
				System.out.println(in.readUTF());
			}
			if (image) {
				long fileSize = in.readLong(); // ler tamanho da imagem
				int bytesRead;
				long totalBytesRead = 0;
				File dir = new File(name);
				if (!dir.exists())
					dir.mkdir();
				File img = new File(name + "//" + in.readUTF());
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
