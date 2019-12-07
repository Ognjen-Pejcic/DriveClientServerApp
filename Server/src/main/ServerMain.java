package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import clienthandler.ClientHandler;

public class ServerMain {

	public static LinkedList<ClientHandler> onlineKorisnici = new LinkedList<>();

	
	public static void main(String[] args) {
		Socket socketZaKomunikaciju = null;
		ServerSocket serverSocket = null;
		int port= 10000;
		
		try {
			serverSocket=new ServerSocket(port);
			
			while(true) {
				System.out.println("Cekam na komunikaciju...");
				socketZaKomunikaciju = serverSocket.accept();
				System.out.println("Doslo je do konekcije!");
				
				ClientHandler klijent = new ClientHandler(socketZaKomunikaciju);
				
				onlineKorisnici.add(klijent);
			
				klijent.start();
				
			}
		} catch (Exception e) {
			System.out.println("Greska prilikom pokretanja servera");
		}
	}

}
