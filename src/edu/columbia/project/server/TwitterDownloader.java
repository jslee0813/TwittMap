package edu.columbia.project.server;

import java.io.IOException;
import java.util.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import twitter4j.conf.ConfigurationBuilder;
import twitter4j.*;

public class TwitterDownloader {
	
	public static void download(User user, Key key) throws IOException {
		String text = "";
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("")  // Copy all the twitter API keys accordingly
		.setOAuthConsumerSecret("")
		.setOAuthAccessToken("")
		.setOAuthAccessTokenSecret("");

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        
        datastore.delete(key);
       
        
		// The factory instance is re-useable and thread safe.
		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        try 
        {
            Query query = new Query("soccer"); // Search tweets for particular words
            QueryResult result;
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            
            for (Status tweet : tweets) {	
            	
            	text = "@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + " at " + tweet.getGeoLocation();
                	//System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
					// You can store the tweets in datastore                
            	
                Entity e = new Entity("TwittMap", key);
                e.setProperty("user", user);
                e.setProperty("date", new Date());
                e.setProperty("content", text);
                e.setProperty("location", tweet.getGeoLocation());
                datastore.put(e);               
            }
        } catch (TwitterException te) {
            te.printStackTrace();
            text = "Failed to search tweets: " + te.getMessage();
        }
	}

}