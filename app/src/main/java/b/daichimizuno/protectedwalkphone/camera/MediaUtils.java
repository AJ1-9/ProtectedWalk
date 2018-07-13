package b.daichimizuno.protectedwalkphone.camera;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import b.daichimizuno.protectedwalkphone.MainActivity;

/**
 * Created by daichimizuno on 2018/04/27.
 */

public class MediaUtils{
    private final static String TAG = MediaUtils.class.getSimpleName();

    private static MediaPlayer mMediaPlayer;
    private static AssetFileDescriptor mAssetFileDescriptor;

    public static void initPlayer(Context context) {

        int age = MainActivity.getAge();
        int gender = MainActivity.getGender();

        try {
            if (gender == 1) {
                if (age < 15) {
                    mAssetFileDescriptor = context.getAssets().openFd("man_voice1.m4a");
                } else if (age < 40) {
                    mAssetFileDescriptor = context.getAssets().openFd("man_voice2.m4a");
                } else {
                    mAssetFileDescriptor = context.getAssets().openFd("man_voice3.m4a");
                }
            } else if (gender == 2) {
                mAssetFileDescriptor = context.getAssets().openFd("woman_voice.m4a");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        initMediaPlayer(mAssetFileDescriptor);
    }

    private static void initMediaPlayer(AssetFileDescriptor assetFileDescriptor){
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.d(TAG,"MediaPlayer compileted");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startPlayer(){
        if(!mMediaPlayer.isPlaying()){
            try {
                mMediaPlayer.setVolume(100,100);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean getPlayerStatus(){
        return mMediaPlayer.isPlaying();
    }

    public static void stopPlayer(){
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
    }

    public static void releaesPlayer(){
        if(mMediaPlayer != null){
            mMediaPlayer.release();
        }
    }

    public static void saveBitmap(Bitmap saveImage) throws IOException {

        final String SAVE_DIR = "/MyPhoto/";
        File file = new File(Environment.getExternalStorageDirectory().getPath() + SAVE_DIR);
        try{
            if(!file.exists()){
                file.mkdir();
            }
        }catch(SecurityException e){
            e.printStackTrace();
            throw e;
        }

        Date mDate = new Date();
        SimpleDateFormat fileNameDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = fileNameDate.format(mDate) + ".jpg";
        String AttachName = file.getAbsolutePath() + "/" + fileName;

        try {
            FileOutputStream out = new FileOutputStream(AttachName);
            saveImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch(IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
