package b.daichimizuno.protectedwalkphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sdsmdg.harjot.crollerTest.Croller;

public class WomanAgeActivity extends AppCompatActivity implements Croller.onProgressChangedListener, View.OnClickListener {
    TextView textView;
    Button button;
    int progress2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_woman_age);

        textView = findViewById(R.id.text_woman_age);
        button = findViewById(R.id.button_woman_age);
        button.setOnClickListener(this);

        Croller croller = findViewById(R.id.croller_woman);
        croller.setOnProgressChangedListener(this);

        findViewById(R.id.back_woman).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onProgressChanged(int progress) {
        textView.setText("Age:"+ String.valueOf(progress));
        progress2 = progress;
    }

    @Override
    public void onClick(View view) {
        if(view == button){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("human_age",progress2);
            intent.putExtra("gender",2);
            startActivity(intent);
        }
    }
}