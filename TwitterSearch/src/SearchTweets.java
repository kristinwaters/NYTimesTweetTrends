/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since Twitter4J 2.1.7
 */
public class SearchTweets {

    public static void main(String[] args) {
    	
    	String searchTerm = "RT";
    	Query searchQuery = new Query(searchTerm);
    	
    	// Restrict results to English only
    	searchQuery.setLang("en");
    	
    	// Set the number of results
    	searchQuery.rpp(100);
    	
    	// Set the since-date
    	searchQuery.since("2011-09-25");
    	
        Twitter twitter = new TwitterFactory().getInstance();
        
        try
        {
            // Create a new file output stream connected to "SearchTweets.csv"
        	FileOutputStream outStream = new FileOutputStream("/Users/Kristin/Desktop/SearchTweets.csv", true);

            // Connect print stream to the output stream
        	PrintStream printStream = new PrintStream(outStream);

            QueryResult result = twitter.search(searchQuery);
            List<Tweet> tweets = result.getTweets();
            
            for (Tweet tweet : tweets) {
                System.out.println("@" + tweet.getFromUser() + " - " + tweet.getText() + " - " + tweet.getLocation() + " - " + tweet.getCreatedAt());
                printStream.println(tweet.getFromUser() + "," + tweet.getText().replace(',',' ') + "," + tweet.getCreatedAt());
                printStream.flush();
            }
            
            System.out.println("\nRemaining Hits: " + " " + twitter.getRateLimitStatus().getRemainingHits());
        	printStream.close();
        	outStream.close();
            System.exit(0);
            
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
        catch (Exception e)
        {
            System.err.println ("Error writing to file");
            e.printStackTrace();
        }
    }
}
