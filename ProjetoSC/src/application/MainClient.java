package application;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {

	private static Socket socket;
//	private static ObjectInputStream in;
//	private static ObjectOutputStream out;

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
			out.writeObject(args[1]);
			out.writeObject(args[2]);

			// interagir com o server
			interact(in, out);

			// fechar ligacoes
			in.close();
			out.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void interact(ObjectInputStream in, ObjectOutputStream out) throws Exception {
		System.out.println("add <wine> <image> - adiciona um novo vinho identificado por wine, associado a imagem\r\n"
				+ "image. Caso ja exista um vinho com o mesmo nome deve ser devolvido um erro.\r\n"
				+ "Inicialmente o vinho nao tera qualquer classificacao e o numero de unidades disponiveis\r\n"
				+ "sera zero.\r\n"
				+ "sell <wine> <value> <quantity> - coloca a venda o numero indicado por quantity de\r\n"
				+ "unidades do vinho wine pelo valor value. Caso o wine nao exista, deve ser devolvido um\r\n"
				+ "erro.\r\n" + "view <wine> - obtem as informacoes associadas ao vinho identificado por wine,\r\n"
				+ "nomeadamente a imagem associada, a classificacao media e, caso existam unidades do\r\n"
				+ "vinho disponiveis para venda, a indicacao do utilizador que as disponibiliza, o preco e a\r\n"
				+ "quantidade disponivel. Caso o vinho wine nao exista, deve ser devolvido um erro.\r\n"
				+ "buy <wine> <seller> <quantity> - compra quantity unidades do vinho wine ao utilizador\r\n"
				+ "seller. O numero de unidades deve ser removido da quantidade disponivel e deve ser\r\n"
				+ "transferido o valor correspondente a compra da conta do comprador para o vendedor.\r\n"
				+ "Caso o vinho nao exista, ou nao existam unidades suficientes, ou o comprador nao tenha\r\n"
				+ "saldo suficiente, devera ser devolvido e assinalado o erro correspondente.\r\n"
				+ "wallet - obtem o saldo atual da carteira.\r\n"
				+ "classify <wine> <stars> - atribui ao vinho wine uma classificacao de 1 a 5, indicada por stars.\r\n"
				+ "Caso o vinho wine nao exista, deve ser devolvido um erro.\r\n"
				+ "talk <user> <message> - permite enviar uma mensagem privada ao utilizador user (por\r\n"
				+ "exemplo, uma pergunta relativa a um vinho a venda). Caso o utilizador nao exista, deve\r\n"
				+ "ser devolvido um erro.\r\n" + "3\r\n"
				+ "read - permite ler as novas mensagens recebidas. Deve ser apresentada a identificacao do\r\n"
				+ "remetente e a respetiva mensagem. As mensagens sao removidas da caixa de mensagens\r\n"
				+ "do servidor depois de serem lidas.");

		Scanner sc = new Scanner(System.in);
		while (true) {
			String line = sc.nextLine();
			String[] tokens = line.split(" ");
			if (tokens[0].equals("a") || tokens[0].equals("add")) {
				out.writeObject("a");
				out.writeObject(tokens[1]);
				out.writeObject(new File(tokens[2]));
			} else if (tokens[0].equals("s") || tokens[0].equals("sell")) {
				out.writeObject("s");
				out.writeObject(tokens[1]);
				out.writeObject(tokens[2]);
				out.writeObject(tokens[3]);
			} else if (tokens[0].equals("v") || tokens[0].equals("view")) {
				out.writeObject("v");
				out.writeObject(tokens[1]);
			} else if (tokens[0].equals("b") || tokens[0].equals("buy")) {
				out.writeObject("b");
				out.writeObject(tokens[1]);
				out.writeObject(tokens[2]);
				out.writeObject(tokens[3]);
			} else if (tokens[0].equals("w") || tokens[0].equals("wallet")) {
				out.writeObject("w");
			} else if (tokens[0].equals("c") || tokens[0].equals("classify")) {
				out.writeObject("c");
				out.writeObject(tokens[1]);
				out.writeObject(tokens[2]);
			} else if (tokens[0].equals("t") || tokens[0].equals("talk")) {
				out.writeObject("t");
				out.writeObject(tokens[1]);
				out.writeObject(tokens[2]);
			} else if (tokens[0].equals("r") || tokens[0].equals("read")) {
				out.writeObject("r");
			} else {
				out.writeObject("exit");
				break;
			}

			System.out.println((String) in.readObject());
		}
		sc.close();
	}
}
