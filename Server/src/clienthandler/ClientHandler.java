package clienthandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;
import java.util.LinkedList;
import main.ServerMain;

import korisnici.Korisnik;
//import main.ServerMain;
import enkripcijaDekripcija.AES;

public class ClientHandler extends Thread {

	// deklaracija promenljivih
	BufferedReader clientInput = null;
	PrintStream clientOutput = null;
	Socket socketZaKomunikaciju = null;
	String username;
	String status;
	String password;
	String opcija;
	String premium;
	LinkedList<Korisnik> korisnici = new LinkedList<>();
	final String enkripcionaSifra = "AA42609D0D34D400FC671123C63715CC";
	// konstruktor
	public ClientHandler(Socket socketZaKomunikaciju) {
		this.socketZaKomunikaciju = socketZaKomunikaciju;
	}

	public void napraviDirektorijum(String username) {
		String putanja = new File("").getAbsolutePath();
		putanja = putanja.concat("\\korisnici\\").concat(username);
		File direktorijum = new File(putanja);
		if (!direktorijum.exists()) {
			direktorijum.mkdir();
		}
	}
	public boolean daLijeLogovan(LinkedList<ClientHandler> l, String ime) {
		l=ServerMain.onlineKorisnici;
//		for (ClientHandler clientHandler : l) {
//			clientOutput.println(clientHandler.username);
//		}
		for (ClientHandler clientHandler : l) {
			if(clientHandler.username!=null && clientHandler.username.equals(ime)) {
				return true;
			}
		}
		return false;
	}
	public int loginMeni() {
		int opcijaint = 0;
		try {
			clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());

			boolean validan = false;
			do {
				try {
					clientOutput.println(
							"Odaberite: \n1. Registracija\n2. Login\n3. Pristup deljenom direktorijumu preko linka\n0. Izlaz");
					opcija = clientInput.readLine();
					opcijaint = Integer.parseInt(opcija);
					if (opcijaint == 1 || opcijaint == 2 || opcijaint == 3 || opcijaint == 0) {
						validan = true;
					} else
						clientOutput.println("Pogresan unos, taj broj ne postoji kao opcija");
				} catch (NumberFormatException e) {
					System.out.println("Pogresan unos, unesite broj!");
				}
			} while (validan != true);
			return opcijaint;
		} catch (IOException e) {

		}
		return 0;
	}

	public int meniKorisnika(String username) {
		int opcijaint = 0;
		try {
			clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
//			clientOutput.println("Dobrodosli " + username);
			boolean validan = false;

			// login meni
			do {
				try {
					clientOutput
							.println("Odaberite: \n1. Prikaz liste datotkea\n2. Otvaranje datoteke\n3. Upload daoteke"
									+ "\n4. Dodolite pristup korisniku\n5. Generisanje link za deljenje\n6. Pregled podeljenih direktorijuma\n7. Upravljanje folderima\n8. Download datoteke\n0. Logout");
					opcija = clientInput.readLine();
					opcijaint = Integer.parseInt(opcija);
					if (opcijaint == 1 || opcijaint == 2 || opcijaint == 0 || opcijaint == 3 || opcijaint == 4
							|| opcijaint == 5 || opcijaint == 6 || opcijaint == 7 || opcijaint == 8) {
						validan = true;
					} else
						clientOutput.println("Pogresan unos, taj broj ne postoji kao opcija");
				} catch (NumberFormatException e) {
					System.out.println("Pogresan unos, unesite broj!");
				}
			} while (validan != true);
			return opcijaint;
		} catch (IOException e) {
			return opcijaint;
		}
	}

	public int meniNePremiumKorisnika(String username) {
		int opcijaint = 0;
		try {
			clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
//			clientOutput.println("Dobrodosli " + username);
			boolean validan = false;

			// login meni
			do {
				try {
					clientOutput
							.println("Odaberite: \n1. Prikaz liste datotkea\n2. Otvaranje datoteke\n3. Upload daoteke"
									+ "\n4. Dodolite pristup korisniku\n5. Generisanje link za deljenje\n6. Pregled podeljenih direktorijuma\n7. Download datoteke\n0. Logout");
					opcija = clientInput.readLine();
					opcijaint = Integer.parseInt(opcija);
					if (opcijaint == 1 || opcijaint == 2 || opcijaint == 0 || opcijaint == 3 || opcijaint == 4
							|| opcijaint == 5 || opcijaint == 6 || opcijaint == 7) {
						validan = true;
					} else
						clientOutput.println("Pogresan unos, taj broj ne postoji kao opcija");
				} catch (NumberFormatException e) {
					System.out.println("Pogresan unos, unesite broj!");
				}
			} while (validan != true);
			return opcijaint;
		} catch (IOException e) {
			return opcijaint;
		}
	}

	public String login() {
		try {
			clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());

//			int a = 1;
			int b = 0;
			boolean validan = false;
			do {
				FileReader fr = new FileReader("korisnici.txt");
				BufferedReader citanjeIzFajla = new BufferedReader(fr);
				clientOutput.println("Unesi username: ");
				String s = clientInput.readLine();
				String red;
				while ((red = citanjeIzFajla.readLine()) != null) {
					String[] tekst = red.split("_");
					if (tekst[0].equals(s)) {
						if(daLijeLogovan(ServerMain.onlineKorisnici, tekst[0])) {
							b=1;
							clientOutput.println("Vec ste ulogovani");
						}
//						if(b==1) {
//							clientOutput.println("Vec ste ulogovani");
//							citanjeIzFajla.close();
//							return null;
//						}
						else {
						do {
							username=s;
							clientOutput.println("Unesi password: ");
							String passwordProvera = clientInput.readLine();
						//	if ((AES.decrypt(tekst[1], enkripcionaSifra)).equals(passwordProvera)) {
								if(tekst[1].equals(passwordProvera)) {
							validan = true;
								premium = tekst[2];
							} else {
								clientOutput.println("Pogresan password");
							}
						} while (validan != true);
						}
					}
					if (validan == true)
						break;
				}
				
				if (b!=1 &&validan == false) {
					clientOutput.println("Uneti username ne postoji!");
					}
				citanjeIzFajla.close();
			} while (validan != true);

			
			if ( validan == false)
				clientOutput.println("Username ne postoji!");

			return username;
		} catch (IOException e) {
			
		} catch (Exception e) {
			clientOutput.println("Greska prilikom dekpricije");
		}
		return null;
	}

	public void registracija() {
		boolean validan = false;

		try {
			PrintWriter upisUfajl = new PrintWriter(new FileWriter("korisnici.txt", true));
			FileReader fr = new FileReader("korisnici.txt");
			BufferedReader citanjeIzFajla = new BufferedReader(fr);
			clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
			LinkedList<String> korisnici = generisiKorisnike();

//			int a = 1;

			do {
				validan = true;
				clientOutput.println("Unesi username: ");
				username = clientInput.readLine();
//			String red;
				for (String korisnik : korisnici) {
					if (korisnik.split("_")[0].equals(username))
						validan = false;
				}
//			while((red=citanjeIzFajla.readLine())!=null) {
//				validan=true;
//				if(a%3==1 && red.equals(username)) {
//					clientOutput.println("Korisnik vec postoji, pokusajte ponovo!");
//					validan=false;
//					break;
//				}
//				a++;
//			}
				if (validan == false)
					clientOutput.println("Uneto korisnicko ime vec postoji");
			} while (validan != true);
//			upisUfajl.println(username);
			// upisUfajl.flush();
			clientOutput.println("Unesi zeljeni password: ");
			password = clientInput.readLine();
			//password = AES.encrypt(password, enkripcionaSifra);
//			upisUfajl.println(password);
			validan = false;

			do {
				clientOutput.println("Da li zelite da budete premium korisnik?(Unesite da/ne)");
				premium = clientInput.readLine();
				if (premium.equals("da")) {
					validan = true;
				} else if (premium.equals("ne")) {
					validan = true;
				} else
					clientOutput.println("Pogresan unos");
			} while (validan != true);
//			upisUfajl.println(tekst);
			// korisnici.add(new Korisnik(username, premium));
//			clientOutput.println("Da li zelite da budete premium korisnik?(Unesite da/ne)");
//			premium = clientInput.readLine();
			napraviDirektorijum(username);
			String link = new File("").getAbsolutePath().concat("\\korisnici\\" + username);
			link=AES.encrypt(link, enkripcionaSifra);
			String tekst = username + "_" + password + "_" + premium + "_" + link + "_";
			upisUfajl.println(tekst);
			upisUfajl.close();
			citanjeIzFajla.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			clientOutput.println("Greska prilikom enkripcije");
		}

	}

