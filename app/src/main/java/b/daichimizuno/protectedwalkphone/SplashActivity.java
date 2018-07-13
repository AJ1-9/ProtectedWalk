package b.daichimizuno.protectedwalkphone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.AppLaunchChecker;
import android.util.Log;
import android.view.View;


public class SplashActivity extends Activity {

    Handler mHandler = new Handler();
    private final String preName = "MAIN_SETTING";
    private final String dataIntPreTag = "dataIPT";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;
    private int dataInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // スプラッシュ用のビューを取得する
        setContentView(R.layout.activity_splash);
        findViewById(R.id.imageView).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        sharedPreferences = getSharedPreferences(preName, MODE_PRIVATE);
        dataInt = sharedPreferences.getInt(dataIntPreTag, 0);
        dataInt++;
        edit = sharedPreferences.edit();

        final int user_gender_value;
        final int user_age_value;

        if (dataInt == 1) {
            Log.d("Log0", "初回起動");
            edit.putInt(dataIntPreTag, dataInt).apply();

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), ChoiceActivity.class);
                    intent.putExtra("human_age",0);
                    intent.putExtra("gender",0);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }, 1 * 1000); // 2000ミリ秒後（2秒後）に実行

        } else {
            Log.d("Log0", dataInt + "回目の起動");
            edit.putInt(dataIntPreTag, dataInt).apply();

            SharedPreferences data = getSharedPreferences("UserSet", MODE_PRIVATE);
            user_gender_value = data.getInt("user_gender", 0);
            user_age_value = data.getInt("user_age", 0);

            Log.d("UserSet", user_age_value + ":" + user_gender_value);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("human_age",user_age_value);
                    intent.putExtra("gender",user_gender_value);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }, 1 * 1000); // 2000ミリ秒後（2秒後）に実行
        }
    }
}
