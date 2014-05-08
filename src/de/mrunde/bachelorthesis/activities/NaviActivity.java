package de.mrunde.bachelorthesis.activities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.RouteManager;
import com.mapquest.android.maps.RouteResponse;

import de.mrunde.bachelorthesis.R;
import de.mrunde.bachelorthesis.instructions.InstructionManager;

/**
 * This is the navigational activity which is started by the MainActivity. It
 * navigates the user from his current location to the desired destination.
 * 
 * @author Marius Runde
 */
public class NaviActivity extends MapActivity implements OnInitListener {

	// --- The graphical user interface (GUI) ---
	/**
	 * Instruction view
	 */
	private TextView tv_instruction;

	/**
	 * Map view
	 */
	private MapView map;

	/**
	 * An overlay to display the user's location
	 */
	private MyLocationOverlay myLocationOverlay;

	// --- End of GUI ---

	// --- The route and instruction objects ---
	/**
	 * Current location as String (for RouteManager only!)
	 */
	private String str_currentLocation;

	/**
	 * Destination as String (for RouteManager and as title of destination
	 * overlay)
	 */
	private String str_destination;

	/**
	 * Latitude of the destination
	 */
	private double destination_lat;

	/**
	 * Longitude of the destination
	 */
	private double destination_lng;

	/**
	 * Route manager for route calculation
	 */
	private RouteManager rm;

	/**
	 * Route options (already formatted as a String)
	 */
	private String routeOptions;

	/**
	 * Instruction manager that creates instructions
	 */
	private InstructionManager im;

	// --- End of route and instruction objects ---

	/**
	 * LocationListener of the NaviActivity
	 */
	private LocationListener locationListener;

	/**
	 * Maximum amount of tolerated driving errors
	 */
	private final int MAX_DRIVING_ERRORS = 5;

	/**
	 * Store the number of driving errors
	 */
	private int drivingErrors = 0;

	/**
	 * Store the last distance to the next decision point to recognize driving
	 * errors
	 */
	private double lastDistance = -1;

	/**
	 * TextToSpeech for audio output
	 */
	private TextToSpeech tts;

	/**
	 * This class handles changes of the user's location.
	 * 
	 * @author Marius Runde
	 */
	private final class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO This is just for testing and should be deleted
			// AlertDialog.Builder builder = new AlertDialog.Builder(
			// NaviActivity.this);
			// builder.setTitle("Location changed!");
			// AlertDialog dialog = builder.create();
			// dialog.show();

