package de.mrunde.bachelorthesis.activities;

import de.mrunde.bachelorthesis.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * This is the about activity. It shows the GitHub wiki page where the meta
 * information are located.
 * 
 * @author Marius Runde
 */
public class AboutActivity extends Activity {

	/**
	 * The web view object that displays the help page
	 */
	private WebView webView;

	/**
	 * This method is called when the application has been started
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		this.webView = (WebView) findViewById(R.id.web);
		webView.loadUrl(getResources().getString(R.string.aboutPage));
	}
}
