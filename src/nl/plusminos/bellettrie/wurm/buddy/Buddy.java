package nl.plusminos.bellettrie.wurm.buddy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import nl.plusminos.bellettrie.wurm.exceptions.MailWurmException;
import nl.plusminos.bellettrie.wurm.exceptions.TwitterWurmException;
import nl.plusminos.bellettrie.wurm.mail.WurmMail;
import nl.plusminos.bellettrie.wurm.twitter.TwitterAPI;

/*
 * http://stackoverflow.com/questions/5824049/running-a-method-when-closing-the-program
 */
public class Buddy {
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	public static final int TIME_BETWEEN_OPEN_UPDATES = 60;
	
	private class PostOpenReminder implements Runnable {
		boolean doPost;
		
		public PostOpenReminder(boolean p) {
			doPost = p;
		}
		
		public void run() {
			TwitterAPI ta = new TwitterAPI();
			try {
				ta.init();
				if (doPost) ta.updateStatus("Heerlijk als de drakenkelder open is!");
			} catch (TwitterWurmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			scheduler.schedule(new PostOpenReminder(true), TIME_BETWEEN_OPEN_UPDATES, TimeUnit.SECONDS);
			System.out.println("Posted tweet & started timer");
		}
	}
	
	public void init() {
		// Send open msg
		TwitterAPI ta = new TwitterAPI();
		try {
			ta.init();
			ta.updateStatus("De drakenkelder is zojuist geopend, kom gezellig langs voor een goed boek of kop koffie!"
					+ " #drakenkelder");
		} catch (TwitterWurmException e) {
			System.out.println("[Buddy] Shit is hitting the fan... Twitter opening failed");
			e.printStackTrace();
			WurmMail wm = new WurmMail();
			try {
				wm.init();
				wm.sendAlarmMail("openen", e);
			} catch (MailWurmException e1) {
				System.out.println("[Buddy] Shit is hitting the fan... Everything failed");
				e1.printStackTrace();
			}
		}
		
		// Setup periodic tweets
		scheduler.schedule(new PostOpenReminder(false), TIME_BETWEEN_OPEN_UPDATES, TimeUnit.SECONDS);
		
		// Set shutdown hook s.t. a closing message can be sent upon shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				TwitterAPI ta = new TwitterAPI();
				try {
					ta.init();
					ta.updateStatus("De drakenkelder sluit weer! Tot later!"
							+ "#wormpjegezien #kastjedicht");
				} catch (TwitterWurmException e) {
					System.out.println("[Buddy] Shit is hitting the fan... Twitter closing failed");
					e.printStackTrace();
					WurmMail wm = new WurmMail();
					try {
						wm.init();
						wm.sendAlarmMail("afsluiten", e);
					} catch (MailWurmException e1) {
						// TODO Auto-generated catch block
						System.out.println("[Buddy] Shit is hitting the fan... Everything failed");
						e1.printStackTrace();
					}
				}
			}
		}, "Shutdown thread of bellettrie worm buddy"));
		
		try {
			Thread.sleep(500 * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	public static void main(String[] args) {
		Buddy buddy = new Buddy();
		buddy.init();
	}
}
