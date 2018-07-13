package b.daichimizuno.protectedwalkphone.gps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import b.daichimizuno.protectedwalkphone.R;

public class GPSActivity extends AppCompatActivity implements LocationListener{
    private final static String TAG = GPSActivity.class.getSimpleName();

    private static LocationManager mLocationManager;
    private TextView mLatitudeTextView;
    private TextView mLongtudeTextView;

    private boolean isStartingRequestLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        mLatitudeTextView = (TextView)findViewById(R.id.Latitude);
        mLongtudeTextView = (TextView)findViewById(R.id.Longtude);

        if(isLocationManager()){
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1);

                Log.d(TAG, "checkSelfPermission false");

                return;
            }

            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,1000,5,this
            );

        }
    }

    public Boolean isLocationManager(){
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(mLocationManager != null){
            return true;
        }else{
            Log.w(TAG,"Location Manager not supported");
            return false;
        }
    }

    @Override
    public void onPause(){
        super.onPause();

    }



    @Override
    public void onLocationChanged(Location location) {
        mLatitudeTextView.setText("Latitude" + String.valueOf(location.getLatitude()));
        mLongtudeTextView.setText("Longtude" + String.valueOf(location.getLongitude()));

        Log.d(TAG,"latitude :" + location.getLatitude());
        Log.d(TAG,"longtude :" + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG,"#onStatusChanged : " + provider);
        if(status == LocationProvider.AVAILABLE) {
            Log.d(TAG, "LocationProvider Available");
        }
        else if(status == LocationProvider.OUT_OF_SERVICE){
            Log.d(TAG,"LocationProvider Out of Service");
        }
        else if(status == LocationProvider.TEMPORARILY_UNAVAILABLE){
            Log.d(TAG,"LocationProvder Temporary Unavalilable");
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
