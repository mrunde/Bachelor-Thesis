package de.mrunde.bachelorthesis.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mapquest.android.maps.BoundingBox;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.RouteManager;
import com.mapquest.android.maps.RouteResponse;

import de.mrunde.bachelorthesis.R;

/**
 * This is the initial activity which is started with the application. It offers
 * the user to change the route type and to search for his desired destination.
 * 
 * @author Marius Runde
 */
public class MainActivity extends MapActivity implements OnInitListener {

	/**
	 * Maximum amount of results for the destination
	 */
	private final int MAX_RESULTS = 5;

	// --- Route types ---
	/**
	 * Fastest route type
	 */
	private final String ROUTETYPE_FASTEST = "fastest";

	/**
	 * Shortest route type
	 */
	private final String ROUTETYPE_SHORTEST = "shortest";

	/**
	 * Pedestrian route type
	 */
	private final String ROUTETYPE_PEDESTRIAN = "pedestrian";

	/**
	 * Bicycle route type
	 */
	private final String ROUTETYPE_BICYCLE = "bicycle";

	/**
	 * Current route type
	 */
	private String routeType;

	// --- End of route types ---

	// --- The graphical user interface (GUI) ---
	/**
	 * The entered destination
	 */
	private EditText edt_destination;

	/**
	 * The "search for destination" button
	 */
	private Button btn_search;

	/**
	 * The "calculate route" button<br/>
	 * This button also starts the navigation after route calculation.
	 */
	private Button btn_calculate;

	/**
	 * The "preferences" button to change the route type
	 */
	private Button btn_preferences;

	/**
	 * The "help" button
	 */
	private Button btn_help;

	/**
	 * The initial map view
	 */
	protected MapView map;

	/**
	 * An overlay to display the user's location
	 */
	private MyLocationOverlay myLocationOverlay;

	// --- End of graphical user interface ---

	/**
	 * Route manager for route calculation
	 */
	private RouteManager rm;

	/**
	 * The current location as a String
	 */
	private String str_currentLocation;

	/**
	 * The destination as a String
	 */
	private String str_destination;

	/**
	 * Coordinates of the destination to be sent to the NaviActivity
	 */
	private double[] destination_coords = null;

	/**
	 * TextToSpeech for audio output
	 */
	private TextToSpeech tts;

	/**
	 * This method is called when the application has been started
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Initialize the TextToSpeech
		tts = new TextToSpeech(this, this);

		// Set the route type to fastest
		this.routeType = ROUTETYPE_FASTEST;

		// Setup the whole GUI and map
		setupGUI();
		setupMapView();
		setupMyLocation();
	}

	/**
	 * Set up the GUI
	 */
	private void setupGUI() {
		this.edt_destination = (EditText) findViewById(R.id.edt_destination);
		// TODO just for testing, must be deleted...
		edt_destination.setText("Cineplex, Münster");

		this.btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Get the entered destination
				str_destination = edt_destination.getText().toString();

				if (str_destination.length() == 0) {
					Toast.makeText(MainActivity.this,
							R.string.noDestinationEntered, Toast.LENGTH_SHORT)
							.show();
				} else {
					// Search for the destination
					SearchDestinationTask destinationTask = new SearchDestinationTask();
					destinationTask.execute(str_destination);
				}
			}
		});

		this.btn_calculate = (Button) findViewById(R.id.btn_calculate);
		btn_calculate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (destination_coords == null) {
					Toast.makeText(MainActivity.this,
							R.string.noDestinationEntered, Toast.LENGTH_SHORT)
							.show();
				} else if (btn_calculate.getText() == getResources().getString(
						R.string.calculate)) {
					// Inform the user about the route is being calculated
					tts.speak("Calculating route from current location to "
							+ edt_destination.getText().toString(),
							TextToSpeech.QUEUE_FLUSH, null);

					// Transform the current location into a String
					str_currentLocation = "{latLng:{lat:"
							+ myLocationOverlay.getMyLocation().getLatitude()
							+ ",lng:"
							+ myLocationOverlay.getMyLocation().getLongitude()
							+ "}}";

					// Transform the destination location into a String
					str_destination = "{latLng:{lat:" + destination_coords[0]
							+ ",lng:" + destination_coords[1] + "}}";

					// Calculate the route
					calculateRoute();
				} else {
					// Create an Intent to start the NaviActivity and hereby the
					// navigation
					Intent intent = new Intent(MainActivity.this,
							NaviActivity.class);
					intent.putExtra("str_currentLocation", str_currentLocation);
					intent.putExtra("str_destination", str_destination);
					intent.putExtra("destination_lat", destination_coords[0]);
					intent.putExtra("destination_lng", destination_coords[1]);
					intent.putExtra("routeOptions", getRouteOptions());
					startActivity(intent);
				}
			}
		});

		this.btn_preferences = (Button) findViewById(R.id.btn_preferences);
		btn_preferences.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Display the route type dialog
				displayRouteTypeDialog();
			}
		});

		this.btn_help = (Button) findViewById(R.id.btn_help);
		btn_help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Display the help
