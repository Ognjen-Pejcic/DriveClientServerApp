package clienthandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

import korisnici.Korisnik;
import main.ServerMain;




public class ClientHandler extends Thread {

	//deklaracija promenljivih
	BufferedReader clientInput = null;
	PrintStream clientOutput = null;
	Socket socketZaKomunikaciju = null;
	String username;
	String password;
	String opcija;
	boolean premium;
	LinkedList<Korisnik> korisnici= new LinkedList<>();
	
	
	//konstruktor
	public ClientHandler(Socket socketZaKomunikaciju) {
		this.socketZaKomunikaciju = socketZaKomunikaciju;
	}
	public void napraviDirektorijum(String username) {
		String putanja = new File("").getAbsolutePath();
		putanja=putanja.concat("\\korisnici\\").concat(username);
		File direktorijum = new File(putanja);
		if(!direktorijum.exists()) {
			direktorijum.mkdir();
		}
	}
	public int loginMeni() {
		int opcijaint=0;
		try {
			clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
			
			boolean validan = false;
			
			//login meni
			do {
				try {
				clientOutput.println("Odaberite: \n1. Registracija\n2. Login\n3. Pristup deljenom direktorijumu preko linka\n0. Izlaz");
				opcija = clientInput.readLine();
				opcijaint = Integer.parseInt(opcija);
				if(opcijaint==1 || opcijaint==2||opcijaint==0){
					validan=true;
				}else clientOutput.println("Pogresan unos, taj broj ne postoji kao opcija");
				}catch (NumberFormatException e) {
					System.out.println("Pogresan unos, unesite broj!");
				}
			}while(validan!=true);
			return opcijaint;
		} catch (IOException e) {
			return opcijaint;
		}
		
	}
	public int meniKorisnika(String username) {
		int opcijaint=0;
		try {
			clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
			clientOutput.println("Dobrodosli " + username);
			boolean validan = false;
			
			//login meni
			do {
				try {
				clientOutput.println("Odaberite: \n1. Prikaz liste datotkea\n2. Otvaranje datoteke\n3. Upload daoteke"
						+ "\n4. Deljenje diska\n5. Generisanje link za deljenje\n6. Upravljanje folderima\n0. Logout");
				opcija = clientInput.readLine();
				opcijaint = Integer.parseInt(opcija);
				if(opcijaint==1 || opcijaint==2||opcijaint==0|| opcijaint==3|| opcijaint==4|| opcijaint==5|| opcijaint==6){
					validan=true;
				}else clientOutput.println("Pogresan unos, taj broj ne postoji kao opcija");
				}catch (NumberFormatException e) {
					System.out.println("Pogresan unos, unesite broj!");
				}
			}while(validan!=true);
			return opcijaint;
		} catch (IOException e) {
			return opcijaint;
		}
	}
	public String login() {
		try {
			clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
			FileReader fr= new FileReader("korisnici.txt");
			BufferedReader citanjeIzFajla = new BufferedReader(fr);

			int a=1;
			int b=0;
			boolean validan =false;
			do {
				clientOutput.println("Unesi username: ");
				username = clientInput.readLine();
				String red;
				while((red=citanjeIzFajla.readLine())!=null) {
					
					if(a%3==1 && red.equals(username)) {
						b=1;
						password=citanjeIzFajla.readLine();
						do {
							clientOutput.println("Unesi password: ");
							String passwordProvera=clientInput.readLine();
							if(password.equals(passwordProvera)) {
								validan=true;
							}
						}while(validan!=true);
					}
					if(validan==true)break;
					a++;
				}
				
				if(b==0)clientOutput.println("Username ne postoji!");
				citanjeIzFajla.close();
				if(validan==true)break;
			}while(true);
			return username;
		} catch (IOException e) {
			return null;
		}
		
	}
	public void registracija() {
		boolean validan = false;
		int opcijaint=0;
		try {
			PrintWriter upisUfajl = new PrintWriter(new FileWriter("korisnici.txt",true));
			FileReader fr= new FileReader("korisnici.txt");
			BufferedReader citanjeIzFajla = new BufferedReader(fr); 
			clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
			
			
			validan=true;
			int a=1;
			
			do {
			clientOutput.println("Unesi username: ");
			username = clientInput.readLine();
			String red;
			while((red=citanjeIzFajla.readLine())!=null) {
				validan=true;
				if(a%3==1 && red.equals(username)) {
					clientOutput.println("Korisnik vec postoji, pokusajte ponovo!");
					validan=false;
					break;
				}
				a++;
			}
			
			}while(validan!=true);
			upisUfajl.println(username);
		//	upisUfajl.flush();
			clientOutput.println("Unesi zeljeni password: ");
			password = clientInput.readLine();
			upisUfajl.println(password);
			validan=false;
			String tekst;
			do{
				clientOutput.println("Da li zelite da budete premium korisnik?(Unesite da/ne)");
				 tekst = clientInput.readLine();
				if(tekst.equals("da")) {
					premium=true;
					validan=true;
					}
				else if(tekst.equals("ne")) {
					premium=false;
					validan=true;
				}
				else
					clientOutput.println("Pogresan unos");
			}while(validan!=true);
			upisUfajl.println(tekst);
		//	korisnici.add(new Korisnik(username, premium));
			upisUfajl.close();
			citanjeIzFajla.close();
			napraviDirektorijum(username);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	public void listanjeDirektorijuma(String username) {
		try {
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
			String putanja = new File("").getAbsolutePath();
			putanja=putanja.concat("\\korisnici\\").concat(username);
			File folder = new File(putanja);
			File[] listaFoldera = folder.listFiles();
			
			for (File file : listaFoldera) {
				if(file.isFile())
					clientOutput.println("Fajl: " + file.getName());
				else if(file.isDirectory())
					clientOutput.println("Folder: " + file.getName());
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		
		try {
			PrintWriter upisUfajl = new PrintWriter(new FileWriter("korisnici.txt",true));
			FileReader fr= new FileReader("korisnici.txt");
			BufferedReader citanjeIzFajla = new BufferedReader(fr); 
			clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
			
			boolean validan = false;
			int opcijaint=0;

			opcijaint=loginMeni();
			if(opcijaint==0) {
				clientOutput.println("Potvrdi izlaz unosom ***quit");
			}
			else if(opcijaint==1) {//sign up
				registracija();
			}
			else if(opcijaint==2) {
				String korisnik=login();
				int opcija2=meniKorisnika(korisnik);
				if(opcija2==1)listanjeDirektorijuma(korisnik);
			}
			
			else {
				
			}
			clientOutput.println("Upisi ***quit za izlaz");
			String message;
			while (true) {
				message = clientInput.readLine();

				if (message.startsWith("***quit")) {
					break;
				}
			}
				clientOutput.println(">>> Dovidjenja ");
			ServerMain.onlineKorisnici.remove(this);
			socketZaKomunikaciju.close();
		} catch (IOException e) {
			ServerMain.onlineKorisnici.remove(this);
		} catch (NullPointerException e) {
		System.out.println("Resi ovaj exception nekad");
		}
	}
}