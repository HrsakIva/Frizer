package hr.math.frizer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class NearestSalons extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;
    double latti;
    double longi;
    String lattitude;
    String longitude;
    boolean locationFound;
    Location currentLocation;
    Map<String, String> mapOfDistances;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_salons);

        mapOfDistances = new TreeMap<String, String >();
        LinearLayout ll = (LinearLayout) findViewById(R.id.linearlayoutNearest);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},REQUEST_LOCATION);
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
            //Toast.makeText(this, R.string.turnGPSon, Toast.LENGTH_SHORT).show();
            locationFound = false;
        } else {
            locationFound = getLocation();
        }

        if(locationFound){
            DBAdapter db = new DBAdapter(this);
            db.open();

            Cursor c = db.getAllSalons();
            if(c.moveToFirst()) {
                do {
                    float endLatti = Float.parseFloat(c.getString(7));
                    float endLongi = Float.parseFloat(c.getString(8));

                    Location endLocation = new Location("");
                    endLocation.setLatitude(endLatti);
                    endLocation.setLongitude(endLongi);

                    float distance = currentLocation.distanceTo(endLocation);
                    /*Toast.makeText(this, new StringBuilder().append(String.valueOf(distance)),
                            Toast.LENGTH_LONG).show();*/

                    if (distance < 5000) {
                        mapOfDistances.put(c.getString(0), String.valueOf(distance));
                    }

                    /*Toast.makeText(this, new StringBuilder().append(c.getString(7)).append(" ").append(c.getString(8)),
                            Toast.LENGTH_LONG).show();*/

                } while (c.moveToNext());

                if(!mapOfDistances.isEmpty()){
                    Iterator it = mapOfDistances.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        Cursor salon = db.getSalon(Long.parseLong(pair.getKey().toString()));

                        // showing salons like in mainActivity
                        TextView tvName = new TextView(this);
                        tvName.setText(salon.getString(1));
                        tvName.setTextSize(20);
                        ll.addView(tvName);

                        RatingBar rating = new RatingBar(this, null, android.R.attr.ratingBarStyleSmall);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        rating.setLayoutParams(layoutParams);
                        rating.setNumStars(5);
                        rating.setStepSize((float) 0.1);
                        rating.setIsIndicator(true);
                        float rating_number ;
                        if(Float.parseFloat(salon.getString(11))==0)
                        {
                            rating_number=Float.parseFloat(salon.getString(6))/ (Float.parseFloat(salon.getString(11))+1);
                        }
                        else{
                            rating_number =Float.parseFloat(salon.getString(6))/ (Float.parseFloat(salon.getString(11)));
                        }
                        rating.setRating(rating_number);
                        ll.addView(rating);

                        TextView tvAddress = new TextView(this);
                        tvAddress.setText(salon.getString(2));
                        ll.addView(tvAddress);

                        TextView tvTelNumber = new TextView(this);
                        tvTelNumber.setText(salon.getString(4));
                        ll.addView(tvTelNumber);

                        TextView distanceInfo = new TextView(this);
                        distanceInfo.setText(new StringBuilder().append(String.format("%.2f", Float.parseFloat(pair.getValue().toString()))).append(" m"));
                        ll.addView(distanceInfo);

                        Button btOpen = new Button(this);
                        btOpen.setText(R.string.btOpen);
                        btOpen.setId(Integer.parseInt(salon.getString(0)));
                        ll.addView(btOpen);

                        btOpen.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(NearestSalons.this, SalonActivity.class);
                                Bundle extras = new Bundle();
                                extras.putString("_id", Integer.toString(view.getId()) );
                                i.putExtras(extras);
                                startActivity(i);
                            }
                        });

                        it.remove(); // avoids a ConcurrentModificationException
                    }
                }

                else{
                    TextView tvName = new TextView(this);
                    tvName.setText(R.string.noSalonsNearBy);
                    tvName.setTextSize(20);
                    ll.addView(tvName);
                }
            }

            db.close();
        }

    }
    public boolean getLocation(){
        if(ActivityCompat.checkSelfPermission(NearestSalons.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NearestSalons.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(NearestSalons.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else{
            currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(currentLocation != null){
                latti = currentLocation.getLatitude();
                longi = currentLocation.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                //Toast.makeText(this, new StringBuilder().append(lattitude).append(" ").append(longitude), Toast.LENGTH_SHORT).show();
                return true;
            }

            else{
                //Toast.makeText(this, R.string.noCurrentLocation, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
    }
}


