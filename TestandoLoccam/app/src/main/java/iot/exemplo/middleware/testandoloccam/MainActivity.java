package iot.exemplo.middleware.testandoloccam;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements ContextListener {

    View viewBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewBackground = this.getWindow().getDecorView();

        ContextManager.getInstance().connect(this,"LoccamConnection");
        ContextManager.getInstance().registerListener(this);

    }

    public void changeBackground(View v){

        viewBackground.setBackgroundColor(getRandomNumber());


    }

    public void changeBackground(){

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                viewBackground.setBackgroundColor(getRandomNumber());

            }
        });



    }

    public static int getRandomNumber(){
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;

    }

    @Override
    public void onContextReady(String data) {

        System.out.println(data);
        changeBackground();

    }

    @Override
    public String getContextKey() {
        return ContextKeys.PROXIMITY;
    }


    @Override
    protected void onDestroy() {
        ContextManager.getInstance().unregisterListener(this);
        super.onDestroy();

    }
}
