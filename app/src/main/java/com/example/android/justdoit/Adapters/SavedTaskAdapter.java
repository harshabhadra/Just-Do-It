package com.example.android.justdoit.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.justdoit.R;
import com.example.android.justdoit.Model.TaskItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SavedTaskAdapter extends RecyclerView.Adapter<SavedTaskAdapter.SaveTaskViewHolder> {

    LayoutInflater inflater;
    List<TaskItem> savedList = new ArrayList<>();
    OnSavedItemClickListener listener;
    String TAG = SavedTaskAdapter.class.getSimpleName();

    public SavedTaskAdapter(Context context, OnSavedItemClickListener listener) {

        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public SaveTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SaveTaskViewHolder(inflater.inflate(R.layout.save_task_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SaveTaskViewHolder holder, int position) {

        if (savedList != null) {
            Log.e(TAG, "List is full");
            Picasso.get().load(savedList.get(position).getCategoryImage()).into(holder.taskImage);
            holder.taskName.setText(savedList.get(position).getTaskName());
            holder.taskTime.setText(savedList.get(position).getTimeinSeconds());
        } else {
            Log.e(TAG, "empty list");
        }
    }


    @Override
    public int getItemCount() {
        if (!savedList.isEmpty()) {
            return savedList.size();
        }else {
            return 0;
        }
    }

    //Remove item from list
    public void removeItem(int position){
            savedList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,savedList.size());
    }

    //Get task position
    public TaskItem getTask(int position){
        if (savedList!= null){
          return savedList.get(position);
        }
        return null;
    }

    //Set the list of task
    public void setTaskList(List<TaskItem>taskList){
        this.savedList = taskList;
        notifyDataSetChanged();
    }

    public interface OnSavedItemClickListener {
        void onSavedItemClick(int position);
    }

    class SaveTaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView taskImage;
        TextView taskName;
        TextView taskTime;

        public SaveTaskViewHolder(@NonNull View itemView) {
            super(itemView);

            taskImage = itemView.findViewById(R.id.saved_task_image);
            taskName = itemView.findViewById(R.id.save_task_name);
            taskTime = itemView.findViewById(R.id.save_task_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            listener.onSavedItemClick(position);

        }
    }
}
