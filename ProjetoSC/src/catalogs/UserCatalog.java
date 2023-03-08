package catalogs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entities.User;

public class UserCatalog {

	// Private static instance variable of the class
    private static UserCatalog instance;
	private List<User> users;
	
	private UserCatalog() {
		users = new ArrayList<>();
		File userInfo = new File("storedFiles\\userCatalog.txt");
		try {
			if(!userInfo.exists())
				userInfo.createNewFile();
			else
				getUsersByTextFile(userInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public static UserCatalog getInstance() {
        if (instance == null) {
            instance = new UserCatalog();
        }
        return instance;
    }
    
    private void getUsersByTextFile(File userInfo) {
		try {
			Scanner myReader = new Scanner(userInfo);
			while(myReader.hasNextLine()) {
				String data = myReader.nextLine();
				users.add(new User(data.split(":")[0]));
			}
			myReader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("An error ocurred.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("An error ocurred.");
			e.printStackTrace();
		}
    }
	
	/**
	 * Faz login do utilizador ou cria um utilizador novo
	 * 
	 * @return o username se login com sucesso ou null, caso contrario
	 * @throws Exception se ocorrer erro ao ler ou escrever
	 */
	public String login(ObjectInputStream in, ObjectOutputStream out) throws Exception {
		
		File users = new File("storedFiles\\userCreds.txt");
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
			FileWriter fw = new FileWriter("storedFiles\\userCreds.txt");
			fw.write(user + ":" + password);
			fw.close();
		}

		return user;
	}
}
