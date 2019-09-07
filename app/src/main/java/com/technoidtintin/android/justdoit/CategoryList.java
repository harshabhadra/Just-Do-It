package com.technoidtintin.android.justdoit;

import com.technoidtintin.android.justdoit.Model.CategoryItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryList {

    public static List<CategoryItem>getCategoryList(){
        List<CategoryItem>categoryItemList = new ArrayList<>();
        categoryItemList.add(new CategoryItem(R.mipmap.ic_work,"Work"));
        categoryItemList.add(new CategoryItem(R.mipmap.ic_personal,"Personal"));
        categoryItemList.add(new CategoryItem(R.mipmap.ic_fitness,"Fitness"));
        categoryItemList.add(new CategoryItem(R.mipmap.ic_reminder,"Reminder"));
        return  categoryItemList;
    }
}
