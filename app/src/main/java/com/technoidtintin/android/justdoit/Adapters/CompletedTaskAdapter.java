package com.technoidtintin.android.justdoit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technoidtintin.android.justdoit.Model.CompletedTask;
import com.technoidtintin.android.justdoit.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CompletedTaskAdapter extends RecyclerView.Adapter<CompletedTaskAdapter.CompletedViewHolder>{

    LayoutInflater inflater;
    List<CompletedTask>completedTasks = new ArrayList<>();

    public CompletedTaskAdapter( Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CompletedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CompletedViewHolder(inflater.inflate(R.layout.completed_task_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedViewHolder holder, int position) {

        Picasso.get().load(completedTasks.get(position).getcImage()).into(holder.completedIV);
        holder.completedTV.setText(completedTasks.get(position).getcTaskName());
        holder.timerTV.setText(completedTasks.get(position).getcStartTime());
    }

    @Override
    public int getItemCount() {
        return completedTasks.size();
    }

    public void setCompletedTasks(List<CompletedTask>completedTasks){
        this.completedTasks = completedTasks;
        notifyDataSetChanged();
    }

    public void removeAll(){
        for (int i = 0; i<completedTasks.size(); i++){
            completedTasks.remove(i);
            notifyDataSetChanged();
        }
    }

    class CompletedViewHolder extends RecyclerView.ViewHolder{

        ImageView completedIV;
        TextView completedTV;
        TextView timerTV;

        public CompletedViewHolder(@NonNull View itemView) {
            super(itemView);

            completedIV = itemView.findViewById(R.id.completed_image_view);
            completedTV = itemView.findViewById(R.id.completed_Task_name);
            timerTV = itemView.findViewById(R.id.completed_Task_timing_tv);
        }
    }
}
