package io.github.jugendhackt.fishtest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        //ImageView imageview = findViewById(R.id.imageView);       //Zum Überschreiben wenn
        //imageview.setImageResource(R.drawable.ic_wanzepicred);  //ein Echo gefunden ist
    }
}
