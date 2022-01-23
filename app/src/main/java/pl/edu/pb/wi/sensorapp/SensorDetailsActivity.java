package pl.edu.pb.wi.sensorapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {
    public static final String EXTRA_SENSOR_TYPE_PARAMETER = "EXTRA_SENSOR_TYPE";

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorNameTextView;
    private TextView sensorValueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        sensorNameTextView = findViewById(R.id.details_sensor_name);
        sensorValueTextView = findViewById(R.id.details_sensor_value);

        int sensorType = getIntent().getIntExtra(EXTRA_SENSOR_TYPE_PARAMETER, Sensor.TYPE_PRESSURE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);

        if (sensor == null) {
            sensorNameTextView.setText(R.string.missing_sensor);
        } else {
            sensorNameTextView.setText(sensor.getName());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
        float value = event.values[0];
        int count = event.values.length;
        Log.d("SENSOR_APP_TAG", "Ilość zwracanych wyników" + count);
        Log.d("SENSOR_APP_TAG", "Wywołano onSensorChanged");
        if(type==Sensor.TYPE_AMBIENT_TEMPERATURE && value > 30.00)
        {
            findViewById(R.id.sensor_details).setBackgroundColor(Color.RED);
            sensorNameTextView.setTextColor(Color.WHITE);
            sensorValueTextView.setTextColor(Color.WHITE);
        }
        else if(type==Sensor.TYPE_AMBIENT_TEMPERATURE && value < -20.00)
        {
            findViewById(R.id.sensor_details).setBackgroundColor(Color.BLUE);
            sensorNameTextView.setTextColor(Color.WHITE);
            sensorValueTextView.setTextColor(Color.WHITE);

        }
        else if(type==Sensor.TYPE_AMBIENT_TEMPERATURE && value >= -20.00 && value <= 30.00)
        {findViewById(R.id.sensor_details).setBackgroundColor(Color.WHITE);
            sensorNameTextView.setTextColor(Color.BLACK);
            sensorValueTextView.setTextColor(Color.BLACK);}

        if(type==Sensor.TYPE_PRESSURE && value > 1020.00)
        {
            findViewById(R.id.sensor_details).setBackgroundColor(Color.RED);
            sensorNameTextView.setTextColor(Color.WHITE);
            sensorValueTextView.setTextColor(Color.WHITE);
        }
        else if(type==Sensor.TYPE_PRESSURE && value < 980.00)
        {
            findViewById(R.id.sensor_details).setBackgroundColor(Color.BLUE);
            sensorNameTextView.setTextColor(Color.WHITE);
            sensorValueTextView.setTextColor(Color.WHITE);

        }
        else if(type==Sensor.TYPE_PRESSURE && value >= 980.00 && value <= 1020.00)
        {findViewById(R.id.sensor_details).setBackgroundColor(Color.WHITE);
            sensorNameTextView.setTextColor(Color.BLACK);
            sensorValueTextView.setTextColor(Color.BLACK);}
        switch (type) {
            case Sensor.TYPE_PRESSURE:
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                sensorValueTextView.setText(String.valueOf(value));
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("SENSOR_APP_TAG", "Wywołano onAccuracyChanged");
    }
}
