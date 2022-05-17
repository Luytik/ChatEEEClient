package ua.kiev.prog;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Scanner;


public class Main {
	public static void main(String[] args) {
		int checkAccount;
		Scanner scanner = new Scanner(System.in);
		String login;
		Account acc;
		try {
			System.out.println("Enter your login: ");
			login = scanner.nextLine();
			acc = new Account(login);
			checkAccount = acc.send();

			while (checkAccount != 200){
				System.out.println("This account is already exist");
				System.out.println("Enter your login: ");
				login = scanner.nextLine();
				acc = new Account(login);
				checkAccount = acc.send();
			}
	
			Thread th = new Thread(new GetThread(login));
			th.setDaemon(true);
			th.start();


			while (true) {
				System.out.println("Enter your message: ");
				String text = scanner.nextLine();
				if (text.isEmpty()) break;
				if(text.equals("-users")){
					System.out.println(Utils.getUsersList());
					continue;
				}
				if(text.equals("-changeStatus")){
					System.out.println("Input your status: ");
					String status = scanner.nextLine();
					acc.setStatus(status);
					acc.changeStatus();
					continue;
				}

				if(text.startsWith("-file")) {
					String[] fileLink = text.split(" ");
					File file = new File(fileLink[1]);
					if(file.exists()){
						byte[] arrayByte;
						arrayByte = Files.readAllBytes(file.toPath());
						String s = Base64.getEncoder().encodeToString(arrayByte);
						StringBuilder sb = new StringBuilder();
						sb.append("-file" + " " + file.getName() + " " + s);
						text = sb.toString();
					} else {
						System.out.println("Link to the file s incorrected");
						continue;
					}
				}

				System.out.println("Your message will be sent to: ");
				String to = scanner.nextLine();
				if (text.isEmpty()) break;

				Message m = new Message(login, text, to);
				int res = m.send(Utils.getURL() + "/add");

				if (res != 200) { // 200 OK
					System.out.println("HTTP error occured: " + res);
					return;
				}
			}
			acc.exitAccount();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			scanner.close();
		}
	}
}
