import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class MyStatusListener implements StatusListener {

	static FileOutputStream outStream;
	static PrintStream printStream;

	public MyStatusListener () {		
		newFile();
	}

	public void onStatus(Status status) {			
		if ((status.getUser().getScreenName() != null) &&
				(status.getText() != null) &&
				(status.getCreatedAt() != null))
		{

			try {
				printStream.println("@" + status.getUser().getScreenName().replace(',', ' ') + "," + 
						status.getText().replace(',', ' ') + "," + 
						status.getCreatedAt());
				outStream.flush();
				printStream.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {				
		// System.out.println("Got a status deletion notice id:" +
		// statusDeletionNotice.getStatusId());
	}

	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		// System.out.println("Got track limitation notice:" +
		// numberOfLimitedStatuses);
	}

	public void onScrubGeo(long userId, long upToStatusId) {
		// System.out.println("Got scrub_geo event userId:" + userId +
		// " upToStatusId:" + upToStatusId);
	}

	public void onException(Exception ex) {
		ex.printStackTrace();
	}

	public void cleanUp() {
		try {
			outStream.flush();
			printStream.flush();
			
			outStream.close();
			printStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void newFile() {
		try {
			Date currentDate = new Date();
			
			String rightNow = currentDate.toString();
			rightNow = rightNow.replace(' ', '_');
			rightNow = rightNow.replace(':', '_');

			String fileName  = "/Users/Kristin/Desktop/Streams/StreamTweets_" + rightNow + ".csv";
			
			// Create a new file output stream
			outStream = new FileOutputStream(fileName, true);

			// Connect print stream to the output stream
			printStream = new PrintStream(outStream);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
