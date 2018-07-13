package b.daichimizuno.protectedwalkphone.acceleration;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import b.daichimizuno.protectedwalkphone.R;
import b.daichimizuno.protectedwalkphone.Utils;

import static b.daichimizuno.protectedwalkphone.Utils.getNowDate;

public class AccelerationActivity extends AppCompatActivity implements SensorEventListener{
    private final static String TAG = AccelerationActivity.class.getSimpleName();

    private static SensorManager mSensorManager;

    private TextView mSensorXTextView;
    private TextView mSensorYTextView;
    private TextView mSensorZTextView;
    private TextView mTextViewInfo;

    private List<String> mFileData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceleration);

        mSensorXTextView = (TextView)findViewById(R.id.sensorX);
        mSensorYTextView = (TextView)findViewById(R.id.sensorY);
        mSensorZTextView = (TextView)findViewById(R.id.sensorZ);
        mTextViewInfo =(TextView)findViewById(R.id.text_info);

        initSensorManager();
    }

    private void initSensorManager(){
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mSensorManager != null){
            Sensor acceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, acceleration,
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d(TAG,"onSensorChanged");

        showInfo(event);

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            String strTmp = String.format(Locale.JAPAN, "Acceleration\n " +
                    "X: %f\n Y: %f\n Z: %f",event.values[0], event.values[1], event.values[2]);

            mSensorXTextView.setText("X : " + event.values[0]);
            mSensorYTextView.setText("Y : " + event.values[1]);
            mSensorZTextView.setText("Z : " + event.values[2]);

            mFileData.add(getNowDate());
            mFileData.add(strTmp);
            Utils.saveFile(getApplicationContext(),"acceleration_log.txt",mFileData);

        }
    }

    // センサーの各種情報を表示する
    private void showInfo(SensorEvent event){
        // センサー名
        StringBuffer info = new StringBuffer("Name: ");
        info.append(event.sensor.getName());
        info.append("\n");

        // ベンダー名
        info.append("Vendor: ");
        info.append(event.sensor.getVendor());
        info.append("\n");

        // 型番
        info.append("Type: ");
        info.append(event.sensor.getType());
        info.append("\n");

        // 最小遅れ
        int data = event.sensor.getMinDelay();
        info.append("Mindelay: ");
        info.append(String.valueOf(data));
        info.append(" usec\n");

        // 最大遅れ
        data = event.sensor.getMaxDelay();
        info.append("Maxdelay: ");
        info.append(String.valueOf(data));
        info.append(" usec\n");

        // レポートモード
        data = event.sensor.getReportingMode();
        String stinfo = "unknown";
        if(data == 0){
            stinfo = "REPORTING_MODE_CONTINUOUS";
        }else if(data == 1){
            stinfo = "REPORTING_MODE_ON_CHANGE";
        }else if(data == 2){
            stinfo = "REPORTING_MODE_ONE_SHOT";
        }
        info.append("ReportingMode: ");
        info.append(stinfo);
        info.append("\n");

        // 最大レンジ
        info.append("MaxRange: ");
        float fData = event.sensor.getMaximumRange();
        info.append(String.valueOf(fData));
        info.append("\n");

        // 分解能
        info.append("Resolution: ");
        fData = event.sensor.getResolution();
        info.append(String.valueOf(fData));
        info.append(" m/s^2\n");

        // 消費電流
        info.append("Power: ");
        fData = event.sensor.getPower();
        info.append(String.valueOf(fData));
        info.append(" mA\n");

        mTextViewInfo.setTextColor(Color.WHITE);
        mTextViewInfo.setText(info);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG,"onAccurancyChanged");

    }
}
