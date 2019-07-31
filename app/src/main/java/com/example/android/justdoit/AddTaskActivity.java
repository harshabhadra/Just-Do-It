package com.example.android.justdoit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddTaskActivity extends AppCompatActivity implements CategoryAdapter.onCategoryItemClickListener {


    RecyclerView categoryRecyclerView;
    CategoryAdapter categoryAdapter;
    ConstraintLayout addTaskTemplateLayout;
    TextView chooseCategoryTV;
    Button startButton;
    Button saveTaskButton;
    TextView timerLabelTv;
    CardView timerCard;
    Button saveButton;
    ImageView taskImage;
    Spinner timerSpinner;
    EditText taskName;
    EditText taskDescription;
    EditText hourEditText;
    EditText minuteEditText;

    TaskViewModel taskViewModel;

    String time;
    String amOrpm = "AM";
    int image;
    String hh;
    String mm;
    boolean isYesButtonClicked = false;

    String currentTime;
    String timeSetByUser;
    int DEFAULT_TASK_ID = -1;
    int taskId = DEFAULT_TASK_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setTitle("Add New Task");

        addTaskTemplateLayout = findViewById(R.id.addTask_template);
        categoryRecyclerView = findViewById(R.id.category_recycler);
        chooseCategoryTV = findViewById(R.id.choose_category);
        startButton = findViewById(R.id.start_now);
        saveTaskButton = findViewById(R.id.save_task);
        timerLabelTv = findViewById(R.id.start_time_label);
        timerCard = findViewById(R.id.timer_card);
        saveButton = findViewById(R.id.save_button);
        taskImage = findViewById(R.id.task_image);
        timerSpinner = findViewById(R.id.time_spinner);
        taskName = findViewById(R.id.task_name);
        taskDescription = findViewById(R.id.task_description);
        hourEditText = findViewById(R.id.hour);
        minuteEditText = findViewById(R.id.minute);
        final Button yesButton = findViewById(R.id.yes_button);
        final Button noButton = findViewById(R.id.no_button);
        TextView notificationLabel = findViewById(R.id.notification_label);

        yesButton.setBackgroundColor(Color.GREEN);
        noButton.setBackgroundColor(Color.RED);

        timerLabelTv.setVisibility(View.GONE);
        timerCard.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
        addTaskTemplateLayout.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent.hasExtra("savedTask")) {

            setTitle("Update Task");
            final TaskItem taskItem = intent.getParcelableExtra("savedTask");

            if (taskItem != null) {
                taskId = taskItem.getId();

                timerLabelTv.setVisibility(View.VISIBLE);
                yesButton.setVisibility(View.VISIBLE);
                noButton.setVisibility(View.VISIBLE);
                addTaskTemplateLayout.setVisibility(View.VISIBLE);

                categoryRecyclerView.setVisibility(View.GONE);
                startButton.setVisibility(View.GONE);
                saveTaskButton.setVisibility(View.GONE);
                chooseCategoryTV.setVisibility(View.GONE);

                if (taskItem.getTaskName() != null) {
                    taskName.setText(taskItem.getTaskName());
                }
                if (!taskItem.getTaskDescription().isEmpty()) {
                    taskDescription.setText(taskItem.getTaskDescription());
                }
                if (taskItem.getCategoryImage() != null) {
                    image = taskItem.getCategoryImage();
                    Picasso.get().load(taskItem.getCategoryImage()).into(taskImage);
                }
                     time =  taskItem.getTimeinSeconds();
                notificationLabel.setText(String.format("Start Time is: %s", time));
            }
            timerLabelTv.setText(getResources().getString(R.string.do_u_want));
            saveButton.setText(getResources().getString(R.string.update));

        } else {
            Log.e("AddTaskActivity", "No Intent");
        }
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isYesButtonClicked = true;
                timerCard.setVisibility(View.VISIBLE);
                yesButton.setVisibility(View.GONE);
                noButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButton.setVisibility(View.VISIBLE);
                yesButton.setVisibility(View.GONE);
                noButton.setVisibility(View.GONE);
            }
        });

        //Setting up spinner
        setUpSpinner();

        if (taskId != DEFAULT_TASK_ID){
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (isYesButtonClicked) {
                        hh = hourEditText.getText().toString();
                        mm = minuteEditText.getText().toString();
                        timeSetByUser = hh + ":" + mm + " " + amOrpm;
                    }else {
                        timeSetByUser = time;
                    }
                    TaskItem taskItem1 = new TaskItem(taskId,image, taskName.getText().toString(),
                            taskDescription.getText().toString(), timeSetByUser);
                    taskViewModel.updateSingleTask(taskItem1);
                    finish();
                }
            });
        }else{

            //Set action for save button
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hh = hourEditText.getText().toString();
                    mm = minuteEditText.getText().toString();
                    timeSetByUser = hh + ":" + mm + " " + amOrpm;
                    TaskItem taskItem = new TaskItem(image, taskName.getText().toString(), taskDescription.getText().toString(), timeSetByUser);
                    taskViewModel.insertTask(taskItem);
                    finish();
                }
            });
        }

        //Set action for start now button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting the current locale time
                currentTime = getCurrentTime();
                TaskItem taskItem = new TaskItem(image, taskName.getText().toString(), taskDescription.getText().toString(), currentTime);
                taskViewModel.insertTask(taskItem);
                Intent startIntent = new Intent(AddTaskActivity.this, MainActivity.class);
                startIntent.putExtra("startNow", taskItem);
                startActivity(startIntent);
            }
        });

        //Initializing the ViewModel class
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        //Set action for save task button click
        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setVisibility(View.GONE);
                saveTaskButton.setVisibility(View.GONE);

                timerLabelTv.setVisibility(View.VISIBLE);
                timerCard.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
            }
        });

        //Setting up CategoryRecycler
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        categoryAdapter = new CategoryAdapter(AddTaskActivity.this, AddTaskActivity.this);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    //setup the spinner
    private void setUpSpinner() {

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinner, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timerSpinner.setAdapter(arrayAdapter);
        timerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                amOrpm = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

//                amOrpm = adapterView.getItemAtPosition(1).toString();

            }
        });
    }

    //Get the current time
    private String getCurrentTime() {

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        Date currentLocalTime = calendar.getTime();

        DateFormat time = new SimpleDateFormat("HH:mm a", Locale.getDefault());
        time.setTimeZone(TimeZone.getDefault());

        return time.format(currentLocalTime);
    }

    @Override
    public void onCategoryItemClick(int position) {

        chooseCategoryTV.setVisibility(View.GONE);
        categoryRecyclerView.setVisibility(View.GONE);
        addTaskTemplateLayout.setVisibility(View.VISIBLE);
        image = categoryAdapter.getImage(position);
        taskImage.setImageResource(image);
    }
}
