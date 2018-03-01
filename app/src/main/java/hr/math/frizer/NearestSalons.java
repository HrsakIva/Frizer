package hr.math.frizer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearestSalons extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;
    double latti;
    double longi;
    String lattitude;
    String longitude;
    boolean locationFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_salons);

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
            //DBAdapter db =
        }
    }
    public boolean getLocation(){
        if(ActivityCompat.checkSelfPermission(NearestSalons.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NearestSalons.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(NearestSalons.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else{
            Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(currentLocation != null){
                latti = currentLocation.getLatitude();
                longi = currentLocation.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                Toast.makeText(this, new StringBuilder().append(lattitude).append(" ").append(longitude), Toast.LENGTH_SHORT).show();
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


