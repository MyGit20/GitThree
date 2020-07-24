package LoginApp;

import java.util.HashMap;
import java.util.Scanner;

import org.jasypt.util.text.BasicTextEncryptor;
import java.io.*;

//package org.arpit.javapostsForLearning;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {

	private static HashMap<String, String> userPassKeyPairs = new HashMap<String, String>();
	private static BasicTextEncryptor cryptor = new BasicTextEncryptor();

	private String userName;
	private String password;
	int count = 0; int i = 0;
	// read data
	public static void readFromFile() throws IOException {
		int count = 0;
		File file = new File("C:\\Users\\raj_m\\Desktop\\Encrypt.csv");

		try (Scanner myReader = new Scanner(file)) {
			while (myReader.hasNextLine()) {
				String line = myReader.nextLine();
				String[] userDetails = line.split(","); // reading csv (comma separated values file)
				
				String userName = userDetails[0];
				String encryptedPass = userDetails[1];
				userPassKeyPairs.put(userName, encryptedPass);
				System.out.println(line);
				count++;
				System.out.println("No of Rows = " + (count));
			}
		} finally {
			
		}
	}
	static {
		try {
			readFromFile();
			cryptor.setPassword("cryptorPassword");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String execute() {
		return SUCCESS;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void validate() {
		String userName = null;
		if (getUserName().length() == 0) {
			addFieldError("userName", "UserName.required");
		} else if (!userPassKeyPairs.containsKey(getUserName())) {
			addFieldError("userName", "Invalid User");
		} else {

			// this is a valid user
			userName = getUserName();

			System.out.println(userName);

			if (getPassword().length() == 0) {
				addFieldError("password", getText("password.required"));

			} else if (cryptor.decrypt(userPassKeyPairs.get(userName)).equals(getPassword()) == false) {
				addFieldError("password", "Invalid Password");
			}
		}
	}
}