//	public String procitajRegistrovane() {
//		String registrovani = "";
//		try {
//			BufferedReader bReader = new BufferedReader(new FileReader("registrovani.txt"));
//			boolean kraj = false;
//
//			while (kraj == false) {
//				String pom = bReader.readLine();
//				if (pom == null) {
//					kraj = true;
//				} else {
//					registrovani = registrovani + pom + "\n";
//				}
//			}
//			bReader.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return registrovani;
//	}

//	private void registracija2() throws IOException {
//		clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
//		clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
//		String registrovani = procitajRegistrovane();
//		try {
//
//			boolean validnoIme = false;
//			while (validnoIme == false) {
//				clientOutput.println("Unesite korisnicko ime.");
//
//				String ime = clientInput.readLine();
//
//				if (ime.startsWith("menu")) {
//					return;
//				}
//				boolean jedinstvenoIme = false;
//				if (registrovani == "") {
//					jedinstvenoIme = true;
//				} else {
//					jedinstvenoIme = daLiJeJedinstveno(ime);
//				}
//
//				if (jedinstvenoIme == false) {
//					clientOutput.println("Korisnicko ime nije jedinstveno");
//				} else {
//					this.username = ime;
//					String usernameForClient = "" + this.username + "";
//					this.clientOutput.println(usernameForClient);
//					validnoIme = true;
//				}
//			}
//
//			clientOutput.println("Unesite sifru.");
//			String sifra = clientInput.readLine();
//			if (sifra.startsWith("menu")) {
//				return;
//			}
//			this.password = sifra;
//
//			while (true) {
//				clientOutput.println("Za status obicnog korisnika unesite 1, a za status premium korisnika unesite 2");
//				String unos = clientInput.readLine();
//				if (unos.equals("1")) {
//					this.status = "obican";
//					break;
//				} else if (unos.equals("2")) {
//					this.status = "premium";
//					break;
//				} else {
//					clientOutput.println("***Neprihvatljiv unos");
//				}
//			}
//
//			clientOutput.println(username + " _ " + password + " _ " + status + " _ " + " _ " + "\n");
//			clientOutput.flush();
//			clientOutput.close();
//			clientOutput.println("Uspjesno ste se registrovali");
//			napraviDirektorijum(this.username);
//		} catch (IOException e) {
//			System.out.println("Doslo je do greske");
//		}
//	}

