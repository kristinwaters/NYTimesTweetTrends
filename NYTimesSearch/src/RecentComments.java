import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

public class RecentComments {

	static final String apiKey   = "6d2db5bfeeed52c426ec01ae32032804:9:64600048";
	static String recentComments = "http://api.nytimes.com/svc/community/v2/comments/recent.json?api-key=" + apiKey;
	static String filePath       = "/Users/Kristin/Desktop/NYTimes/NYTRecentComments_";
	
    static Gson gson = new Gson();
    
	public static void main(String[] args) {

		Date currentDate = new Date();

		String rightNow = currentDate.toString();
		rightNow = rightNow.replace(' ', '_');
		rightNow = rightNow.replace(':', '_');

		// Make sure this gets GC'd
		currentDate = null;

		getData(recentComments,  filePath + rightNow + ".csv");

		// Force GC
		System.gc();
	}

	public static void getData(String urlText, String fileName) 
	{
		try
		{  
			// Create Connection
			URL urlNYTimes = new URL(urlText);
			URLConnection nyTimes = urlNYTimes.openConnection();

			// Create a new file output stream
			FileOutputStream outStream = new FileOutputStream(fileName, true);

			// Connect print stream to the output stream
			PrintStream printStream = new PrintStream(outStream);

			// Create an input stream for the connection
			InputStreamReader inReader = new InputStreamReader(nyTimes.getInputStream());

			// Connect reader stream to connection input
			BufferedReader inBuffer = new BufferedReader(inReader);
			String inputLine = inBuffer.readLine();
			
			if (inputLine != null) {
				System.out.println(inputLine);
				
				
		        // Deserialize.
				CommentData commentData = gson.fromJson(inputLine, CommentData.class);
				
				for (int i = 0; i < commentData.getComments().size(); i++){
					printStream.println(commentData.getComments().get(i).getDisplayName().replace(',', ' ') + ',' + 
							            commentData.getComments().get(i).getCommentBody().replace(',', ' ') + ',' + 
							            commentData.getComments().get(i).getApproveDate().replace(',', ' ') + ',' + 
							            commentData.getComments().get(i).getArticleURL());
				}
			}

			printStream.flush();
			outStream.flush();

			// Close streams and set to null
			inBuffer.close();
			inReader.close();
			printStream.close();
			outStream.close();

			urlNYTimes  = null;
			inBuffer    = null;
			inReader    = null;
			printStream = null;
			outStream   = null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

class CommentData {
    private List<CommentData> results;
    private List<Comment> comments;

    public List<Comment> getComments() { return comments; }
    public List<CommentData> getResults() { return results; }

    public void setComments( List<Comment> comments) { this.comments = comments; }
    public void setResults( List<CommentData> results) { this.results = results; }
}

class Comment {
    private String commentBody;
    private String approveDate;
    private String display_name;
    private String articleURL;

    public String getCommentBody() { return commentBody; }
    public String getApproveDate() { return approveDate; }
    public String getDisplayName() { return display_name; }
    public String getArticleURL() { return articleURL; }

    public void setCommentBody(String commentBody) { this.commentBody = commentBody; }
    public void setApproveDate(String approveDate) { this.approveDate = approveDate; }
    public void setDisplayName(String display_name) { this.display_name = display_name; }
    public void setArticleURL(String articleURL) { this.articleURL = articleURL; }

    public String toString() {
        return String.format("commentBody:%s,display_name:%s,articleURL:%s", commentBody, display_name, articleURL);
    }
}
