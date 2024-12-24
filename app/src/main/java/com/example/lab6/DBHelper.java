package com.example.lab6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "home.db";
    private static String DB_LOCATION;
    private static final int DB_VERSION =1;

    private Context myContext;

    public DBHelper (Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
        DB_LOCATION = context.getApplicationInfo().dataDir+"/databases/"; //сохраняет бд в папку

        copyDB();
    }

    private boolean checkDB() { //проверка на существование бд
        File fileDB = new File(DB_LOCATION + DB_NAME);
        return fileDB.exists();
    }

    private void copyDB(){
        if (!checkDB()){ //если файла нет
            this.getReadableDatabase();
            try {
                copyDBFile();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private void copyDBFile() throws IOException{
        InputStream inputStream =myContext.getAssets().open(DB_NAME);
        OutputStream outputStream = new FileOutputStream(DB_LOCATION+DB_NAME);
        byte[] buffer =new byte[1024];
        int lenght;
        while ((lenght=inputStream.read(buffer)) > 0){
            outputStream.write(buffer,0,lenght);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
