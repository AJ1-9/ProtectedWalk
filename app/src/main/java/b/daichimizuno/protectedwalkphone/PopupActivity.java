package b.daichimizuno.protectedwalkphone;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;


public class PopupActivity extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this, "Toast example", Toast.LENGTH_LONG).show();

    }
}