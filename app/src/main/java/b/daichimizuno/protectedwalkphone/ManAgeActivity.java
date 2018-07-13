package b.daichimizuno.protectedwalkphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sdsmdg.harjot.crollerTest.Croller;

public class ManAgeActivity extends AppCompatActivity implements Croller.onProgressChangedListener, View.OnClickListener {
    TextView textView;
    Button button;
    int progress1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_age);

        textView = findViewById(R.id.text_man_age);
        button = findViewById(R.id.button_man_age);
        button.setOnClickListener(this);

        Croller croller = findViewById(R.id.croller_man);
        croller.setOnProgressChangedListener(this);

        findViewById(R.id.back_man).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onProgressChanged(int progress) {
        textView.setText("Age:" + String.valueOf(progress));
        progress1 = progress;
    }

    @Override
    public void onClick(View view) {
        if(view == button){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("human_age",progress1);
            intent.putExtra("gender",1);
            startActivity(intent);
        }
    }
}