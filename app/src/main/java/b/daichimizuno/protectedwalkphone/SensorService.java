package b.daichimizuno.protectedwalkphone;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import b.daichimizuno.protectedwalkphone.camera.MediaUtils;

public class SensorService extends Service implements SensorEventListener2{
    private final static String TAG = SensorService.class.getSimpleName();

    private SensorManager mSensorManager;
    private Sensor mArroundElectronicsSensor;
    private Sensor mAccelerationSensor;
    private Sensor mGyroSensor;

    private float light = 0f;
    private List<Float> gyroDataPlusList = new ArrayList<>();
    private List<Float> gyroDataMinuxList = new ArrayList<>();
    private int gyroDataMaxNumber = 300;
    private List<String> textList = new ArrayList<>();

    private static boolean isWalkingForSensor = false;

    public SensorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");


    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
//        to do something

        textList.add("detect");
        MediaUtils.initPlayer(this);
        initSensorManager();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"#onDestroy");
        //mSensorManager.unregisterListener(this,mArroundElectronicsSensor);
        //mSensorManager.unregisterListener(this,mAccelerationSensor);
        //mSensorManager.unregisterListener(this,mGyroSensor);
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    private void initSensorManager(){
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        if(mSensorManager != null){
            mArroundElectronicsSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            mAccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

            if(mArroundElectronicsSensor != null){
                mSensorManager.registerListener(this,mArroundElectronicsSensor,
                        SensorManager.SENSOR_DELAY_UI);
            }

            if(mAccelerationSensor != null){
//                mSensorManager.registerListener(this,mAccelerationSensor,
//                        SensorManager.SENSOR_DELAY_UI);
            }

            if(mGyroSensor != null){
                mSensorManager.registerListener(this,mGyroSensor,
                        SensorManager.SENSOR_DELAY_UI);
            }

        }

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        String strTmp = null;

        if(event.sensor.getStringType() == Sensor.STRING_TYPE_LIGHT){
            strTmp ="Light : " +event.values[0];
            Log.d(TAG,"#onSensorChanged : Lignt : " +event.values[0]);
            light = event.values[0];
        }

        else if(event.sensor.getStringType() == Sensor.STRING_TYPE_ACCELEROMETER){
            strTmp = String.format(Locale.JAPAN, "Acceleration\n " +
                    "X: %f\n Y: %f\n Z: %f",event.values[0], event.values[1], event.values[2]);

            Log.d(TAG,"#onSensorChanged : Acceleration : " +strTmp);

        }

        else if(event.sensor.getStringType() == Sensor.STRING_TYPE_GYROSCOPE){
            float sensorX = event.values[0];
            float sensorY = event.values[1];
            float sensorZ = event.values[2];

            strTmp = String.format(Locale.JAPAN, "Gyroscope\n " +
                    "X: %f\n Y: %f\n Z: %f",sensorX, sensorY, sensorZ);

            Log.d(TAG,"#onSensorChanged : Gyro : " + strTmp);

            if(sensorX > 0f && sensorX < 1f){
                gyroDataPlusList.add(sensorX);
            }else if(sensorX > -1f && sensorX < 0f){
                gyroDataMinuxList.add(sensorX);
            }

            if(gyroDataPlusList.size() > gyroDataMaxNumber){
                gyroDataPlusList.remove(0);
            }

            if(gyroDataMinuxList.size() > gyroDataMaxNumber){
                gyroDataMinuxList.remove(0);
            }
        }

        textList.add(strTmp);
//        Utils.saveFile(this,"text.txt" ,textList);
        Log.d(TAG,"Median plus :" + Utils.getMedian(gyroDataPlusList));
        Log.d(TAG,"Median minus :" + Utils.getMedian(gyroDataMinuxList));

        Log.d(TAG,"exceed : Light :"+ light);
        if(light > THRESHOLD.ARROUND_ELECTORICS){
            if(gyroDataPlusList.size() >  gyroDataMaxNumber-5 && Utils.getMedian(gyroDataPlusList) > THRESHOLD.GYRO_PLUS){
                Log.d(TAG,"plus ok : status "+ MediaUtils.getPlayerStatus());
                if(!MediaUtils.getPlayerStatus()){
                    MediaUtils.startPlayer();
                    gyroDataPlusList.clear();
                    gyroDataMinuxList.clear();
                    textList.add("detect");
//                    Utils.saveFile(this,"test.txt",textList);
//                    isWalkingForSensor = true;

                    MainActivity.lockMeNow();
                }
            }


            else if(gyroDataMinuxList.size() > gyroDataMaxNumber-5 && Utils.getMedian(gyroDataMinuxList) < THRESHOLD.GYRO_MINUX){
                Log.d(TAG,"minus ok : status "+ MediaUtils.getPlayerStatus());
                if(!MediaUtils.getPlayerStatus()){
                    MediaUtils.startPlayer();
                    gyroDataPlusList.clear();
                    gyroDataMinuxList.clear();
                    textList.add("detect");
//                    Utils.saveFile(this,"test.txt",textList);
//                    isWalkingForSensor = true;

                    MainActivity.lockMeNow();
                }
            }else{
                isWalkingForSensor = false;
            }
        }else{
            isWalkingForSensor = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG,"#onAccurancyChanged " + sensor.getStringType());
        Log.d(TAG,"#onAccurancyChanged " + accuracy);
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {
        Log.d(TAG,"#onFlushCOmpleted :" +sensor.getStringType());
    }


    public static boolean isWalking(){
        return isWalkingForSensor;
    }
}
