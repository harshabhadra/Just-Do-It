package com.example.android.justdoit.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.justdoit.Adapters.CompletedTaskAdapter;
import com.example.android.justdoit.Model.CompletedTask;
import com.example.android.justdoit.R;
import com.example.android.justdoit.StatefulRecyclerView;
import com.example.android.justdoit.TaskViewModel;

import java.util.List;

public class CompletedTaskActivity extends AppCompatActivity {

    TextView finalTimeHour;
    TextView finalTimeMin;
    StatefulRecyclerView completedRecycler;
    CompletedTaskAdapter completedTaskAdapter;
    TextView completedTaskTv;

    long time;
    long minutes;
    long hour;

    TaskViewModel taskViewModel;

    private String TAG = CompletedTaskActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task);

        finalTimeHour = findViewById(R.id.fina_time_hour);
        finalTimeMin = findViewById(R.id.final_time_min);
        completedTaskTv = findViewById(R.id.completed_task_label);
        completedTaskTv.setBackgroundColor(Color.GREEN);
        completedTaskTv.setTextColor(Color.WHITE);

        //Getting Intent
        Intent intent = getIntent();
        if (intent.hasExtra("taskTime")) {

            long todaysTimeInSec = intent.getLongExtra("taskTime", 0);
            Log.e(TAG,"time from intent: " + todaysTimeInSec);

            SharedPreferences preferences = getSharedPreferences("time", MODE_PRIVATE);
            time = preferences.getLong("sec", 0);


            if (todaysTimeInSec > 0) {
                time = time + todaysTimeInSec;
                SharedPreferences.Editor editor = getSharedPreferences("time", MODE_PRIVATE).edit();
                editor.putLong("sec", time);
                editor.apply();
            }

            Log.e(TAG, "time is : " + time);

            if (time >= 60) {
                minutes = convertToMinute(time);
                if (minutes<60) {
                    finalTimeMin.setText(String.valueOf(minutes));
                }else {

                    convertToHour(minutes);
                }

            }else {
                Log.e(TAG,"input is less than 60");
            }
        }

        //Populating the complete RecyclerView
        completedRecycler = findViewById(R.id.completed_Task_recycler);
        completedRecycler.setHasFixedSize(true);
        completedTaskAdapter = new CompletedTaskAdapter(this);
        completedRecycler.setLayoutManager(new LinearLayoutManager(this));
        completedRecycler.setAdapter(completedTaskAdapter);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getCompletedTaskList().observe(this, new Observer<List<CompletedTask>>() {
            @Override
            public void onChanged(List<CompletedTask> completedTasks) {
                if (completedTasks != null) {
                    completedTaskTv.setText(String.valueOf(completedTasks.size()));
                    completedTaskAdapter.setCompletedTasks(completedTasks);
                } else {
                    completedTaskTv.setText("0");
                }
            }
        });
    }

    //Convert seconds in minute
    private long convertToMinute(long sec) {
        long min = sec/60;
        Log.e(TAG,"Minute: " + min);
        return min;
    }

    //Convert minute to hour
    private void convertToHour(long min) {

        double convertMin = ((double) (min))/60;
        String s = String.valueOf(convertMin);
        String[]split = s.split("\\.");
        finalTimeHour.setText(split[0]);
        finalTimeMin.setText(split[1]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_completed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.delete_all) {

            taskViewModel.deleteAllCompletedTask();
            completedTaskAdapter.removeAll();
            SharedPreferences.Editor editor = getSharedPreferences("time", MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            SharedPreferences preferences = getSharedPreferences("time", MODE_PRIVATE);
            time = preferences.getInt("sec", 0);
            finalTimeHour.setText(String.valueOf(time));
            finalTimeMin.setText(String.valueOf(time));
        }

        return super.onOptionsItemSelected(item);
    }
}
