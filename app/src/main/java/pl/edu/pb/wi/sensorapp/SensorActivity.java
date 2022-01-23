package pl.edu.pb.wi.sensorapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import static pl.edu.pb.wi.sensorapp.SensorDetailsActivity.EXTRA_SENSOR_TYPE_PARAMETER;

public class SensorActivity extends AppCompatActivity {
    private ImageView sensorIconImageView;
    private TextView sensorNameTextView;
    private TextView sensorTypeTextView;
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private RecyclerView recyclerView;
    private final List<Integer> favourSensors = Arrays.asList(Sensor.TYPE_PRESSURE, Sensor.TYPE_AMBIENT_TEMPERATURE);
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_sensor_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String string = getString(R.string.sensors_count, sensorList.size());
        getSupportActionBar().setSubtitle(string);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        sensorList.forEach(sensor -> {
            Log.d("SENSOR_APP_TAG", "Sensor name:" + sensor.getName());
            Log.d("SENSOR_APP_TAG", "Sensor vendor:" + sensor.getVendor());
            Log.d("SENSOR_APP_TAG", "Sensor max range:" + sensor.getMaximumRange());
        });

        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if(adapter==null){
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    private class SensorHolder extends RecyclerView.ViewHolder{

        private Sensor sensor=null;

        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            sensorIconImageView = itemView.findViewById(R.id.imageView);
            sensorNameTextView = itemView.findViewById(R.id.sensor_name);
            sensorTypeTextView = itemView.findViewById(R.id.sensor_type);
        }

        public void bind(Sensor sensor) {
            sensorNameTextView.setText(sensor.getName());
            sensorTypeTextView.setText(String.valueOf(sensor.getType()));
            View itemContainer = itemView.findViewById(R.id.list_item_sensor);
            if (favourSensors.contains(sensor.getType())) {
                itemContainer.setBackgroundColor(getResources().getColor(R.color.favour_item_background));
                itemContainer.setOnClickListener(v -> {
                    Intent intent = new Intent(SensorActivity.this, SensorDetailsActivity.class);
                    intent.putExtra(EXTRA_SENSOR_TYPE_PARAMETER, sensor.getType());
                    startActivityForResult(intent, 1);
                });
            }
        }
    }


    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {
        private final List<Sensor> sensors;
        public SensorAdapter(List<Sensor> items) {
            sensors = items;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new SensorHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(final SensorHolder holder, int position) {
            Sensor sensor = sensorList.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensors.size();
        }


    }

}