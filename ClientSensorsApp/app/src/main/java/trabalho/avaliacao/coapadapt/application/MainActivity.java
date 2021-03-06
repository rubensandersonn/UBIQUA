package trabalho.avaliacao.coapadapt.application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.snapshot.DetectedActivityResponse;
import com.google.android.gms.awareness.snapshot.LocationResponse;
import com.google.android.gms.awareness.snapshot.TimeIntervalsResponse;
import com.google.android.gms.awareness.snapshot.WeatherResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.SnapshotClient;

import trabalho.avaliacao.coapadapt.model.Device;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LinkedHashMap<String, String> context = new LinkedHashMap<String, String>();
    private String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.VIBRATE,
            Manifest.permission_group.SENSORS
    };

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissions(permissions, 123);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        startSnapshots();

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> availableSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        ArrayList<Device> devices = createDevices(availableSensors);

        ListView list = (ListView) findViewById(R.id.listSensors);
        ArrayAdapter<Device> adapter = new ArrayAdapter<Device>(this, android.R.layout.simple_list_item_1, devices);
        list.setAdapter(adapter);

        Gson gson = new Gson();
        CoapClient client = new CoapClient("coap://18.229.202.214:5683/devices");
        for(Device device : devices){
            client.post(gson.toJson(device), MediaTypeRegistry.APPLICATION_JSON);
        }
    }

    private LinkedHashMap<String, String> startSnapshots(){
        SnapshotClient snapshot = Awareness.getSnapshotClient(this);
        snapshot.getLocation().addOnSuccessListener(new OnSuccessListener<LocationResponse>() {
            @Override
            public void onSuccess(LocationResponse locationResponse) {
                context.put("location", locationResponse.getLocation().toString());
                Toast.makeText(MainActivity.this, context.get("location"), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                Log.e("location", e.toString());
            }
        });

        snapshot.getDetectedActivity().addOnSuccessListener(new OnSuccessListener<DetectedActivityResponse>() {
            @Override
            public void onSuccess(DetectedActivityResponse detectedActivityResponse) {
                context.put("detectedActivity", detectedActivityResponse.getActivityRecognitionResult().getMostProbableActivity().toString());
                Toast.makeText(MainActivity.this, context.get("detectedActivity"), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                Log.e("detectedActivity", e.toString());
            }
        });

        snapshot.getTimeIntervals().addOnSuccessListener(new OnSuccessListener<TimeIntervalsResponse>() {
            @Override
            public void onSuccess(TimeIntervalsResponse timeIntervalsResponse) {
                context.put("timeInterval", timeIntervalsResponse.getTimeIntervals().toString());
                Toast.makeText(MainActivity.this, context.get("timeInterval"), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                Log.e("timeInterval", e.toString());
            }
        });

        return context;
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

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(MainActivity.this, "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