//				displayHelp(); TODO
				// Create an Intent to start the HelpActivity
				Intent intent = new Intent(MainActivity.this,
						HelpActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * This is a class to search for the destination asynchronously.
	 * 
	 * @author Marius Runde
	 */
	private class SearchDestinationTask extends
			AsyncTask<String, Void, GeoPoint> {

		/**
		 * Progress dialog to inform the user about the searching process
		 */
		private ProgressDialog progressDialog = new ProgressDialog(
				MainActivity.this);

		@Override
		protected void onPreExecute() {
			// Display progress dialog
			progressDialog.setMessage("Searching for destination...");
			progressDialog.show();
			progressDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// Enable canceling the search
					SearchDestinationTask.this.cancel(true);
				}
			});
		}

		@Override
		protected GeoPoint doInBackground(String... destination) {
			String str_destination = destination[0];
			List<Address> addresses;
			try {
				// Create a geocoder to locate the destination
				Geocoder geocoder = new Geocoder(MainActivity.this,
						Locale.getDefault());
				addresses = geocoder.getFromLocationName(str_destination,
						MAX_RESULTS);
			} catch (IOException e1) {
				// Destination could not be located but try again once
				// because sometimes it works at the second try
				Log.d("MainActivity",
						"First try to locate destination failed. Starting second try...");
				try {
					// Create a geocoder to locate the destination
					Geocoder geocoder = new Geocoder(MainActivity.this,
							Locale.getDefault());
					addresses = geocoder.getFromLocationName(str_destination,
							MAX_RESULTS);
				} catch (IOException e2) {
					// Seems like the destination could really not be
					// found, so send the user a message about the error
					Log.e("MainActivity",
							"IO Exception in searching for destination. This is the error message: "
									+ e2.getMessage());
					Toast.makeText(MainActivity.this,
							R.string.noDestinationFound, Toast.LENGTH_SHORT)
							.show();
					return null;
				}
			}

			if (addresses.isEmpty()) {
				// Destination could not be located
				Toast.makeText(MainActivity.this, R.string.noDestinationFound,
						Toast.LENGTH_SHORT).show();
				return null;
			} else {
				// Destination could be located
				Log.d("MainActivity", "Located destination sucessfully.");
				GeoPoint result = new GeoPoint(addresses.get(0).getLatitude(),
						addresses.get(0).getLongitude());
				return result;
			}
		}

		@Override
		protected void onPostExecute(GeoPoint result) {
			// Dismiss progress dialog
			progressDialog.dismiss();

			// Check if the search was successful
			if (result != null) {
				// Create the destination overlay
				addDestinationOverlay(result);

				// If the route has been calculated before change the text
				// of the button so the route has to be calculated again and
				// clear the route from the RouteManager
				if (btn_calculate.getText() == getResources().getString(
						R.string.start)) {
					btn_calculate.setText(R.string.calculate);
					rm.clearRoute();
				}
			}
		}
	}

	/**
	 * Add the destination overlay to the map
	 * 
	 * @param destination
	 *            The destination
	 */
	private void addDestinationOverlay(GeoPoint destination) {
		// Create a GeoPoint object of the current location and the destination
		GeoPoint currentLocation = new GeoPoint(myLocationOverlay
				.getMyLocation().getLatitude(), myLocationOverlay
				.getMyLocation().getLongitude());

		// Also set the coordinates of the destination for the NaviActivity
		this.destination_coords = new double[] { destination.getLatitude(),
				destination.getLongitude() };

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

		// Zoom and pan the map to show all overlays
		map.getController().zoomToSpan(
				new BoundingBox(currentLocation, destination));
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
		// Zoom and center the map to display the route
		rm.setBestFitRoute(true);
		// Set the route options (e.g. route type)
		rm.setOptions(getRouteOptions());
		// Set debug true to receive the URL
		rm.setDebug(true);
		// Set route callback
		rm.setRouteCallback(new RouteManager.RouteCallback() {

			@Override
			public void onSuccess(RouteResponse response) {
				// Route has been calculated successfully
				Log.i("MainActivity",
						getResources().getString(R.string.routeCalculated));
				// Change the text of the button to enable navigation
				btn_calculate.setText(R.string.start);
			}

			@Override
			public void onError(RouteResponse response) {
				// Find the reason why the route could not be calculated. The
				// status codes can be found here:
				// http://www.mapquestapi.com/directions/status_codes.html The
				// pedestrian error seems to be 500 though and not 6xx
				if (response.info.statusCode == 500) {
					// Route could not be calculated because the length of
					// routes with the pedestrian route type are restricted to a
					// specific distance
					Log.e("MainActivity",
							getResources().getString(
									R.string.routeNotCalculated_500));
					Toast.makeText(
							MainActivity.this,
							getResources().getString(
									R.string.routeNotCalculated_500),
							Toast.LENGTH_LONG).show();
				} else {
					// Route could not be calculated because of another error
					Log.e("MainActivity",
							getResources().getString(
									R.string.routeNotCalculated)
									+ "\nStatus Code: "
									+ response.info.statusCode);
					Toast.makeText(
							MainActivity.this,
							getResources().getString(
									R.string.routeNotCalculated),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		// Calculate the route and display it on the map
		rm.createRoute(str_currentLocation, str_destination);
	}

	/**
	 * Setup the route options and return them
	 * 
	 * @return Route options as String
	 */
	private String getRouteOptions() {
		JSONObject options = new JSONObject();

		try {
			// Set the units to kilometers
			String unit = "m";
			options.put("unit", unit);

			// Set the route type
			options.put("routeType", routeType);

			// Set the output shape format
			String outShapeFormat = "raw";
			options.put("outShapeFormat", outShapeFormat);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return options.toString();
	}

	/**
	 * Set up the map and enable default zoom controls
	 */
	private void setupMapView() {
		this.map = (MapView) findViewById(R.id.map);
		map.setBuiltInZoomControls(true);
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

		// Create the MyLocationOverlay
		this.myLocationOverlay = new MyLocationOverlay(this, map);
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.setMarker(
				getResources().getDrawable(R.drawable.my_location), 0);
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

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Called when the OptionsMenu is created
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Called when an item of the OptionsMenu is clicked
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_about:
			// Initialize an AlertDialog.Builder and an AlertDialog
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			AlertDialog dialog;

			// Inform the user about this application
			builder.setMessage("This is the Bachelor Thesis of Marius Runde");
			builder.setPositiveButton("Awesome!",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// User clicked the "Awesome!" button
						}
					});
			dialog = builder.create();
			dialog.show();
			return true;
		case R.id.menu_help:
			displayHelp();
			return true;
		case R.id.menu_routeTypes:
			displayRouteTypeDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Display the route type dialog so that the user can change it
	 */
	private void displayRouteTypeDialog() {
		// Initialize an AlertDialog.Builder and an AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog dialog;

		// If the route has been calculated before change the text
		// of the button so the route has to be calculated again and
		// clear the route from the RouteManager
		if (btn_calculate.getText() == getResources().getString(R.string.start)) {
			btn_calculate.setText(R.string.calculate);
			rm.clearRoute();
		}

		// Change the route type in the settings
		builder.setTitle(R.string.routeType);
		builder.setItems(R.array.routeTypes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							// Fastest selected
							routeType = ROUTETYPE_FASTEST;
							Toast.makeText(MainActivity.this,
									"Fastest route type selected",
									Toast.LENGTH_SHORT).show();
							break;
						case 1:
							// Shortest selected
							routeType = ROUTETYPE_SHORTEST;
							Toast.makeText(MainActivity.this,
									"Shortest route type selected",
									Toast.LENGTH_SHORT).show();
							break;
						case 2:
							// Pedestrian selected
							routeType = ROUTETYPE_PEDESTRIAN;
							Toast.makeText(MainActivity.this,
									"Pedestrian route type selected",
									Toast.LENGTH_SHORT).show();
							break;
						case 3:
							// Bicycle selected
							routeType = ROUTETYPE_BICYCLE;
							Toast.makeText(MainActivity.this,
									"Bicycle route type selected",
									Toast.LENGTH_SHORT).show();
							break;
						default:
							break;
						}
					}
				});
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * Display the help
	 */
	private void displayHelp() {
		// Initialize an AlertDialog.Builder and an AlertDialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog dialog;

		// Inform the user about this application
		builder.setMessage("Coming soon...");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// User clicked the "OK" button
			}
		});
		dialog = builder.create();
		dialog.show();
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

	/**
	 * Shut down the TextToSpeech engine when the application is terminated
	 */
	@Override
	protected void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
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
