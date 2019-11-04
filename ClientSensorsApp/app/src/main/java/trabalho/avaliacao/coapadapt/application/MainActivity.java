package trabalho.avaliacao.coapadapt.application;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    EditText etEntradaContexto;
//    TextView tvRespondeMessage;
//    WifiManager wm;

    //ncoap: https://github.com/okleine/nCoAP
    //simple client ncoap: https://github.com/okleine/nCoAP/tree/master/ncoap-simple-client
    //coap server ncoap: https://github.com/okleine/coSense/
    //https://github.com/okleine/spitfirefox/tree/master/app/src/main/java/de/uzl/itm/ncoap/android/client

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        etEntradaContexto = (EditText)findViewById(R.id.etEntradaContexto);
//        tvRespondeMessage = (TextView)findViewById(R.id.tvRespondeMessage);
//        wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);




        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> availableSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        ListView list = (ListView) findViewById(R.id.listSensors);
        list.setAdapter(new MySensorsAdapter(this, R.layout.row_item, availableSensors));

    }

  /*  public void gravarContextoDesejado(View v){

        String entradaDeContexto=etEntradaContexto.getText().toString();
        tvRespondeMessage.setTextColor(Color.parseColor("#006400"));
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        //String ipTeste = getLocalIpAddress();
        //errado: #B22222
        //certo: #006400
        //tabela rgb http://www.erikasarti.com/html/tabela-cores/
        tvRespondeMessage.setText(entradaDeContexto + ip);

    }*/

/*    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }*/


}
