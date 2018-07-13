package b.daichimizuno.protectedwalkphone.gyro;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import b.daichimizuno.protectedwalkphone.BuildConfig;
import b.daichimizuno.protectedwalkphone.R;
import b.daichimizuno.protectedwalkphone.Utils;

import static b.daichimizuno.protectedwalkphone.Utils.getNowDate;

public class GyroActivity extends AppCompatActivity implements SensorEventListener{
    private final static String TAG = GyroActivity.class.getSimpleName();

    private SensorManager mSensorManager;
    private TextView mTextView;
    private TextView mTextViewInfo;

    private List<String> mFileData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyro);

//        init sensorManager
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

//        init textview
        mTextView = (TextView)findViewById(R.id.text_view);
        mTextViewInfo = (TextView)findViewById(R.id.text_info);

    }

    @Override
    protected void onResume(){
        super.onResume();

//        Register Listener
        Sensor gyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if(gyro != null){
            mSensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_UI);
        }else{
            mTextView.setText("No Supported");
        }


    }

    @Override
    protected void onPause(){
        super.onPause();
//        unregsiter Listener
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(BuildConfig.DEBUG){
            Log.d(TAG,"onSensorChanged");
        }

        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            float sensorX = event.values[0];
            float sensorY = event.values[1];
            float sensorZ = event.values[2];

            String strTmp = String.format(Locale.JAPAN, "Gyroscope\n " +
                    "X: %f\n Y: %f\n Z: %f",sensorX, sensorY, sensorZ);

            mTextView.setText(strTmp);
            mFileData.add(getNowDate());
            mFileData.add(strTmp);
            Utils.saveFile(getApplicationContext(),"gyro_log.txt",mFileData);

            showInfo(event);
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

    }
}
