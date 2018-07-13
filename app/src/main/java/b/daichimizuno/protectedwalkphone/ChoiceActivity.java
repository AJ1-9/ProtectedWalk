package b.daichimizuno.protectedwalkphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
//import android.util.Log;

public class ChoiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        findViewById(R.id.textView).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        // SeekBar
        SeekBar seekBar = findViewById(R.id.seekBar);
        // 初期値
        seekBar.setProgress(50);
        // 最大値
        seekBar.setMax(100);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            //ツマミがドラッグされると呼ばれる
            @Override
            public void onProgressChanged(
                    SeekBar seekbar, int progress, boolean fromUser) {
                //Log.d("log", "progress : " + progress);
                if (progress == 0) {
                    Intent intent = new Intent(getApplicationContext(),ManAgeActivity.class);
                    intent.putExtra("gender",0);
                    startActivity(intent);
                    ChoiceActivity.this.finish();
                }
                else if (progress==100) {
                    Intent intent = new Intent(getApplicationContext(),WomanAgeActivity.class);
                    intent.putExtra("gender",1);
                    startActivity(intent);
                    ChoiceActivity.this.finish();
                }
            }
            //ツマミがタッチされた時に呼ばれる
            public void onStartTrackingTouch(SeekBar seekbar) {}
            //ツマミがリリースされた時に呼ばれる
            @Override
            public void onStopTrackingTouch(SeekBar seekbar) {}
        });

    }
}