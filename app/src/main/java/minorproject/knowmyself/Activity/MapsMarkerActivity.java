package minorproject.knowmyself.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import minorproject.knowmyself.Database.MapDBHelper;
import minorproject.knowmyself.Other.UserLocation;
import minorproject.knowmyself.R;

import static minorproject.knowmyself.Other.UserSessionManager.KEY_USERID;
import static minorproject.knowmyself.Other.UserSessionManager.PREFER_NAME;


/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener {

    private PlaceAutocompleteFragment placeAutoComplete;
    private GoogleMap googleMap;
    private LatLng currentPlace;
    private Map<Marker, Dialog> setofPlaces = new HashMap<>();
    private static String currentPlacename;
    private Button continueButton;
    SharedPreferences sharedPreferences;
    private Circle mCircle;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 111;
    protected LocationManager locationManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        if (googleMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        Location loc = new Location("");
        sharedPreferences = getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("Maps", "Place selected: " + place.getName());
                currentPlace = place.getLatLng();
                currentPlacename = (String) place.getName();
                showOnMap(googleMap, currentPlace, (String) place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlace == null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MapsMarkerActivity.this).create();
                    alertDialog.setTitle("Select Place");
                    alertDialog.setMessage("Select any place to add Marker...search in the search box");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    alertAddMarker();
                }
            }
        });
        FloatingActionButton fab_cur_loc = findViewById(R.id.fab_cur_loc);
        fab_cur_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsMarkerActivity.this,"Ohh shitt",Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(MapsMarkerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(MapsMarkerActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
                    if (!hasPermissionLocation) {
                        ActivityCompat.requestPermissions(MapsMarkerActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_ACCESS_FINE_LOCATION);
                    }
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(MapsMarkerActivity.this,"Not getting",Toast.LENGTH_LONG).show();
                    return;
                }
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            }
        });
        continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueButton(view);
                Intent accountsIntent = new Intent(MapsMarkerActivity.this, HomePage.class);//ToDo

                accountsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                //accountsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//toDo
                //accountsIntent.putExtra("EMAIL", _emailText.getText().toString().trim());
                startActivity(accountsIntent);
                finish();
            }
        });
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap mMap) {
        // and move the map's camera to the same location.
        googleMap = mMap;
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    protected void showOnMap(GoogleMap googleMap, LatLng selected_place, String placeName) {

//        Marker marker =  googleMap.addMarker(new MarkerOptions().position(selected_place)
//                .title(placeName).draggable(true));
        currentPlace = selected_place;
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(selected_place));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(selected_place));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(5));
        Log.v("MapsMarkerActivity", "here we go");
        if(placeName.equals("current_place")){
            if(mCircle!=null){
                mCircle.setCenter(selected_place);
            }
            else{
                double radiusInMeters = 100.0;
                int strokeColor = 0xffff0000; //red outline
                int shadeColor = 0x44ff0000; //opaque red fill
                CircleOptions circleOptions = new CircleOptions().center(selected_place).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
                mCircle = googleMap.addCircle(circleOptions);
            }
        }
    }

    protected void RemoveAllMarker() {
        googleMap.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_marker_remove, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.removeMarker:
                RemoveAllMarker();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        //drag start...remove its object from arraylist
        setofPlaces.remove(marker);
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(final Marker marker) {
        setofPlaces.remove(marker);
        currentPlace = marker.getPosition();
        marker.remove();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> list ;
        String loc = "";
        try{
            list = geocoder.getFromLocation(currentPlace.latitude,currentPlace.longitude,1);
            String address = list.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String subLocality = list.get(0).getSubLocality();
            String city = list.get(0).getLocality();
            String knownName = list.get(0).getFeatureName();
            if(address==null){
                if(subLocality==null){
                    if(knownName==null){
                        loc=city;
                    }
                    else
                        loc=knownName;
                }
                else
                    loc=subLocality;
            }
            else
                loc=address;
        }
        catch (IOException e){

        }
        currentPlacename = loc;
        //alert then add marker
        alertAddMarker();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng selected_position = marker.getPosition();
        setofPlaces.get(marker).show();
        return false;
    }

    public void alertAddMarker() {
        try {
            Geocoder geocoder = new Geocoder(MapsMarkerActivity.this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(currentPlace.latitude, currentPlace.longitude, 1);

            final String placeName = currentPlacename;
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(MapsMarkerActivity.this);
            alertdialog.setTitle("Add Marker");
            alertdialog.setMessage("Do you really want to add this place : " + placeName);

            alertdialog.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            addMarker(placeName);
                        }
                    });

            alertdialog.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            currentPlace = null;
                            dialog.cancel();
                        }
                    });
            alertdialog.setCancelable(false);
            alertdialog.show();
        } catch (IOException io) {
            Toast.makeText(MapsMarkerActivity.this, "Error occured...try again", Toast.LENGTH_SHORT);
        }
    }

    public void addMarker(String placeName) {
        LatLng latLng = currentPlace;
        final Marker marker = googleMap.addMarker(new MarkerOptions().position(currentPlace)
                .title("").draggable(true));
        marker.setDraggable(true);

        marker.setTitle(placeName);
        final Dialog dialog = new Dialog(MapsMarkerActivity.this);

        dialog.setContentView(R.layout.dialog_place_details);

        dialog.setTitle("Details");

        final TextView placeDetails = (TextView) dialog.findViewById(R.id.dialog_place_name);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.dialog_radioGroup);
        final EditText outTime = (EditText) dialog.findViewById(R.id.dialog_timePickerOut);
        final EditText inTime = (EditText) dialog.findViewById(R.id.dialog_timePickerIn);
        final Button dialogSaveButton = (Button) dialog.findViewById(R.id.dialog_save_marker);
        Button dialogDeleteButton = (Button) dialog.findViewById(R.id.dialog_remove_marker);

        placeDetails.setText(placeName);
        inTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;


                mTimePicker = new TimePickerDialog(MapsMarkerActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        inTime.setText(" " + hour + ":" + minute);
                        radioGroup.clearCheck();
                        dialogSaveButton.setEnabled(false);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        outTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;


                mTimePicker = new TimePickerDialog(MapsMarkerActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        outTime.setText(" " + hour + ":" + minute);
                        Log.d("Continue","time is selected");
                        radioGroup.clearCheck();
                        dialogSaveButton.setEnabled(false);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        dialogDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.remove();
                dialog.dismiss();
            }
        });
        dialogSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save the marker)

                setofPlaces.put(marker, dialog);
                dialog.dismiss();
                Log.d("CheckHere", "Size of places " + setofPlaces.size());
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("Continue","radiobutton is clicked");
                dialogSaveButton.setEnabled(true);
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        currentPlace = null;

    }


    public void continueButton(View view) {
        Log.d("Continue","Continue button is clicked");
        MapDBHelper mydb = new MapDBHelper(this);
        ArrayList<UserLocation> arrayList = new ArrayList<>();
        int placeType;
        String inTime, outTime;
        LatLng latLng;
        for (Map.Entry<Marker, Dialog> entry : setofPlaces.entrySet()) {
            Marker marker = entry.getKey();
            Dialog dialog = entry.getValue();
            RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.dialog_radioGroup);
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
            String selectedtext = (String) radioButton.getText();
            if (selectedtext.equals("Normal")) placeType = 0;
            else if (selectedtext.equals("AbNormal")) placeType = 1;
            else
                placeType = 2;

            latLng = marker.getPosition();
            EditText editText = (EditText) dialog.findViewById(R.id.dialog_timePickerIn);
            inTime = editText.getText().toString();

            editText = (EditText) dialog.findViewById(R.id.dialog_timePickerOut);
            outTime = editText.getText().toString();

            UserLocation userLocation = new UserLocation();
            userLocation.setInTime(inTime);
            userLocation.setLatLng(latLng);
            userLocation.setLocationType(placeType);
            userLocation.setOutTime(outTime);
            arrayList.add(userLocation);
            String userID = sharedPreferences.getString(KEY_USERID,"");
            mydb.insertContact(userLocation,userID);

            String string = " "+latLng.latitude+" "+latLng.longitude+" "+" "+userLocation.getInTime()+" "
                    +userLocation.getOutTime() +" "+placeType+"\n";
            Log.d("Continue",string);
        }


    }

}