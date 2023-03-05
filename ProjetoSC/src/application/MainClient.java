package application;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {

	private static Socket socket;
	private static ObjectInputStream in;
	private static ObjectOutputStream out;

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
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());

			// enviar user e password
			out.writeObject(args[1]);
			out.writeObject(args[2]);

			// interagir com o server
			interact();

			// fechar ligacoes
			in.close();
			out.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void interact() throws Exception {
		System.out.println("add <wine> <image> - adiciona um novo vinho identificado por wine, associado � imagem\r\n"
				+ "image. Caso j� exista um vinho com o mesmo nome deve ser devolvido um erro.\r\n"
				+ "Inicialmente o vinho n�o ter� qualquer classifica��o e o n�mero de unidades dispon�veis\r\n"
				+ "ser� zero.\r\n"
				+ "sell <wine> <value> <quantity> - coloca � venda o n�mero indicado por quantity de\r\n"
				+ "unidades do vinho wine pelo valor value. Caso o wine n�o exista, deve ser devolvido um\r\n"
				+ "erro.\r\n" + "view <wine> - obt�m as informa��es associadas ao vinho identificado por wine,\r\n"
				+ "nomeadamente a imagem associada, a classifica��o m�dia e, caso existam unidades do\r\n"
				+ "vinho dispon�veis para venda, a indica��o do utilizador que as disponibiliza, o pre�o e a\r\n"
				+ "quantidade dispon�vel. Caso o vinho wine n�o exista, deve ser devolvido um erro.\r\n"
				+ "buy <wine> <seller> <quantity> - compra quantity unidades do vinho wine ao utilizador\r\n"
				+ "seller. O n�mero de unidades deve ser removido da quantidade dispon�vel e deve ser\r\n"
				+ "transferido o valor correspondente � compra da conta do comprador para o vendedor.\r\n"
				+ "Caso o vinho n�o exista, ou n�o existam unidades suficientes, ou o comprador n�o tenha\r\n"
				+ "saldo suficiente, dever� ser devolvido e assinalado o erro correspondente.\r\n"
				+ "wallet - obt�m o saldo atual da carteira.\r\n"
				+ "classify <wine> <stars> - atribui ao vinho wine uma classifica��o de 1 a 5, indicada por stars.\r\n"
				+ "Caso o vinho wine n�o exista, deve ser devolvido um erro.\r\n"
				+ "talk <user> <message> - permite enviar uma mensagem privada ao utilizador user (por\r\n"
				+ "exemplo, uma pergunta relativa a um vinho � venda). Caso o utilizador n�o exista, deve\r\n"
				+ "ser devolvido um erro.\r\n" + "3\r\n"
				+ "read - permite ler as novas mensagens recebidas. Deve ser apresentada a identifica��o do\r\n"
				+ "remetente e a respetiva mensagem. As mensagens s�o removidas da caixa de mensagens\r\n"
				+ "do servidor depois de serem lidas.");

		Scanner sc = new Scanner(System.in);
		while (true) {
			String line = sc.nextLine();
			String[] tokens = line.split(" ");
			if (tokens[0] == "a" || tokens[0] == "add") {
				out.writeObject("a");
				out.writeObject(tokens[1]);
				out.writeObject(new File(tokens[2]));
			} else if (tokens[0] == "s" || tokens[0] == "sell") {
				out.writeObject("s");
				out.writeObject(tokens[1]);
				out.writeObject(tokens[2]);
				out.writeObject(tokens[3]);
			} else if (tokens[0] == "v" || tokens[0] == "view") {
				out.writeObject("v");
				out.writeObject(tokens[1]);
			} else if (tokens[0] == "b" || tokens[0] == "buy") {
				out.writeObject("b");
				out.writeObject(tokens[1]);
				out.writeObject(tokens[2]);
				out.writeObject(tokens[3]);
			} else if (tokens[0] == "w" || tokens[0] == "wallet") {
				out.writeObject("w");
			} else if (tokens[0] == "c" || tokens[0] == "classify") {
				out.writeObject("c");
				out.writeObject(tokens[1]);
				out.writeObject(tokens[2]);
			} else if (tokens[0] == "t" || tokens[0] == "talk") {
				out.writeObject("t");
				out.writeObject(tokens[1]);
				out.writeObject(tokens[2]);
			} else if (tokens[0] == "r" || tokens[0] == "read") {
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
