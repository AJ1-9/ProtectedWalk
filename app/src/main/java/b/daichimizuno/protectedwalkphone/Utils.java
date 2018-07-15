package b.daichimizuno.protectedwalkphone;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Utils {
    private final static String TAG = Utils.class.getSimpleName();

    public static void saveFile(Context context, String fileName,List<String> str){
        Log.d(TAG,"#saveFile :" + fileName + ": str: " +str);
        FileOutputStream fileOutputStream = null;

        try{
            fileOutputStream=context.openFileOutput(fileName, Context.MODE_PRIVATE);
            for(int i=0;i<str.size();i++){
                fileOutputStream.write(str.get(i).getBytes());
                fileOutputStream.write(13);
                fileOutputStream.write(10);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public static float getAverageList(List<Float> list){
        if(list == null){
            return 0f;
        }

        float tmp = 0f;
        for(int i=0;i<list.size();i++){
            tmp += list.get(i);
        }

        float ave = tmp / list.size();

        return ave;
    }


    public static float getMedian(List<Float> floats){
        if(floats.size() > 10){
            Collections.sort(floats);

            float middle_index = floats.size()/2;
            int index = Math.round(middle_index);
            Log.d(TAG,"index : "+ index);

            float middle = floats.get(index);

            return middle;
        }

        return 0f;
    }
}