//	private boolean daLiJeJedinstveno(String ime) {
//		try {
//			BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("registrovani.txt")));
//			LinkedList<String> userInfos = new LinkedList<String>();
//			String info = "";
//			while ((info = bufferedReader.readLine()) != null) {
//				userInfos.add(info);
//			}
//
//			for (String userInfo : userInfos) {
//				if (ime.equals(userInfo.split(" : ")[0])) {
//					return false;
//				}
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException r) {
//			r.printStackTrace();
//		}
//		return true;
//	}

	public void recursivePrint(File[] arr, int index, int level) throws IOException {
		clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
		// terminate condition
		if (index == arr.length)
			return;

		// tabs for internal levels
		for (int i = 0; i < level; i++)
			clientOutput.print("\t");

		// for files
		if (arr[index].isFile())
			clientOutput.println(arr[index].getName());

		// for sub-directories
		else if (arr[index].isDirectory()) {
			clientOutput.println("[" + arr[index].getName() + "]");

			// recursion for sub-directories
			recursivePrint(arr[index].listFiles(), 0, level + 1);
		}

		// recursion for main directory
		recursivePrint(arr, ++index, level);
	}

	public File vratiFajlRekurzivno(File[] arr, int index, String nazivFajla) {
		if (index == arr.length) {

			return null;
		}
		if (arr[index].isFile()) {
			if (arr[index].getName().equals(nazivFajla)) {
				return arr[index];
			}
		} else
			return vratiFajlRekurzivno(arr[index].listFiles(), 0, nazivFajla);
		return vratiFajlRekurzivno(arr, ++index, nazivFajla);
	}

	public void listanje(String username) throws IOException {
		clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
		String putanja = new File("").getAbsolutePath();
		putanja = putanja.concat("\\korisnici\\").concat(username);
		File folder = new File(putanja);
		File[] listaFoldera = folder.listFiles();
		recursivePrint(listaFoldera, 0, 0);
	}
	public void listanjePrekoLinka(String link) throws IOException {
		
		File folder = new File(link);
		File[] listaFoldera = folder.listFiles();
		recursivePrint(listaFoldera, 0, 0);
	}

	public File searchFile(File file, String search) {
		if (file.isDirectory()) {
			File[] arr = file.listFiles();
			for (File f : arr) {
				File found = searchFile(f, search);
				if (found != null)
					return found;
			}
		} else {
			if (file.getName().equals(search)) {
				return file;
			}
		}
		return null;
	}

	public File searchFolder(File file, String search) {
		if (file.isDirectory()) {
			if (file.getName().equals(search)) {
				return file;
			}
			File[] arr = file.listFiles();
			for (File f : arr) {
				File found = searchFolder(f, search);
				if (found != null)
					return found;
			}
		} else {
//			if (file.getName().equals(search)) {
//				return file;
//			}
		}
		return null;
	}

	public void listanjeDirektorijuma(String username) {
		try {
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
			String putanja = new File("").getAbsolutePath();
			putanja = putanja.concat("\\korisnici\\").concat(username);
			File folder = new File(putanja);
			File[] listaFoldera = folder.listFiles();

			for (File file : listaFoldera) {
				if (file.isFile())
					clientOutput.println("Fajl: " + file.getName());
				else if (file.isDirectory()) {
					clientOutput.println("Folder: " + file.getName());
					// listanjeDirektorijuma(file.getName());
//					String novaPutanja = new File("").getAbsolutePath();
//					novaPutanja = putanja.concat(file.getName());
//					File folder1 = new File(novaPutanja);
//					for
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public File vratiFajl(String username, String nazivFajla) {
		try {
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
			String putanja = new File("").getAbsolutePath();
			putanja = putanja.concat("\\korisnici\\").concat(username);
			File folder = new File(putanja);
			File[] listaFoldera = folder.listFiles();

			for (File file : listaFoldera) {
				if (file.isFile()) {
					clientOutput.println(file.getName());
					if (file.getName().equals(nazivFajla))
						return file;
				}
			}
			return null;
		} catch (IOException e) {
			return null;
		}

	}

	public void otvoriFajl(File file) throws IOException {
		clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
		if (file == null)
			clientOutput.println("Odabrani fajl ne postoji");
		else {
			if (file.isDirectory()) {
				clientOutput.println("Direktorijum je");
			} else {
				if (file.getName().endsWith(".txt")) {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String tekst;
					while ((tekst = br.readLine()) != null) {
						clientOutput.println(tekst);
					}
					br.close();
				} else {
					String nesto = null;
					try {
						FileInputStream fis = new FileInputStream(file);
						byte[] b = new byte[(int) file.length()];
						fis.read(b);
						nesto = new String(Base64.getEncoder().encode(b), "UTF-8");
						clientOutput.println(nesto);
						fis.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}

				}

			}
		}
	}

	public void upload(File file, String username) throws IOException {

		clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
		clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
		FileInputStream fis = new FileInputStream(file);
		otvoriFajl(file);
		byte b[] = new byte[(int) file.length()];
		fis.read(b, 0, b.length);
		String s = file.getName();
		String putanja = new File("").getAbsolutePath();
		putanja = putanja.concat("\\korisnici\\" + username + "\\" + s);
		File novifajl = new File(putanja);
		FileOutputStream fos = new FileOutputStream(novifajl);
		fos.write(b, 0, b.length);
		fis.close();
		fos.close();
	}

	public void download(File file) throws IOException {

		clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
//		clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
		FileInputStream fis = new FileInputStream(file);
//		otvoriFajl(file);
		byte b[] = new byte[(int) file.length()];
		fis.read(b, 0, b.length);
		String naziv = file.getName();
		String putanja = new File("").getAbsolutePath();
		String[] rastavljenaPutanja = putanja.split("\\\\");
		String novaPutanja = "";
		for (int i = 0; i < rastavljenaPutanja.length - 1; i++) {
			novaPutanja = novaPutanja.concat(rastavljenaPutanja[i] + "\\");
		}
		novaPutanja = novaPutanja.concat("Client\\" + naziv);
		File novifajl = new File(novaPutanja);

//		putanja = putanja.concat("\\korisnici\\" + username + "\\" + s);
//		File novifajl = new File(new File("").getAbsolutePath().);
		FileOutputStream fos = new FileOutputStream(novifajl);
		fos.write(b, 0, b.length);
		fis.close();
		fos.close();
	}

	public LinkedList<String> generisiKorisnike() throws IOException {
		clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
		FileReader fr = new FileReader("korisnici.txt");
		BufferedReader citanjeIzFajla = new BufferedReader(fr);
		LinkedList<String> korisnik = new LinkedList<>();
		String red;
		while ((red = citanjeIzFajla.readLine()) != null) {
			korisnik.add(red);
		}
		citanjeIzFajla.close();
		return korisnik;
	}

//	private String pristupTudjemPrekoLinka(String link) throws Exception {
//		LinkedList<String> korisnici = generisiKorisnike();
//		for (String korisnik : korisnici) {
//			String[] delovi = korisnik.split("_");
//			if (AES.decrypt(link, enkripcionaSifra).equals(delovi[3])) {
//				return delovi[0];
//			}
//		}
//		return null;
//	}

	private void generisLinkZaDeljenje(String user) throws IOException {
		clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
		FileReader fr = new FileReader("korisnici.txt");
		BufferedReader citanjeIzFajla = new BufferedReader(fr);
		LinkedList<String> korisnici = generisiKorisnike();
		for (String korisnik : korisnici) {
			String[] delovi = korisnik.split("_");
			if (delovi[0].equals(user)) {
				clientOutput.println(delovi[3]);
				citanjeIzFajla.close();
				return;
			}
		}
		citanjeIzFajla.close();
	}

	private void dajOvlascenjeKorisniku(String user) throws IOException {

		FileReader fr = new FileReader("korisnici.txt");
		clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
		BufferedReader citanjeIzFajla = new BufferedReader(fr);
		StringBuffer inputBuffer = new StringBuffer();
		String line;
		LinkedList<String> korisnici = generisiKorisnike();
		boolean postoji = false;
		for (String korisnik : korisnici) {
			if (user.equals(korisnik.split("_")[0])) {
				postoji = true;
			}

		}
		if (postoji == false) {
			clientOutput.println("Korisnik ne postoji!");
			citanjeIzFajla.close();
			return;
		}

		while ((line = citanjeIzFajla.readLine()) != null) {
			if (line.startsWith(username))
				line = line + user + " ";
			inputBuffer.append(line);
			inputBuffer.append('\n');

		}
		PrintWriter upisUfajl = new PrintWriter(new FileWriter("korisnici.txt"));
		upisUfajl.write(inputBuffer.toString());
		upisUfajl.close();
		citanjeIzFajla.close();
	}

	private boolean imaPristup(String ovajCijiJefajl, String user) throws IOException {
		LinkedList<String> korisnici = generisiKorisnike();
		clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());

		for (String korisnik : korisnici) {
			if (korisnik != null) {
				String[] delovi = korisnik.split("_");
				if (delovi.length >= 5) {
					String imajuPristup = delovi[4];
					if (imajuPristup != null) {
						if (ovajCijiJefajl.equals(delovi[0]) && imajuPristup.contains(user + " "))
							return true;
					}
				}
			}
		}
		return false;
	}

	private boolean daLiPostojiKorisnik(String user) throws IOException {
		LinkedList<String> korisnici = generisiKorisnike();
		for (String korisnik : korisnici) {
			if (korisnik.split("_")[0].equals(user))
				return true;
		}
		return false;
	}

	public File vratiFolder(String username, String nazivFajla) {
		try {
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
			String putanja = new File("").getAbsolutePath();
			putanja = putanja.concat("\\korisnici\\").concat(username);
			File folder = new File(putanja);
			File[] listaFoldera = folder.listFiles();

			for (File file : listaFoldera) {
				if (file.isDirectory()) {
					clientOutput.println(file.getName());
					if (file.getName().equals(nazivFajla))
						return file;
				}
			}
			return null;
		} catch (IOException e) {
			return null;
		}

	}

	public void prebaci(File file, String putanja) throws IOException {

		clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
		clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
		FileInputStream fis = new FileInputStream(file);
		otvoriFajl(file);
		byte b[] = new byte[(int) file.length()];
		fis.read(b, 0, b.length);
		String s = file.getName();
//		String putanja = new File("").getAbsolutePath();
//		putanja = putanja.concat("\\korisnici\\" + username + "\\" + s);
		putanja = putanja.concat("\\" + s);
		File novifajl = new File(putanja);
		FileOutputStream fos = new FileOutputStream(novifajl);
		fos.write(b, 0, b.length);
		fis.close();
		fos.close();
	}

	public void korisniciCijimDirektorijumimaImaPristup() throws IOException {
		FileReader fr = new FileReader("korisnici.txt");
		clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
		BufferedReader citanjeIzFajla = new BufferedReader(fr);
		LinkedList<String> korisnici = generisiKorisnike();

		for (String korisnik : korisnici) {
			String[] delovi = korisnik.split("_");
			if (delovi[0].equals(username)) {
				String[] oniKojimaImaPristup = delovi[4].split(" ");
				clientOutput.println("Imate pristup direktorijumima sledecih korisnika");
				for (String oni : oniKojimaImaPristup) {
					clientOutput.println(oni);
				}
			}

		}
		citanjeIzFajla.close();
	}

	private int meniUpravljanjaDatotekama() {
		int opcijaint = 0;
		try {
			clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());

			boolean validan = false;
			do {
				try {
					clientOutput.println(
							"Odaberite: \n1. Kreiraj folder\n2. Promeni naziv foldera\n3. Prebaci datoteku iz foldera u drugi folder\n4. Obrisi folder\n0. Izlaz");
					opcija = clientInput.readLine();
					opcijaint = Integer.parseInt(opcija);
					if (opcijaint == 1 || opcijaint == 2 || opcijaint == 3 || opcijaint == 4 || opcijaint == 0) {
						validan = true;
					} else
						clientOutput.println("Pogresan unos, taj broj ne postoji kao opcija");
				} catch (NumberFormatException e) {
					System.out.println("Pogresan unos, unesite broj!");
				}
			} while (validan != true);
			return opcijaint;
		} catch (IOException e) {

		}
		return 0;
	}

	private void napraviDirektorijumNaOdredjenomMestu(String putanja, String nazivFajla) throws IOException {
//		clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());
//		File folder =new File(putanja);
//		if(folder.exists()) {
		File noviFolder = new File(putanja.concat("\\" + nazivFajla));
		noviFolder.mkdir();

//		}else {
//			clientOutput.println("Uneta je putanja koja ne postoji");
//		}
	}

	private void izmeniIme(String putanja, String naziv) {
		File folder = new File(putanja);
//		String[] rastavljenaPutanja = putanja.split("\\\\");
//		String novaPutanja="";
//		for (int i=0;i<rastavljenaPutanja.length-1;i++) {
//			novaPutanja=novaPutanja.concat(rastavljenaPutanja[i]+"\\");
//		}
//		novaPutanja=novaPutanja.concat(naziv);
//		File noviFolder =  new File(putanja.concat(novaPutanja));
//		folder.renameTo(noviFolder);
		File newDir = new File(folder.getParent() + "\\" + naziv);
		folder.renameTo(newDir);
	}

	@Override
	public void run() {

		try {
			clientInput = new BufferedReader(new InputStreamReader(socketZaKomunikaciju.getInputStream()));
			clientOutput = new PrintStream(socketZaKomunikaciju.getOutputStream());

			// boolean validan = false;
			int opcijaint = 0;

			opcijaint = loginMeni();
			if (opcijaint == 0) {
				clientOutput.println("Potvrdi izlaz unosom ***quit");
			} else if (opcijaint == 1) {// sign up
				registracija();
			} else if (opcijaint == 2) {

				String korisnik = login();
				if(korisnik==null) {
					return;
				}
				clientOutput.println("Dobrodosli " + username);
				int opcija2;
				if (premium.equals("da")) {
					do {

						opcija2 = meniKorisnika(korisnik);
						if (opcija2 == 1)
							listanje(korisnik);
						else if (opcija2 == 2) {

							clientOutput.println("Unesi naziv fajla koji zelis da se prikaze: ");
							String naziv = clientInput.readLine();
							String putanja = new File("").getAbsolutePath();
							putanja = putanja.concat("\\korisnici\\").concat(username);
//							File folder = new File(putanja);
//							File[] listaFoldera = folder.listFiles();
							File file = searchFile(new File(putanja), naziv);
							if (file.exists())
								// File file= vratiFajl(korisnik, naziv);
								otvoriFajl(file);
							else
								clientOutput.println("Uneti fajl ne postoji");

						} else if (opcija2 == 3) {
							clientOutput.println("Unesite putanju do datoteke: ");
							String putanja = clientInput.readLine();
							if (new File(putanja).exists())
								upload(new File(putanja), username);
							else
								clientOutput.println("Uneli ste pogresnu putanju");
						} else if (opcija2 == 4) {
							clientOutput.println("Kome zelite da date pristup: ");
							String user = clientInput.readLine();

							dajOvlascenjeKorisniku(user);
						} else if (opcija2 == 5) {
							generisLinkZaDeljenje(username);

						} else if (opcija2 == 6) {
							korisniciCijimDirektorijumimaImaPristup();
							clientOutput.println("Unesi naziv korisnika kojem zelis da pristupis: ");

							String t = clientInput.readLine();
							if (!daLiPostojiKorisnik(t)) {
								clientOutput.println("Uneti korisnik ne postoji");
							} else {
								if (imaPristup(t, username)) {
									listanje(t);
									
									int izbor =0; 
									do {
										clientOutput.println("Unesite:\n1 za pregled datoteke\n2 za download\n0 za povratak");
										izbor= Integer.parseInt(clientInput.readLine());
										if(izbor==0 || izbor==1 ||izbor==2) {
										if(izbor==0) {
											break;
										}
										if(izbor==1) {
											clientOutput.println("Unesi naziv fajla koji zelis da se prikaze: ");
											String naziv = clientInput.readLine();
											String putanja = new File("").getAbsolutePath();
											putanja = putanja.concat("\\korisnici\\").concat(t);
											File file = searchFile(new File(putanja), naziv);
											if (file.exists())
												// File file= vratiFajl(korisnik, naziv);
												otvoriFajl(file);
											else
												clientOutput.println("Uneti fajl ne postoji");

										} 
										if(izbor==2) {
											clientOutput.println("Unesite fajl za download");
											String tekst = clientInput.readLine();
											File f = searchFile(
													new File(new File("").getAbsolutePath().concat("\\korisnici\\" + t)), tekst);
											download(f);
										}
									
										}else
										{
											clientOutput.println("Pogresan unos");
										}
							
									}while(true);
//									clientOutput.println("Unesi naziv fajla koji zelis da se prikaze: ");
//									String naziv = clientInput.readLine();
//									String putanja = new File("").getAbsolutePath();
//									putanja = putanja.concat("\\korisnici\\").concat(t);
//							File folder = new File(putanja);
//							File[] listaFoldera = folder.listFiles();
//									File file = searchFile(new File(putanja), naziv);
//									if (file.exists())
//										// File file= vratiFajl(korisnik, naziv);
//										otvoriFajl(file);
//									else
//										clientOutput.println("Uneti fajl ne postoji");

								} else {
									clientOutput.println("Nemate pristup!");
								}

							}
						} else if (opcija2 == 8) {
							clientOutput.println("Unesite fajl za download");
							String tekst = clientInput.readLine();
//							String putanja = new File("").getAbsolutePath().concat("\\"+username);

//							File fff= new File(putanja);
							File f = searchFile(
									new File(new File("").getAbsolutePath().concat("\\korisnici\\" + username)), tekst);
							download(f);
						} else if (opcija2 == 7) {
							int nekaopcija = 0;
							do {
								nekaopcija = meniUpravljanjaDatotekama();
								if (nekaopcija == 1) {
									String nesto = new File("").getAbsolutePath();
									clientOutput.println(nesto);
									clientOutput.println("Prosledite putanju gde zelite da kreirate nov fajl:");
									String putanja = clientInput.readLine();
									clientOutput.println("Unesite ime novog fajla:");
									String naziv = clientInput.readLine();
									napraviDirektorijumNaOdredjenomMestu(putanja, naziv);
								} else if (nekaopcija == 2) {
									clientOutput.println("Prosledite putanju foldera ciji naziv zelite da promenite:");
									String putanja = clientInput.readLine();
									clientOutput.println("Novo ime:");
									String naziv = clientInput.readLine();
									izmeniIme(putanja, naziv);
								} else if (nekaopcija == 3) {
									clientOutput.println("Unesi naziv fajla koji zelis da premestis: ");
									String naziv = clientInput.readLine();
									String putanja = new File("").getAbsolutePath();
									putanja = putanja.concat("\\korisnici\\").concat(username);
//								File folder = new File(putanja);
//								File[] listaFoldera = folder.listFiles();
									File file = searchFile(new File(putanja), naziv);
									clientOutput.println("Unesite naziv foldera gde zelis da se prebaci: ");
									String nazivnovogfoldera = clientInput.readLine();
									File novFolder = searchFolder(new File(putanja), nazivnovogfoldera);
									String novaputanja = novFolder.getAbsolutePath();
//								String novaputanja = new File("").getAbsolutePath().concat("\\korisnici\\liked\\a");
									prebaci(file, novaputanja);
									if (file.delete())
										clientOutput.println("Resio fajl");
									else
										clientOutput.println("Nisam resio fajl");
								} else if (nekaopcija == 4) {
									clientOutput.println(new File("").getAbsolutePath());
									clientOutput.println("Prosledite putanju foldera koji zelite da izbrisete:");
									String putanja = clientInput.readLine();

									File f = new File(putanja);

									if (f.list().length == 0) {
										f.delete();
									} else
										clientOutput.println("Uneti direktorijum nije prazan!");
								}

							} while (nekaopcija != 0);
						}

						else if (opcija2 == 0) {
							clientOutput.println("Dovidjenja!");
						}
					} while (opcija2 != 0);
				} else {
					do {

						opcija2 = meniNePremiumKorisnika(korisnik);
						if (opcija2 == 1)
							listanje(korisnik);
						else if (opcija2 == 2) {

							clientOutput.println("Unesi naziv fajla koji zelis da se prikaze: ");
							String naziv = clientInput.readLine();
							String putanja = new File("").getAbsolutePath();
							putanja = putanja.concat("\\korisnici\\").concat(username);
//							File folder = new File(putanja);
//							File[] listaFoldera = folder.listFiles();
							File file = searchFile(new File(putanja), naziv);
							if (file.exists())
								// File file= vratiFajl(korisnik, naziv);
								otvoriFajl(file);
							else
								clientOutput.println("Uneti fajl ne postoji");

						} else if (opcija2 == 3) {
							String p = new File("").getAbsolutePath();
							p = p.concat("\\korisnici\\" + username);
							File f = new File(p);
							if (f.list().length < 5) {
								clientOutput.println("Unesite putanju do datoteke: ");
								String putanja = clientInput.readLine();
								if (new File(putanja).exists())
									upload(new File(putanja), username);
								else
									clientOutput.println("Uneli ste pogresnu putanju");
							} else {
								clientOutput.println("Ne mozete da uneste vise od 5 fajlova u direktorijum");
							}
						} else if (opcija2 == 4) {
							clientOutput.println("Kome zelite da date pristup: ");
							String user = clientInput.readLine();

							dajOvlascenjeKorisniku(user);
						} else if (opcija2 == 5) {
							generisLinkZaDeljenje(username);

						} else if (opcija2 == 6) {
							korisniciCijimDirektorijumimaImaPristup();
							clientOutput.println("Unesi naziv korisnika kojem zelis da pristupis: ");

							String t = clientInput.readLine();
							if (!daLiPostojiKorisnik(t)) {
								clientOutput.println("Uneti korisnik ne postoji");
							} else {
								if (imaPristup(t, username)) {
									listanje(t);
									
									int izbor =0; 
									do {
										clientOutput.println("Unesite:\n1 za pregled datoteke\n2 za download\n0 za povratak");
										izbor= Integer.parseInt(clientInput.readLine());
										if(izbor==0 || izbor==1 ||izbor==2) {
										if(izbor==0) {
											break;
										}
										if(izbor==1) {
											clientOutput.println("Unesi naziv fajla koji zelis da se prikaze: ");
											String naziv = clientInput.readLine();
											String putanja = new File("").getAbsolutePath();
											putanja = putanja.concat("\\korisnici\\").concat(t);
											File file = searchFile(new File(putanja), naziv);
											if (file.exists())
												// File file= vratiFajl(korisnik, naziv);
												otvoriFajl(file);
											else
												clientOutput.println("Uneti fajl ne postoji");

										} 
										if(izbor==2) {
											clientOutput.println("Unesite fajl za download");
											String tekst = clientInput.readLine();
											File f = searchFile(
													new File(new File("").getAbsolutePath().concat("\\korisnici\\" + t)), tekst);
											download(f);
										}
									
										}else
										{
											clientOutput.println("Pogresan unos");
										}
							
									}while(true);
//									clientOutput.println("Unesi naziv fajla koji zelis da se prikaze: ");
//									String naziv = clientInput.readLine();
//									String putanja = new File("").getAbsolutePath();
//									putanja = putanja.concat("\\korisnici\\").concat(t);
//							File folder = new File(putanja);
//							File[] listaFoldera = folder.listFiles();
//									File file = searchFile(new File(putanja), naziv);
//									if (file.exists())
//										// File file= vratiFajl(korisnik, naziv);
//										otvoriFajl(file);
//									else
//										clientOutput.println("Uneti fajl ne postoji");

								} else {
									clientOutput.println("Nemate pristup!");
								}

							}
						} else if (opcija2 == 7) {
							clientOutput.println("Unesite fajl za download");
							String tekst = clientInput.readLine();
//					String putanja = new File("").getAbsolutePath().concat("\\"+username);

//					File fff= new File(putanja);
							File f = searchFile(
									new File(new File("").getAbsolutePath().concat("\\korisnici\\" + username)), tekst);
							download(f);
						}

					} while (opcija2 != 0);
				}
			} else if (opcijaint == 3) {
				clientOutput.println("Unesi link: ");
				String link = clientInput.readLine();
				link = AES.decrypt(link, enkripcionaSifra);
				clientOutput.println(link);
				String[] a=link.split("\\\\");
				String user=a[a.length-1];
				listanjePrekoLinka(link);
				int izbor =0; 
				do {
					clientOutput.println("Unesite:\n1 za pregled datoteke\n2 za download\n0 za povratak");
					izbor= Integer.parseInt(clientInput.readLine());
					if(izbor==0 || izbor==1 ||izbor==2) {
					if(izbor==0) {
						break;
					}
					if(izbor==1) {
						clientOutput.println("Unesi naziv fajla koji zelis da se prikaze: ");
						String naziv = clientInput.readLine();
						String putanja = new File("").getAbsolutePath();
						putanja = putanja.concat("\\korisnici\\").concat(user);
						File file = searchFile(new File(putanja), naziv);
						if (file.exists())
							// File file= vratiFajl(korisnik, naziv);
							otvoriFajl(file);
						else
							clientOutput.println("Uneti fajl ne postoji");

					} 
					if(izbor==2) {
						clientOutput.println("Unesite fajl za download");
						String tekst = clientInput.readLine();
						File f = searchFile(
								new File(new File("").getAbsolutePath().concat("\\korisnici\\" + user)), tekst);
						download(f);
					}
				
					}else
					{
						clientOutput.println("Pogresan unos");
					}
			}while(true);
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}