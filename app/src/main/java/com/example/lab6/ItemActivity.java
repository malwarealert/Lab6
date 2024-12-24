package com.example.lab6;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private ListView listView;
    private TextView categoryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main2), (v, insets) -> {
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

        listView=findViewById(R.id.ListView2);
        categoryText=findViewById(R.id.categoryText);

        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("position", 0) + 1;
        categoryText.setText(mIntent.getStringExtra("category_name"));


        ArrayList<HashMap<String,String>>products =new ArrayList<>(); //общий список
        HashMap <String,String> product; //отдельная строка
        Cursor cursor = database.rawQuery("SELECT products.name AS \"Имя\", products.info AS \"Информация\", products.price AS \"Цена\" FROM products WHERE products.category_id = ?", new String[] { String.valueOf(intValue) } );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){ //сохраняем каждую строчку в массив
            product=new HashMap<>();
            product.put("name", cursor.getString(0));
            product.put("info", "\n" + "Информация о товаре: " + cursor.getString(1) + "\n\nЦена (руб.): " + cursor.getString(2));
            products.add(product);
            cursor.moveToNext();
        }
        cursor.close();

        SimpleAdapter adapter = new SimpleAdapter(
                getApplicationContext(),
                products, android.R.layout.simple_list_item_2, //шаблон
                new String[]{"name","info"}, //ключи из hashmap
                new int[]{android.R.id.text1, android.R.id.text2} //id из шаблона
        );
        listView.setAdapter(adapter);

    }
}
