package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMain implements Runnable {

	static Socket socketZaKomunikaciju = null;
	static BufferedReader serverInput = null;
	static PrintStream serverOutput = null;
	static BufferedReader unosSaTastature = null;

	public static void main(String[] args) {
		try {
			socketZaKomunikaciju = new Socket("localhost", 10000);

			serverInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
			serverOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());

			unosSaTastature = new BufferedReader(new InputStreamReader(System.in));

			new Thread(new ClientMain()).start();

			String input;
			while (true) {
				input = serverInput.readLine();
				// u nasoj konzoli se ispisuje sta smo dobili od servera
				if (input.startsWith(">>> Dovidjenja"))
					break;
				System.out.println(input);
			}
			socketZaKomunikaciju.close();
		} catch (UnknownHostException e) {
			System.out.println("UNKNOWN HOST");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("SERVER IS DOWN");
		}

	}

	@Override
	public void run() {
		String message;

		while (true) {
			try {
				message = unosSaTastature.readLine();
				serverOutput.println(message);
				if (message.startsWith("***quit")) {
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
