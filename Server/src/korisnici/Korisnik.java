package korisnici;

import java.util.LinkedList;

public class Korisnik {

	private String username;
	private boolean premium;
	private String linkOdDirektorijuma;
	private LinkedList<String> usernameShareKorisnika;
	private LinkedList<String> shareDirektorijum;
	
	public Korisnik(String username, boolean premium) {
		super();
		this.username = username;
		this.premium = premium;
	}
	public Korisnik() {
		
	}
	
	
}
