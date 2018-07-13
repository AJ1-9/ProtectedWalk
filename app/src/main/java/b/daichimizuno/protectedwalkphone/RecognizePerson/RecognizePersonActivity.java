package b.daichimizuno.protectedwalkphone.RecognizePerson;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.DetectedActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import b.daichimizuno.protectedwalkphone.R;

public class RecognizePersonActivity extends Activity {
    private final static String TAG = RecognizePersonActivity.class.getSimpleName();

    public static final long DETECTION_INTERVAL_IN_MILLISECONDS = 30 * 1000;
    public static final int CONFIDENCE = 70;
    public static final String BROADCAST_DETECTED_ACTIVITY = "activity_intent";

    private Button btnStartTrcking, btnStopTracking;
    private TextView mRecognitionTextView;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize_person);

        btnStartTrcking = (Button)findViewById(R.id.button_start);
        btnStopTracking = (Button)findViewById(R.id.button_stop);
        mRecognitionTextView = (TextView)findViewById(R.id.recogintion);

        btnStartTrcking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTracking();
            }
        });

        btnStopTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTracking();
            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(BROADCAST_DETECTED_ACTIVITY)) {
                    int type = intent.getIntExtra("type", -1);
                    int confidence = intent.getIntExtra("confidence", 0);

                    if(confidence > CONFIDENCE){
                        mRecognitionTextView.setText(getTypeString(type));
                    }

                }
            }
        };

        startTracking();
    }


    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(BROADCAST_DETECTED_ACTIVITY));
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void startTracking() {
        Intent intent = new Intent(RecognizePersonActivity.this, BackgroundDetectedActivitiesService.class);
        startService(intent);
    }

    private void stopTracking() {
        Intent intent = new Intent(RecognizePersonActivity.this, BackgroundDetectedActivitiesService.class);
        stopService(intent);
    }

    private String getTypeString(int type){
        switch (type){
            case 0:
                return "IN_VEHICLE";
            case 1:
                return "ON_BICYCLE";
            case 2:
                return "ON_FOOT";
            case 3:
                return "STILL";
            case 4:
                return "UNKNOWN";
            case 5:
                return "TILTING";
            case 7:
                return "WALKING";
            case 8:
                return "RUNNING";

        }

        return null;
    }
}