import java.util.Timer;
import java.util.TimerTask;

import twitter4j.StatusAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class StreamTweets extends StatusAdapter {
	static TwitterStream twitterStream;
	static MyStatusListener listener;
	static Timer sampleTimer;
	static Timer sleepTimer;
	
	static final int seconds = 60 * 60;
	static final int sleepSeconds = 58 * 60;

	public static void main(String[] args) throws TwitterException {
		twitterStream = new TwitterStreamFactory().getInstance();
		listener      = new MyStatusListener();
		twitterStream.addListener(listener);

		sampleTimer = new Timer();
		sleepTimer  = new Timer();
		
		class SleepTask extends TimerTask {
			public void run() {
				   twitterStream.cleanUp();
				   listener.cleanUp();
			}
		}
		
		class GetSampleTask extends TimerTask {
			public void run() {
				listener.newFile();
				sleepTimer.schedule(new SleepTask(), sleepSeconds * 1000);
				twitterStream.sample();
			}
		}

		sampleTimer.schedule(new GetSampleTask(), 0, seconds * 1000);
	    sleepTimer.schedule(new SleepTask(), sleepSeconds * 1000);

	} // end main

}
