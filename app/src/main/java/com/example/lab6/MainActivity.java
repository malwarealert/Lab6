package com.example.lab6;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper=new DBHelper(getApplicationContext()); //получение бд
        try {
            database=dbHelper.getWritableDatabase(); //и читать, и писать
        } catch (Exception e){
            e.printStackTrace();
        }

        listView=findViewById(R.id.ListView);

        ArrayList<HashMap<String,String>>categories =new ArrayList<>(); //общий список
        HashMap <String,String> category; //отдельная строка
        Cursor cursor = database.rawQuery("SELECT categories.id, categories.name AS \"Категория\" FROM categories", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){ //сохраняем каждую строчку в массив
            category=new HashMap<>();
            category.put("name", cursor.getString(1));
            categories.add(category);
            cursor.moveToNext();
        }
        cursor.close();

        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                categories, android.R.layout.simple_list_item_2, //шаблон
                new String[]{"name"}, //ключи из hashmap
                new int[]{android.R.id.text1} //id из шаблона
        );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), ItemActivity.class);
                intent.putExtra("position",position); //ключ + значение
                //String category_name = (String) (listView.getItemAtPosition(position));
                HashMap itemData = (HashMap) listView.getItemAtPosition(position);
                String category_name = String.valueOf(itemData.get("name"));
                intent.putExtra("category_name",category_name); //ключ + значение
                startActivity(intent);
            }
        });
    }
}