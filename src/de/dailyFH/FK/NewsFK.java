package de.dailyFH.FK;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;

public class NewsFK extends Activity {

	String[][] news = new String[3][15];
	
	public void getFile() throws XmlPullParserException, IOException,
			IllegalStateException, MalformedURLException, ProtocolException,
			IOException, FileNotFoundException {

		FileOutputStream os;
		final String url_str = "http://www.inf.fh-dortmund.de/rss.php";

		os = openFileOutput("news.xml", Context.MODE_PRIVATE);
		URL url = new URL(url_str.replace(" ", "%20"));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();

		int responseCode = conn.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			byte tmp_buffer[] = new byte[4096];
			InputStream is = conn.getInputStream();
			int n;
			while ((n = is.read(tmp_buffer)) > 0) {
				os.write(tmp_buffer, 0, n);
				os.flush();
			}
		} else {
			throw new IllegalStateException("HTTP response: " + responseCode);
		}
		os.close();
	}
	
	public String[][] parseFile(String dateiname) throws XmlPullParserException, IOException, FileNotFoundException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();

		boolean item = false;
		boolean date = false;
		boolean desc = false;
		boolean title = false;
		int i = 0;

		FileInputStream inFile = openFileInput(dateiname);
		InputStreamReader isr = new InputStreamReader(inFile, "iso-8859-15");
		BufferedReader in = new BufferedReader(isr);

		xpp.setInput(in);

		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (xpp.getName().equals("item")) {
					item = true;
				} else if (xpp.getName().equals("title") && item) {
					title = true;
				} else if (xpp.getName().equals("description") && item) {
					desc = true;
				} else if (xpp.getName().equals("pubDate") && item) {
					date = true;
				}
			} else if (eventType == XmlPullParser.TEXT) {
				if (item) {
					if (title) {
						if (i < 15) {
							news[0][i] = xpp.getText();
						}
						title = false;
					} else if (desc) {
						if (i < 15) {
							news[1][i] = xpp.getText();
							news[1][i] = news[1][i].replaceAll("<br/>", "");
							news[1][i] = news[1][i].replaceAll("<br>", "");
							news[1][i] = news[1][i].replaceAll("<br />", "");
						}
						desc = false;
					} else if (date) {
						if (i < 15) {
							news[2][i] = xpp.getText();
							news[2][i] = news[2][i].replaceAll("\\+0200", "");
						}
						date = false;
						i++;
					}
				}
			} else if (eventType == XmlPullParser.END_TAG) {
				if (xpp.getName().equals("item")) {
					item = false;
					title = false;
					desc = false;
					date = false;
				}
			}
			eventType = xpp.next();
		}
		
		return news;
	}
}
