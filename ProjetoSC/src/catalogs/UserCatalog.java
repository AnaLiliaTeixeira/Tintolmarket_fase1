package catalogs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import entities.User;
import exceptions.WrongCredentialsException;

/**

	A classe UserCatalog é responsável por gerir o catálogo de utilizadores. Esta classe permite criar um novo utilizador, adicionar um utilizador e fazer login de um utilizador.
*/
public class UserCatalog {

	// Private static instance variable of the class
	private static UserCatalog instance;
	private List<User> users;

/**

	Construtor privado da classe UserCatalog.
*/
	private UserCatalog() {
		users = new ArrayList<>();
		File userInfo = new File("userCatalog.txt");
		try {
			if (!userInfo.exists())
				userInfo.createNewFile();
			else
				getUsersByTextFile(userInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/**

	Retorna uma instância do catálogo de utilizadores.
	@return uma instância do catálogo de utilizadores.
*/
	public static UserCatalog getInstance() {
		if (instance == null) {
			instance = new UserCatalog();
		}
		return instance;
	}

/**

	Efetua o login do utilizador ou cria um novo utilizador.
	@param in  ObjectInputStream para ler dados do servidor.
	@param out ObjectOutputStream para enviar dados para o servidor.
	@return o nome de utilizador se o login for bem-sucedido, ou null caso contrário.
	@throws IOException Se ocorrer um erro ao ler ou escrever.
	@throws ClassNotFoundException Se a classe não for encontrada.
	@throws WrongCredentialsException Se as credenciais estiverem incorretas.
*/
	public String login(ObjectInputStream in, ObjectOutputStream out) throws ClassNotFoundException, IOException, WrongCredentialsException {

		File users = new File("userCreds.txt");
		users.createNewFile();
		Scanner sc = new Scanner(users);

		// le user e pass da socket
		String user = (String) in.readObject();
		String password = (String) in.readObject();

		// verifica se user existe e pass esta correta
		boolean newUser = true;
		String line;
		while (sc.hasNextLine()) {
			line = sc.nextLine();
			if (line.startsWith(user) && line.endsWith(password)) { // se for a pass certa
				newUser = false;
				break;
			} else if (line.startsWith(user)) { // se for a pass errada
				sc.close();
				throw new WrongCredentialsException("Credenciais incorretas.");
			}
		}
		sc.close();

		// se o user nao existir faz o seu registo
		if (newUser) {
			this.addUser(user);
			FileWriter fw = new FileWriter("userCreds.txt", true);
			fw.write(user + ":" + password + "\n");
			fw.close();
		}

		return user;
	}

/**

	Lê e armazena os utilizadores do ficheiro de texto userInfo.
	@param userInfo O arquivo de texto com as informações dos utilizadores.
*/
	private void getUsersByTextFile(File userInfo) {
		try {
			Scanner sc = new Scanner(userInfo);
			while (sc.hasNextLine()) {
				String[] line = sc.nextLine().split("(?!\\{.*)\\s(?![^{]*?\\})");
				users.add(new User(line[0], Double.parseDouble(line[1]), stringToHashMap(line[2])));
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/**

	Retorna um objeto User com base no nome do utilizador.
	@param userName O nome do utilizador.
	@return Um objeto User correspondente ao nome do utilizador.
*/
	public User getUserByName(String userName) {
		for (User u : this.users)
			if (u.getName().equals(userName))
				return u;
		return null;
	}

/**

	Adiciona um novo utilizador à lista de utilizadores e ao arquivo de texto.
	@param userName O nome do novo utilizador.
*/
	public void addUser(String userName) {
		try {
			User u = new User(userName, 200, new HashMap<>());
			this.users.add(u);
			File userInfo = new File("userCatalog.txt");
			FileWriter fw = new FileWriter(userInfo, true);
			fw.write(u.toString() + "\r\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

/**

	Converte uma string em um HashMap de strings e listas de strings.
	@param line A string a ser convertida.
	@return Um HashMap com strings como chaves e listas de strings como valores.
*/
	public HashMap<String, List<String>> stringToHashMap(String line) {
		HashMap<String, List<String>> result = new HashMap<>();
		line = line.substring(1, line.length() - 1);
		String[] hashContents = line.split("(?!\\[.*), (?![^\\[]*?\\])");
		if (hashContents[0].contains("=")) {
			for (String s : hashContents) {
				String[] item = s.split("=");
				item[1] = item[1].substring(1, item[1].length() - 1);
				List<String> value = Arrays.asList(item[1].split(", "));
				result.put(item[0], value);
			}
		}
		return result;
	}

}
