package de.dailyFH.FK;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import de.dailyFH.Webservices.NewsWS;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class NewsFK extends Activity {

	String[][] news = new String[3][15];
	private de.dailyFH.Webservices.NewsWS newsWS;
	
	public boolean dateiLaden(Context myContext) {
		
		newsWS = new NewsWS();
		try {
			newsWS.getFile(myContext);
			return true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String[][] parseFile(Context myContext) throws XmlPullParserException, IOException, FileNotFoundException {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();

		boolean item = false;
		boolean date = false;
		boolean desc = false;
		boolean title = false;
		int i = 0;

		FileInputStream inFile = myContext.openFileInput("news.xml");
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
