package de.dailyFH.libs;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import android.app.Activity;
import android.util.Log;

public class XMLParser extends Activity {

	private Document doc;
	
	public void init(InputStream is) {
        try {
        	DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse(is);
		} catch (Exception e) {
			Log.i("XMLPARSER" , "XMLPARSER init() - " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void elementeAusgeben() {
		try {
			
			// Oberstes Element der Datei auslesen
			//System.out.println( doc.getDocumentElement().getNodeName() );
            
            // titel-Element ausgeben
            NodeList titelKnoten = doc.getElementsByTagName("titel");
            Element titelElement = (Element)titelKnoten.item(0);
            System.out.println( ((Node)titelElement.getChildNodes().item(0)).getNodeValue().trim() );
			
			NodeList knotenAlleEintraege = doc.getElementsByTagName("eintrag");
			
			for(int i = 0; i < knotenAlleEintraege.getLength(); i++) {
				
				Node eintragKnoten = knotenAlleEintraege.item(i);	// "eintrag"-Knoten iterieren
				Element eintragElement = (Element)eintragKnoten;	// Umwandeln von Node in Element
				
				// name-Element auslesen
				NodeList nameNode = eintragElement.getElementsByTagName("name");	// Unterknoten "name" auslesen
				Element nameElement = (Element)nameNode.item(0);					// "name"-Knoten in Element umwandeln
				NodeList textFNList = nameElement.getChildNodes();					// Unterknoten von "name" als neue NodeList speichern
	            String name = ((Node)textFNList.item(0)).getNodeValue().trim();
				//System.out.println(name);
				Log.e("XMLPARSER", name);

	            // datum-Element auslesen
	            NodeList datumNode = eintragElement.getElementsByTagName("datum");
	            Element datumElement = (Element)datumNode.item(0);
	            textFNList = datumElement.getChildNodes();
	            String datum = ((Node)textFNList.item(0)).getNodeValue().trim();
	            //System.out.println(datum);
	            Log.e("XMLPARSER", datum);
			}
			
		} catch (Exception e) {
			Log.i("XMLPARSER" , "XMLPARSER elementeAusgeben() - " + e.getMessage());
			e.printStackTrace();
		}
	}
}