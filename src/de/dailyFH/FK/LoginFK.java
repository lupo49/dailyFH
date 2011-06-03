package de.dailyFH.FK;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

public class LoginFK extends Activity {

	public boolean pruefPin(String pin, String matr) {

		// Ausgaben zum Debuggen
		Log.i("Methode", "pruefPin");
		Log.i("Pin-eingabe", md5(pin));

		// Variable fuer return
		boolean erg = false;

		// Preferencdatei
		String file = matr + "_prefs";

		// Preferences laden
		SharedPreferences settings = getSharedPreferences(file, 0);

		// Gespeicherte Daten zum Debuggen ausgeben
		Log.i("PIN-saved", settings.getString("Pin", "0000"));
		Log.i("MATR-saved", settings.getString("Matrikelnummer", "matrikel"));

		// eingegeben Pin mit der gespeicherten abgleichen
		if (md5(pin).equals(settings.getString("Pin", "0000"))) {
			Log.i("preufPiN", "Ist gleich");
			erg = true;
		}
		return erg;
	}
	
	public String md5(String in) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.reset();
			digest.update(in.getBytes());
			byte[] a = digest.digest();
			int len = a.length;
			StringBuilder sb = new StringBuilder(len << 1);
			for (int i = 0; i < len; i++) {
				sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
				sb.append(Character.forDigit(a[i] & 0x0f, 16));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
