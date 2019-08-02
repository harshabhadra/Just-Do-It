package com.example.android.justdoit.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.justdoit.Adapters.SavedTaskAdapter;
import com.example.android.justdoit.Model.CompletedTask;
import com.example.android.justdoit.Model.TaskItem;
import com.example.android.justdoit.R;
import com.example.android.justdoit.StatefulRecyclerView;
import com.example.android.justdoit.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements SavedTaskAdapter.OnSavedItemClickListener {

    TextView todayTextView;
    TextView dateTextView;
    TextView monthTextView;
    TextView noOfTaskTextView;
    TextView workInProgressLabel;
    CardView workInProgressCard;
    TextView currentTaskTextView;
    FloatingActionButton fab;
    Button stopButton;
    Chronometer chronometer;

    private String TAG = MainActivity.class.getSimpleName();

    StatefulRecyclerView savedTaskRecycler;
    TaskViewModel taskViewModel;
    SavedTaskAdapter savedTaskAdapter;
    long elaspedMilliSeconds = 0;

    int noOfTask = 0;
    boolean isTimerRunning = false;
    long pauseOffset;
    long sec = 0;
    long tasktimeinSec = 0;
    String notificationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.e(TAG, "onCreate");

        todayTextView = findViewById(R.id.today);
        dateTextView = findViewById(R.id.today_date);
        monthTextView = findViewById(R.id.current_month);
        noOfTaskTextView = findViewById(R.id.no_of_tasks);
        workInProgressCard = findViewById(R.id.work_in_progress_card);
        workInProgressLabel = findViewById(R.id.task_in_progress_label);
        currentTaskTextView = findViewById(R.id.current_task_name);
        stopButton = findViewById(R.id.chronometer_button);
        fab = findViewById(R.id.fab);
        chronometer = findViewById(R.id.chronometer_timer);

        workInProgressLabel.setVisibility(View.GONE);
        workInProgressCard.setVisibility(View.GONE);
        stopButton.setBackgroundColor(Color.RED);

        savedTaskRecycler = findViewById(R.id.saved_task_recycler);
        savedTaskAdapter = new SavedTaskAdapter(MainActivity.this, MainActivity.this);
        savedTaskRecycler.setHasFixedSize(true);
        savedTaskRecycler.setLayoutManager(new LinearLayoutManager(this));
        savedTaskRecycler.setAdapter(savedTaskAdapter);

        Intent intent = getIntent();
        if (intent.hasExtra("startNow")) {
            workInProgressLabel.setVisibility(View.VISIBLE);
            workInProgressCard.setVisibility(View.VISIBLE);
            savedTaskRecycler.setVisibility(View.GONE);
            TaskItem taskItem = intent.getParcelableExtra("startNow");
            if (taskItem != null) {
                currentTaskTextView.setText(taskItem.getTaskName());
            }
            startChronometer();
            fab.setEnabled(false);
        }

        String ddMMyyyy = getDate();
        String[] split = ddMMyyyy.split("-");

        //Getting the current date
        String mDate = split[0];

        int iDate = Integer.parseInt(mDate);
        String mMonth = split[1];
        int iMonth = Integer.parseInt(mMonth);
        int year = Integer.parseInt(split[2]);

        //Getting the current day
        String Day = calculateDay(iDate, iMonth, year);

        //Getting the current month
        String monthName = calculateMonth(iDate, iMonth, year);

        //Setting up Current Day, Date & Month
        todayTextView.setText(Day);
        dateTextView.setText(mDate);
        monthTextView.setText(monthName);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getTaskList().observe(this, new Observer<List<TaskItem>>() {
            @Override
            public void onChanged(List<TaskItem> taskItems) {

                if (!taskItems.isEmpty()) {

                    //Setting up no of task
                    noOfTask = taskItems.size();
                    noOfTaskTextView.setText(String.valueOf(noOfTask));

                    //populating the savedTaskRecyclerView
                    savedTaskAdapter.setTaskList(taskItems);
                    Log.e("MainActivity", "Task are here");
                    Toast.makeText(getApplicationContext(), "Tasks are here", Toast.LENGTH_SHORT).show();
                } else {
                    noOfTask = 0;
                    noOfTaskTextView.setText(String.valueOf(noOfTask));
                    Log.e("MainActivity", "Empty list");
                    Toast.makeText(getApplicationContext(), "Empty list", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Setup stop button to stop Chronometer
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setEnabled(true);
                workInProgressLabel.setVisibility(View.GONE);
                workInProgressCard.setVisibility(View.GONE);
                tasktimeinSec = tasktimeinSec + sec;
                Log.e("MainActivity", "taskTime: " + tasktimeinSec);
                SharedPreferences.Editor editor = getSharedPreferences("record",MODE_PRIVATE).edit();
                editor.putLong("sec",tasktimeinSec);
                editor.apply();
                stopChronometer();
                resetChronometer();
                savedTaskRecycler.setVisibility(View.VISIBLE);

            }
        });

        swipeRecyclerView();

        //Calculation the difference between start time and end time
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                elaspedMilliSeconds =+ SystemClock.elapsedRealtime() - chronometer.getBase();
                sec = TimeUnit.MILLISECONDS.toSeconds(elaspedMilliSeconds);
                Log.e("MainActivity: ", " " + sec);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(addTaskIntent);
            }
        });
    }

    //Set swipe functionality to the RecyclerView
    public void swipeRecyclerView(){

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                if (direction == ItemTouchHelper.LEFT) {
                    int position = viewHolder.getAdapterPosition();
                    TaskItem taskItem = savedTaskAdapter.getTask(position);
                    taskViewModel.deleteSingleTask(taskItem);
                    savedTaskAdapter.removeItem(position);

                } else if (direction == ItemTouchHelper.RIGHT) {
                    int position = viewHolder.getAdapterPosition();
                    TaskItem taskItem = savedTaskAdapter.getTask(position);
                    savedTaskAdapter.removeItem(position);
                    savedTaskRecycler.setVisibility(View.INVISIBLE);

                    workInProgressLabel.setVisibility(View.VISIBLE);
                    workInProgressCard.setVisibility(View.VISIBLE);
                    currentTaskTextView.setText(taskItem.getTaskName());

                    CompletedTask completedTask = new CompletedTask(taskItem.getCategoryImage(), taskItem.getTaskName(), taskItem.getTaskDescription(), taskItem.getTimeinSeconds());
                    taskViewModel.insertCompletedTask(completedTask);
                    fab.setEnabled(false);
                    startChronometer();
                    taskViewModel.deleteSingleTask(taskItem);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(MainActivity.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftActionIcon(R.drawable.ic_delete)
                        .addSwipeLeftBackgroundColor(Color.RED)
                        .addSwipeLeftLabel("Delete")
                        .setSwipeLeftLabelColor(Color.WHITE)
                        .addSwipeRightActionIcon(R.drawable.ic_play_circle_filled_black_24dp)
                        .addSwipeRightBackgroundColor(Color.GREEN)
                        .addSwipeRightLabel("Start")
                        .setSwipeRightLabelColor(Color.WHITE)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(savedTaskRecycler);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        SharedPreferences preferences = getSharedPreferences("record",MODE_PRIVATE);
        tasktimeinSec = preferences.getLong("sec",0);
        Log.e("MainAcitvity" , "taskTime: " + tasktimeinSec);
    }

    //Stop the Chronometer
    private void stopChronometer() {

        if (isTimerRunning) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            Log.e("MainActivity", "PauseOffset: " + pauseOffset);
            isTimerRunning = false;
        }
    }

    //Start the Chronometer
    private void startChronometer() {

        if (!isTimerRunning) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            isTimerRunning = true;
        }
    }

    //Reset Chronometer
    private void resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    //Get Current Date
    private String getDate() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

    }

    //Calculate current Day
    private String calculateDay(int dd, int mm, int yyyy) {

        Date date = (new GregorianCalendar(yyyy, mm, dd)).getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        return sdf.format(date);

    }

    //Calculate current Month
    private String calculateMonth(int dd, int mm, int yyyy) {

        Date date = (new GregorianCalendar(yyyy, mm, dd)).getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //Open Today's Task Reports
        if (!isTimerRunning)
        if (id == R.id.completed_Task) {

            Intent intent = new Intent(MainActivity.this, CompletedTaskActivity.class);
            intent.putExtra("taskTime",tasktimeinSec);
            SharedPreferences.Editor editor = getSharedPreferences("record",MODE_PRIVATE).edit();
            editor.remove("sec");
            editor.clear();
            editor.putLong("sec",0);
            editor.apply();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSavedItemClick(int position) {

        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
        intent.putExtra("savedTask", savedTaskAdapter.getTask(position));
        startActivity(intent);
    }
}