package com.example.android.justdoit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.justdoit.Model.CategoryItem;
import com.example.android.justdoit.CategoryList;
import com.example.android.justdoit.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    LayoutInflater inflater;
    List<CategoryItem> categoryList = CategoryList.getCategoryList();
    onCategoryItemClickListener categoryItemClickListener;

    public interface onCategoryItemClickListener{
        void onCategoryItemClick(int position);
    }

    public CategoryAdapter(Context context, onCategoryItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        categoryItemClickListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(inflater.inflate(R.layout.category_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        if (!categoryList.isEmpty()){
            Picasso.get().load(categoryList.get(position).getCategoryImg()).into(holder.imageView);
            holder.textView.setText(categoryList.get(position).getCategoryName());
        }

    }

    public int getImage(int position){
        if (categoryList != null){
            return categoryList.get(position).getCategoryImg();
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.category_image);
            textView = itemView.findViewById(R.id.category_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            categoryItemClickListener.onCategoryItemClick(position);
        }
    }
}
