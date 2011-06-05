package de.dailyFH.Webservices;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;

public class NewsWS extends Activity {

	public void getFile(Context myContext) throws XmlPullParserException, IOException,
			IllegalStateException, MalformedURLException, ProtocolException,
			IOException, FileNotFoundException {

		FileOutputStream os;
		final String url_str = "http://www.inf.fh-dortmund.de/rss.php";

		os = myContext.openFileOutput("news.xml", Context.MODE_PRIVATE);
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
}
