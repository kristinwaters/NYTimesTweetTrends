import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

public class SearchPopular {

	static final String apiKey = "d08aee8bfddc12cccaa7dd30b680f956:7:64600048";
	static String mostViewed  = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/all-sections/1.json?api-key="  + apiKey;
	static String mostEmailed = "http://api.nytimes.com/svc/mostpopular/v2/mostemailed/all-sections/1.json?api-key=" + apiKey;
	static String mostShared  = "http://api.nytimes.com/svc/mostpopular/v2/mostshared/all-sections//1.json?api-key="  + apiKey;

	static String filePath = "/Users/Kristin/Desktop/NYTimes/NYTPopular_";
    static Gson gson = new Gson();
    
	public static void main(String[] args) {

		Date currentDate = new Date();

		String rightNow = currentDate.toString();
		rightNow = rightNow.replace(' ', '_');
		rightNow = rightNow.replace(':', '_');

		// Make sure this gets GC'd
		currentDate = null;

		//getData(mostViewed,  filePath + rightNow + "_MostViewed.csv");
		//getData(mostEmailed, filePath + rightNow + "_MostEmailed.csv");
		getData(mostShared,  filePath + rightNow + "_MostShared3.csv");

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
				Data data = gson.fromJson(inputLine, Data.class);
				
				for (int i = 0; i < data.getResults().size(); i++){
					printStream.println(data.getResults().get(i).getTitle().replace(',', ' ') + ',' + 
							            data.getResults().get(i).getByline().replace(',', ' ') + ',' + 
							            data.getResults().get(i).getPublishedData().replace(',', ' ') + ',' + 
							            data.getResults().get(i).getSection() + ',' +
							            data.getResults().get(i).getURL());
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

class Data {
    private List<Data> results;
    
    private String url;
    private String section;
    private String title;
    private String the_abstract;
    private String published_date;
    private String source;
    private String byline;

    public List<Data> getResults() { return results; }

    public String getURL() { return url; }
    public String getSection() { return section; }
    public String getTitle() { return title; }
    public String getAbstract() { return the_abstract; }
    public String getPublishedData() { return published_date; }
    public String getSource() { return source; }
    public String getByline() { return byline; }

    public void setResults( List<Data> results) { this.results = results; }
    public void setURL(String url) { this.url = url; }
    public void setSection(String section) { this.section = section; }
    public void setTitle(String title) { this.title = title; }
    public void setAbstract(String the_abstract) { this.the_abstract = the_abstract; }
    public void setPublishedData(String published_date) { this.published_date = published_date; }
    public void setSource(String source) { this.source = source; }
    public void setByline(String byline) { this.byline = byline; }

    public String toString() {
        return String.format("title:%s,published_date:%s,source:%s", title, published_date, source);
    }
}
