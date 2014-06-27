package nl.plusminos.bellettrie.wurm.Twitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuth2Token;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAPI {
	Twitter twit = null;
	private boolean error = false;
	
	// Consumer key = api key
	//	cb.setOAuthAccessToken(TwitterCredentials.accessToken);
	//	cb.setOAuthAccessTokenSecret(TwitterCredentials.accessTokenSecret);
	//	cb.setUser(TwitterCredentials.username);
	//	cb.setPassword(TwitterCredentials.password);
	public TwitterAPI() {
//		OAuth2Token token = getToken();
//		if (token == null) {
//			error = false;
//			return;
//		}
//		
//		ConfigurationBuilder cb = new ConfigurationBuilder();
//		cb.setDebugEnabled(true);
//		cb.setApplicationOnlyAuthEnabled(true);
//		cb.setOAuthConsumerKey(TwitterCredentials.consumerKey);
//		cb.setOAuthConsumerSecret(TwitterCredentials.consumerSecret);
//		cb.setOAuth2TokenType(token.getTokenType());
//		cb.setOAuth2AccessToken(token.getAccessToken());
////		cb.setUser(TwitterCredentials.username);
////		cb.setPassword(TwitterCredentials.password);
//		
//		TwitterFactory tf = new TwitterFactory(cb.build());
//		twit = tf.getInstance();
//		
//		try {
//			twit.getUserTimeline();
//		} catch (TwitterException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
////		twit.setOAuthConsumer(TwitterCredentials.consumerKey, TwitterCredentials.consumerSecret);
////		try {
////			twit.getOAuth2Token();
////		} catch (TwitterException e) {
////			System.out.println("[TwitterAPI] Failed retrieving OAth2Token");
////			e.printStackTrace();
////			error = true;
////			return;
////		}
//		
//		
//		System.out.println("[TwitterAPI] Connection made");
//		cb.setApplicationOnlyAuthEnabled(true);
		
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
			error = false;
		}
	}
	
//	private void coupleWithBellettrieWurm() {
//		try {
//			// Get auth url
//			Twitter twit = new TwitterFactory().getInstance();
//			twit.setOAuthConsumer(TwitterCredentials.consumerKey, TwitterCredentials.consumerSecret);
//			RequestToken rt = twit.getOAuthRequestToken();
//			System.out.println("Authorization url: " + rt.getAuthorizationURL());
//			
//			AccessToken at = null;
//			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//			while (at == null) {
//				try {
//					System.out.print("Enter credit card number: ");
//					String pin = br.readLine();
//					at = twit.getOAuthAccessToken();
//				} catch (IOException e) {
//					System.out.println("[TwitterAPI] System IO error. Try again");
//					e.printStackTrace();
//				} catch (TwitterException e) {
//					System.out.println("[TwitterAPI] Failed to get access token, cause: " + e.getMessage());
//					System.out.print("[TwitterAPI] Retry entering credit card number: ");
//				}
//			}
//			
//			System.out.println("Access token: " + at.getToken());
//			System.out.println("Access token secret: " + at.getTokenSecret());
//			
//			twit.getUserTimeline();
//		} catch (TwitterException e) {
//			System.out.println("[TwitterAPI] Docking failed");
//			e.printStackTrace();
//		}
//	}
	
//	public OAuth2Token getToken() {
//		OAuth2Token token = null;
//		ConfigurationBuilder cb = new ConfigurationBuilder();
//		
//		cb.setApplicationOnlyAuthEnabled(true);
//		cb.setOAuthConsumerKey(TwitterCredentials.consumerKey);
//		cb.setOAuthConsumerSecret(TwitterCredentials.consumerSecret);
//		
//		try {
//			token = new TwitterFactory(cb.build()).getInstance().getOAuth2Token();
//		} catch (TwitterException e) {
//			System.out.println("[TwitterAPI] Failed to get OAth2 token");
//			e.printStackTrace();
//			return null;
//		}
//		
//		return token;
//	}
	
	public boolean getError() {
		return error;
	}
	
	public boolean updateStatus(String status) {
		try {
			Status result = twit.updateStatus(status);
			
			return true;
		} catch (TwitterException e) {
			System.out.println("[TwitterAPI] Failed to update status");
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args) {
		TwitterAPI ta = new TwitterAPI();
		
		String tweet = "Is daar iemand?";
		System.out.println("Tweeting: \" " + tweet + "\"");

		if (ta.updateStatus(tweet)) {
			System.out.println("Tweeting succesful");
		} else {
			System.out.println("Tweeting failed");
		}
		
		return;
	}
}
