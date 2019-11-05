package great.ufc.br.mocksensors;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;
import com.google.android.gms.awareness.fence.TimeFence;
import com.google.android.gms.awareness.snapshot.DetectedActivityResponse;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.awareness.snapshot.TimeIntervalsResponse;
import com.google.android.gms.awareness.snapshot.WeatherResponse;
import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;

import great.ufc.br.mocksensors.model.Device;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private Gson gson;
    private ArrayList<Device> devices;
    private LinkedHashMap<String, String> context;
    private static final String COAP_SERVER_URL = "coap://18.229.202.214:5683/devices";

    private String[] myPermissions = {
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.VIBRATE,
        Manifest.permission_group.SENSORS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gson = new Gson();

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(myPermissions,123);
            }
        }else {
            context = new LinkedHashMap<String, String>();
            startSnapshots();
        }



        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> availableSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        devices = createDevices(availableSensors);

        ListView list = (ListView) findViewById(R.id.listSensors);
        ArrayAdapter<Device> adapter = new ArrayAdapter<Device>(this, android.R.layout.simple_list_item_1, devices);
        list.setAdapter(adapter);

        findViewById(R.id.btnSendReq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.btnSendReq){
                     String envText = ((EditText) findViewById(R.id.editText)).getText().toString().trim();
                    if(envText.length() > 0) context.put("env", envText);

                    WifiManager manager = (WifiManager) MainActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo info = manager.getConnectionInfo();
                    String sSID = info.getSSID();
                    if(sSID.length() > 0) context.put("network", sSID);

                    CoapClient client = new CoapClient(COAP_SERVER_URL);
                    for(Device device : devices){
                        device.setContext(context);
                        client.post(gson.toJson(device), MediaTypeRegistry.APPLICATION_JSON);
                    }
                    Toast.makeText(MainActivity.this, "CoAP Request Sent!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startSnapshots(){
        SnapshotClient snapshot = Awareness.getSnapshotClient(this);
        snapshot.getWeather().addOnSuccessListener(new OnSuccessListener<WeatherResponse>() {
            @Override
            public void onSuccess(WeatherResponse weatherResponse) {
                context.put("temperature", String.format("%.2f", weatherResponse.getWeather().getTemperature(Weather.CELSIUS)));
                context.put("humidity", weatherResponse.getWeather().getHumidity() + "");
                context.put("feels", String.format("%.2f", weatherResponse.getWeather().getFeelsLikeTemperature(Weather.CELSIUS)));
            }
        });

        snapshot.getLocation().addOnSuccessListener(new OnSuccessListener<LocationResponse>() {
            @Override
            public void onSuccess(LocationResponse locationResponse) {
                context.put("latitude", locationResponse.getLocation().getLatitude() + "");
                context.put("longitude", locationResponse.getLocation().getLongitude() + "");
            }
        });

        snapshot.getDetectedActivity().addOnSuccessListener(new OnSuccessListener<DetectedActivityResponse>() {
            @Override
            public void onSuccess(DetectedActivityResponse detectedActivityResponse) {
                String detectedActivity = null;

                switch (detectedActivityResponse.getActivityRecognitionResult().getMostProbableActivity().getType()){
                    case DetectedActivity.IN_VEHICLE: detectedActivity = "inVehicle"; break;
                    case DetectedActivity.ON_BICYCLE: detectedActivity = "onBicycle"; break;
                    case DetectedActivity.ON_FOOT:    detectedActivity = "onFoot"; break;
                    case DetectedActivity.STILL:      detectedActivity = "still"; break;
                    case DetectedActivity.TILTING:    detectedActivity = "tilting"; break;
                    case DetectedActivity.WALKING:    detectedActivity = "walking"; break;
                    case DetectedActivity.RUNNING:    detectedActivity = "running"; break;
                    default: detectedActivity = "unknown";
                }

                context.put("detectedActivity", detectedActivity);
            }
        });

        snapshot.getTimeIntervals().addOnSuccessListener(new OnSuccessListener<TimeIntervalsResponse>() {
            @Override
            public void onSuccess(TimeIntervalsResponse timeIntervalsResponse) {
                String interval = null;
                int[] intervals = timeIntervalsResponse.getTimeIntervals().getTimeIntervals();

                switch (intervals[0]){
                    case TimeFence.TIME_INTERVAL_WEEKDAY: interval = "weekday_"; break;
                    case TimeFence.TIME_INTERVAL_WEEKEND: interval = "weekend_"; break;
                    case TimeFence.TIME_INTERVAL_HOLIDAY: interval = "holiday_"; break;
                    default: interval = "unknown";
                }
                switch (intervals[1]){
                    case TimeFence.TIME_INTERVAL_MORNING:   interval += "morning"; break;
                    case TimeFence.TIME_INTERVAL_AFTERNOON: interval += "afternoon"; break;
                    case TimeFence.TIME_INTERVAL_EVENING:   interval += "evening"; break;
                    case TimeFence.TIME_INTERVAL_NIGHT:     interval += "night"; break;
                    default: interval = "unknown";
                }

                context.put("timeInterval", interval);
                Toast.makeText(MainActivity.this, "Context ok!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<Device> createDevices(List<Sensor> sensors){
        ArrayList<String> uuids = new ArrayList<>();
        ArrayList<Device> devices = new ArrayList<>();

        for(Sensor sensor : sensors){
            if(sensor != null
                    && !(sensor.getStringType().toLowerCase().contains("não calibrado") || sensor.getStringType().toLowerCase().contains("uncalibrated"))
                    && sensorTypeToString(sensor.getType()) != "Unknown"){

                String uid = String.valueOf(System.currentTimeMillis());
                String rType = sensorTypeToString(sensor.getType());
                String type = (rType.equals("Light") || rType.equals("AmbientTemperature")) ? "actuator" : "sensor";
                String ip = getLocalIpAddress();

                if(!uuids.contains(uid)){
                    devices.add(new Device(uid, type, rType, MediaTypeRegistry.APPLICATION_JSON, ip));
                }
            }
        }

        return devices;
    }

    private String sensorTypeToString(int sensorType) {
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                return "Accelerometer";
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                return "AmbientTemperature";
            case Sensor.TYPE_TEMPERATURE:
                return "AmbientTemperature";
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                return "GameRotationVector";
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                return "GeomagneticRotationVector";
            case Sensor.TYPE_GRAVITY:
                return "Gravity";
            case Sensor.TYPE_GYROSCOPE:
                return "Gyroscope";
            case Sensor.TYPE_HEART_RATE:
                return "HeartRateMonitor";
            case Sensor.TYPE_LIGHT:
                return "Light";
            case Sensor.TYPE_LINEAR_ACCELERATION:
                return "LinearAcceleration";
            case Sensor.TYPE_MAGNETIC_FIELD:
                return "MagneticField";
            case Sensor.TYPE_ORIENTATION:
                return "Orientation";
            case Sensor.TYPE_PRESSURE:
                return "Pressure";
            case Sensor.TYPE_PROXIMITY:
                return "Proximity";
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                return "Humidity";
            case Sensor.TYPE_ROTATION_VECTOR:
                return "RotationVector";
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                return "SignificantMotion";
            case Sensor.TYPE_STEP_COUNTER:
                return "StepCounter";
            case Sensor.TYPE_STEP_DETECTOR:
                return "StepDetector";
            default:
                return "Unknown";
        }
    }

    private String getLocalIpAddress() {
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
    }
}
