package com.technoidtintin.android.justdoit.Model;

public class CategoryItem {

    private int categoryImg;
    private String categoryName;

    public CategoryItem(int categoryImg, String categoryName) {
        this.categoryImg = categoryImg;
        this.categoryName = categoryName;
    }

    public int getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(int categoryImg) {
        this.categoryImg = categoryImg;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
