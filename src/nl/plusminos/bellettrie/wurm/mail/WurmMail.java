package nl.plusminos.bellettrie.wurm.mail;

import nl.plusminos.bellettrie.wurm.exceptions.FacebookWurmException;
import nl.plusminos.bellettrie.wurm.exceptions.MailWurmException;
import nl.plusminos.bellettrie.wurm.exceptions.TwitterWurmException;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class WurmMail {
	private Email email;
	
	public void init() throws MailWurmException {
		email = new SimpleEmail();
		email.setHostName(MailCredentials.hostname);
		email.setSmtpPort(MailCredentials.smtpPort); 
		email.setAuthenticator(new DefaultAuthenticator(MailCredentials.username, MailCredentials.password));

		try {
			email.setFrom(MailCredentials.identity);
		} catch (EmailException e) {
			System.out.println("[WurmMail] Failed to set the from field");
			e.printStackTrace();
			throw new MailWurmException("[WurmMail] Failed to set the from field");
		}
	}
	
	public void sendAlarmMail(String platform, Exception e) throws MailWurmException {
		try {
			email.setSubject(platform + " foutje");
			email.setMsg("Beste Bob, \n\n"
					+ "Er is wat misgegaan met " + platform + "! Deze error kreeg ik: \n\n"
					+ "\"" + e.getMessage() + "\"\n\n"
					+ "Kun je het oplossen? Dit is echt vreselijk!\n\n"
					+ "Wanhopige groeten,\n\n"
					+ "Phoenix der Belletrie");
			email.addTo(MailCredentials.boss);
			email.send();
		} catch (EmailException e1) {
			System.out.println("[WurmMail] Failed to set the message or the sending failed... Now we can only wait");
			e1.printStackTrace();
			throw new MailWurmException("[WurmMail] Failed to set the message or the sending failed... Now we can only wait");
		}
	}
	
	public void twitterAlarm(Exception e) throws MailWurmException {
		sendAlarmMail("Twitter", e);
	}
	
	public void facebookAlarm(Exception e) throws MailWurmException {
		sendAlarmMail("Facebook", e);
	}
	
	public void databaseAlarm(Exception e) throws MailWurmException {
		sendAlarmMail("H2", e);
	}
	
	public static void main(String[] args) {
		WurmMail mail = new WurmMail();
		try {
			mail.init();
			mail.twitterAlarm(new TwitterWurmException("TEST: TWITTER"));
			
			mail.init();
			mail.facebookAlarm(new FacebookWurmException("TEST: FACEBOOK"));
			
			mail.init();
			mail.databaseAlarm(new FacebookWurmException("TEST: DATABASE"));
		} catch (MailWurmException e) {
			System.out.println("FAILURE");
			e.printStackTrace();
		}
	}
	
}