			checkForDrivingError(location);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// GPS provider status changed. Do nothing
		}

		@Override
		public void onProviderEnabled(String provider) {
			// GPS is turned on. Do nothing
		}

		@Override
		public void onProviderDisabled(String provider) {
			// GPS is turned off. Do nothing. User should have been informed
			// already before
		}
	}

	/**
	 * This method is called when the application has been started
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navi);

		// Get the route information from the intent (start and destination)
		Intent intent = getIntent();
		this.str_currentLocation = intent.getStringExtra("str_currentLocation");
		this.str_destination = intent.getStringExtra("str_destination");
		this.destination_lat = intent.getDoubleExtra("destination_lat", 0.0);
		this.destination_lng = intent.getDoubleExtra("destination_lng", 0.0);
		this.routeOptions = intent.getStringExtra("routeOptions");

		// Initialize the TextToSpeech
		tts = new TextToSpeech(this, this);

		// Initialize the MyLocationListener
		this.locationListener = new MyLocationListener();
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10,
				this.locationListener);

		// Setup the whole GUI and map
		setupGUI();
		setupMapView();
		setupMyLocation();

		// Add the destination overlay to the map
		addDestinationOverlay(destination_lat, destination_lng);

		// Calculate the route
		calculateRoute();

		// Get the guidance information and create the instructions
		getGuidance();
	}

	/**
	 * Set up the GUI
	 */
	private void setupGUI() {
		this.tv_instruction = (TextView) findViewById(R.id.tv_instruction);
	}

	/**
	 * Set up the map and enable default zoom controls
	 */
	private void setupMapView() {
		this.map = (MapView) findViewById(R.id.map);
		map.setBuiltInZoomControls(true);
		// Disable user interaction on the map
		map.setClickable(false);
		map.setLongClickable(false);
	}

	/**
	 * Set up a MyLocationOverlay and execute the runnable once a location has
	 * been fixed
	 */
	private void setupMyLocation() {
		// Check if the GPS is enabled
		if (!((LocationManager) getSystemService(LOCATION_SERVICE))
				.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// Open dialog to inform the user that the GPS is disabled
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(getResources().getString(R.string.gpsDisabled));
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.openSettings,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Open the location settings if it is disabled
							Intent intent = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(intent);
						}
					});
			builder.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Dismiss the dialog
							dialog.cancel();
						}
					});

			// Display the dialog
			AlertDialog dialog = builder.create();
			dialog.show();
		}

		this.myLocationOverlay = new MyLocationOverlay(this, map);
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.runOnFirstFix(new Runnable() {

			@Override
			public void run() {
				GeoPoint currentLocation = myLocationOverlay.getMyLocation();
				map.getController().animateTo(currentLocation);
				map.getController().setZoom(14);
				map.getOverlays().add(myLocationOverlay);
				myLocationOverlay.setFollowing(true);
			}
		});
	}

	/**
	 * Add the destination overlay to the map
	 * 
	 * @param lat
	 *            Latitude of the destination
	 * @param lng
	 *            Longitude of the destination
	 */
	private void addDestinationOverlay(double lat, double lng) {
		// Create a GeoPoint object of the destination
		GeoPoint destination = new GeoPoint(lat, lng);

		// Clear previous overlays first
		if (map.getOverlays().size() > 1) {
			map.getOverlays().remove(1);
		}

		// Create the destination overlay
		OverlayItem oi_destination = new OverlayItem(destination,
				"Destination", str_destination);
		final DefaultItemizedOverlay destinationOverlay = new DefaultItemizedOverlay(
				getResources().getDrawable(R.drawable.destination_flag));
		destinationOverlay.addItem(oi_destination);

		// Add the overlay to the map
		map.getOverlays().add(destinationOverlay);
	}

	/**
	 * Calculate the route from the current location to the destination
	 */
	private void calculateRoute() {
		// Clear the previous route first
		if (rm != null) {
			rm.clearRoute();
		}

		// Initialize a new RouteManager to calculate the route
		rm = new RouteManager(getBaseContext(), getResources().getString(
				R.string.apiKey));
		rm.setMapView(map);
		// Set the route options (e.g. route type)
		rm.setOptions(routeOptions);
		// Set route callback
		rm.setRouteCallback(new RouteManager.RouteCallback() {

			@Override
			public void onSuccess(RouteResponse response) {
				// Route has been calculated successfully
				Log.i("NaviActivity",
						getResources().getString(R.string.routeCalculated));

				// Dismiss the progress dialog
				// progressDialog.dismiss();
			}

			@Override
			public void onError(RouteResponse response) {
				// Route could not be calculated
				Log.e("NaviActivity",
						getResources().getString(R.string.routeNotCalculated));
			}
		});
		// Calculate the route and display it on the map
		rm.createRoute(str_currentLocation, str_destination);

		map.getController().animateTo(myLocationOverlay.getMyLocation());
		map.getController().setZoom(18);
	}

	/**
	 * Get the guidance information from MapQuest
	 */
	private void getGuidance() {
		// Create the URL to request the guidance from MapQuest
		String url;
		try {
			url = "https://open.mapquestapi.com/guidance/v1/route?key="
					+ getResources().getString(R.string.apiKey)
					+ "&from="
					+ URLEncoder.encode(str_currentLocation, "UTF-8")
					+ "&to="
					+ URLEncoder.encode(str_destination, "UTF-8")
					+ "&narrativeType=text&fishbone=false&callback=renderBasicInformation";
		} catch (UnsupportedEncodingException e) {
			Log.e("NaviActivity",
					"Could not encode the URL. This is the error message: "
							+ e.getMessage());
			return;
		}

		// Get the data. The instructions are created afterwards.
		GetJsonTask jsonTask = new GetJsonTask();
		jsonTask.execute(url);
	}

	/**
	 * This is a class to get the JSON file asynchronously from the given URL.
	 * 
	 * @author Marius Runde
	 */
	private class GetJsonTask extends AsyncTask<String, Void, JSONObject> {

		/**
		 * Progress dialog to inform the user about the download
		 */
		private ProgressDialog progressDialog = new ProgressDialog(
				NaviActivity.this);

		/**
		 * Count the time needed for the data download
		 */
		private int downloadTimer;

		@Override
		protected void onPreExecute() {
			// Display progress dialog
			progressDialog.setMessage("Downloading guidance...");
			progressDialog.show();
			progressDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// Cancel the download when the "Cancel" button has been
					// clicked
					GetJsonTask.this.cancel(true);
				}
			});

			// Set timer to current time
			downloadTimer = Calendar.getInstance().get(Calendar.SECOND);
		}

		@Override
		protected JSONObject doInBackground(String... url) {
			// Get the data from the URL
			String output = "";
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			try {
				response = httpclient.execute(new HttpGet(url[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					output = out.toString();
				} else {
					// Close the connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {
				Log.e("GetJsonTask",
						"Could not get the data. This is the error message: "
								+ e.getMessage());
				return null;
			}

			// Delete the "renderBasicInformation" stuff at the beginning and
			// end of the output if needed to convert it to a JSONObject
			if (output.startsWith("renderBasicInformation(")) {
				output = output.substring(23, output.length());
			}
			if (output.endsWith(");")) {
				output = output.substring(0, output.length() - 2);
			}

			// Convert the output to a JSONObject
			try {
				JSONObject result = new JSONObject(output);
				return result;
			} catch (JSONException e) {
				Log.e("GetJsonTask",
						"Could not convert output to JSONObject. This is the error message: "
								+ e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// Dismiss progress dialog
			progressDialog.dismiss();

			// Write the time needed for the download into the log
			downloadTimer = Calendar.getInstance().get(Calendar.SECOND)
					- downloadTimer;
			Log.i("GetJsonTask", "Completed guidance download in "
					+ downloadTimer + " seconds");

			// Create the instructions
			createInstructions(result);
		}
	}

	/**
	 * Create the instructions for the navigation
	 * 
	 * @param json
	 *            The guidance information from MapQuest
	 */
	private void createInstructions(JSONObject json) {
		if (json == null) {
			// Could not receive the JSON
			Toast.makeText(this,
					getResources().getString(R.string.routeNotCalculated),
					Toast.LENGTH_SHORT).show();
			// Finish the activity to return to MainActivity
			finish();
		} else {
			// Create the instruction manager
			im = new InstructionManager(json);
			// Check if the import was successful
			if (im.isImportSuccessful()) {
				// Create the instructions
				im.createInstructions();

				// Display the first instruction in the TextView
				// TODO must be only the first one and not 5
				tv_instruction.setText(im.getInstruction(0) + "\n"
						+ im.getInstruction(1) + "\n" + im.getInstruction(2)
						+ "\n" + im.getInstruction(3) + "\n"
						+ im.getInstruction(4));
			} else {
				// Import was not successful
				Toast.makeText(
						this,
						getResources().getString(
								R.string.jsonImportNotSuccessful),
						Toast.LENGTH_SHORT).show();
				// Finish the activity to return to MainActivity
				finish();
			}
		}
	}

	/**
	 * This method compares the distance between the user and the next decision
	 * point. If this distance increases MAX_DRIVING_ERRORS times in a row, the
	 * route will be recalculated.
	 * 
	 * @param lastLocation
	 *            The user location
	 */
	private void checkForDrivingError(Location lastLocation) {
		// Get the last decision point
		GeoPoint lastDecisionPoint = im.getLastInstruction().getDecisionPoint();

		// Calculate the distance to the next decision point
		float[] results = new float[1];
		Location.distanceBetween(lastLocation.getLatitude(),
				lastLocation.getLongitude(), lastDecisionPoint.getLatitude(),
				lastDecisionPoint.getLongitude(), results);

		// Compare the distances
		if (this.lastDistance < results[0]) {
			this.drivingErrors++;
			Log.w("NaviActivity.DrivingError", "Driving errors increased to "
					+ this.drivingErrors);
		} else if (this.drivingErrors > 0) {
			this.drivingErrors--;
			Log.i("NaviActivity.DrivingError", "Driving errors decreased to "
					+ this.drivingErrors);
		}

		if (this.drivingErrors >= this.MAX_DRIVING_ERRORS) {
			// This is supposed to be a driving error
			Toast.makeText(this, R.string.recalculate, Toast.LENGTH_SHORT)
					.show();
			tts.speak(getResources().getString(R.string.recalculate),
					TextToSpeech.QUEUE_FLUSH, null);
			Log.w("NaviActivity.DrivingError", "Recalculating route...");

			// Recalculate route
			calculateRoute();

			// Get the guidance information and create the instructions
//			getGuidance(); TODO seems like an error is happening here
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// Do nothing
		return false;
	}

	/**
	 * Enable features of the MyLocationOverlay
	 */
	@Override
	protected void onResume() {
		myLocationOverlay.enableMyLocation();
		super.onResume();
	}

	/**
	 * Disable features of the MyLocationOverlay when in the background
	 */
	@Override
	protected void onPause() {
		super.onPause();
		myLocationOverlay.disableMyLocation();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			tts.setLanguage(Locale.ENGLISH);
		} else {
			tts = null;
			Log.e("MainActivity", "Failed to initialize the TextToSpeech");
		}
	}
}
