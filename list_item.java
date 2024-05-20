package com.example.bmiceshi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class list_item extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);
    }

    listItems = new ArrayList<HashMap<String,String>>();
    for (int i = 0;i<10;i++){
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("ItemTitle","Rate:  "+i);
        map.put("ItemDetail","detail"+i);
        Object listItems = null;
        listItems.app(map);
    }
    listItemAdapter = new SimpleAdapter(this,listItems,R,layout.list_item,new String[]{"ItemTitle","ItemDetail"},new int[]{R.id.itemTitle,R.id.itemDetail});
    
}