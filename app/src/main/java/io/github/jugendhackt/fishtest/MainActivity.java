package io.github.jugendhackt.fishtest;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        //TextView textView = findViewById(R.id.textView);          //Wenn Button gedrückt,
        //textView.setVisibility(TextView.VISIBLE);                 //zeige diesen Text
        //TextView textView = findViewById(R.id.textView);          //Wenn Scan abgeschlossen,
        //textView.setVisibility(TextView.INVISIBLE);               //zeige diesen Text
        //ImageView imageview = findViewById(R.id.imageView);       //Zum Überschreiben wenn
        //imageview.setImageResource(R.drawable.ic_wanzepicred);    //ein Echo gefunden ist
    }
}
