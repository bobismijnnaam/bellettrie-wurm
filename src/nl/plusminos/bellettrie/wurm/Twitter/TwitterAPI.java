package nl.plusminos.bellettrie.wurm.twitter;

import nl.plusminos.bellettrie.wurm.exceptions.TwitterWurmException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAPI {
	
	Twitter twit = null;
	
	public void init() throws TwitterWurmException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(TwitterCredentials.consumerKey);
		cb.setOAuthConsumerSecret(TwitterCredentials.consumerSecret);
		cb.setOAuthAccessToken(TwitterCredentials.accessToken);
		cb.setOAuthAccessTokenSecret(TwitterCredentials.accessTokenSecret);
		
		TwitterFactory tf = new TwitterFactory(cb.build());
		twit = tf.getInstance();
		
		try {
			twit.getUserTimeline();
		} catch (TwitterException e) {
			System.out.println("[TwitterAPI] Failed to connect to bellettriewurm account");
			e.printStackTrace();
			throw new TwitterWurmException("[TwitterAPI] Failed to connect to bellettriewurm account");
		}
	}
	
	public void updateStatus(String status) throws TwitterWurmException {
		try {
			twit.updateStatus(status);
		} catch (TwitterException e) {
			System.out.println("[TwitterAPI] Failed to update status");
			e.printStackTrace();
			throw new TwitterWurmException("[TwitterAPI] Failed to update status");
		}
	}
	
	public static void main(String[] args) {
		TwitterAPI ta = new TwitterAPI();
		
		String tweet = "Is daar iemand?";
		System.out.println("Tweeting: \" " + tweet + "\"");

		try {
			ta.updateStatus(tweet);
		} catch (TwitterWurmException e) {
			System.out.println("Tweeting failed");
			return;
		}
		
		System.out.println("Tweeting succesful");
		
		return;
	}
}
