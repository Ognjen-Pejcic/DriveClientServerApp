
public class Main {

	public static void main(String[] args) {
		 final String secretKey = "AA42609D0D34D400FC671123C63715CC";
	     
		    String originalString = "howtodoinjava.com";
		    String encryptedString = AES.encrypt(originalString, secretKey) ;
		    String decryptedString = AES.decrypt(encryptedString, secretKey) ;
		     
		    System.out.println(originalString);
		    System.out.println(encryptedString);
		    System.out.println(decryptedString);
	}

}
