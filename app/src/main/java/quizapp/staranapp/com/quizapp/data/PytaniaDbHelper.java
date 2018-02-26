package quizapp.staranapp.com.quizapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import quizapp.staranapp.com.quizapp.allquizes.QuizObject;
import quizapp.staranapp.com.quizapp.pytania.Pytanie;

/**
 * Created by danie on 14.02.2018.
 */

public class PytaniaDbHelper extends SQLiteOpenHelper {
    private Context context;

    ArrayList<QuizObject> quizObjectArrayList = new ArrayList<>();

    //database name
    public static final String DB_NAME = "Pytania.db";
    //version of database
    public static final int DB_VERSION = 1;
    //Table name
    public static final String TABLE_NAME = "PYTANIA";
    //Id of question in database
    public static final String PID = "_PID";
    //id of quiz from json
    public static final String JSONPID = "JSONPID";
    //title of question
    public static final String PYTANIE_TITLE = "TITLE";
    //question desc
    public static final String ODPOWIEDZI = "ANSWERS";
    //correct answer
    public static final String POPRAWNA_ODP = "CORRECTANSW";
    //question's img
    public static final String PYTANIE_OBRAZEK = "QUESTIONIMAGE";
    //create a table first column - question id, sec column - json id, third column - question title, fourth column - answers array converted to string, fifth column - correct answer, sixth column - question's image url
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + PID + " INTEGER PRIMARY KEY AUTOINCREMENT , "+ JSONPID + " BIGINT(99999999999999999), " + PYTANIE_TITLE + " VARCHAR(255), " + ODPOWIEDZI + " LONG VARCHAR, " + POPRAWNA_ODP + " VARCHAR(255), " + PYTANIE_OBRAZEK + " VARCHAR(255));";
    //drop table query
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    public PytaniaDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static String strSeparator = " , ";

    public static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //oncreate is called once
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }



    public void addAllPytania(ArrayList<Pytanie> allPytania) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            for (Pytanie p : allPytania) {
                values.put(JSONPID, p.getIdQuizu());
                values.put(PYTANIE_TITLE, p.getTrescPytania());

                String[] odpowiedziArray = p.getOdpowiedzi().toArray(new String[0]);

                values.put(ODPOWIEDZI, convertArrayToString(odpowiedziArray));
                values.put(POPRAWNA_ODP, p.getPoprawnaOdp());
                values.put(PYTANIE_OBRAZEK, p.getObrazekUrl());
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<Pytanie> getAllStoredPytania() {

        List<Pytanie> pytanieList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String[] column = {PID, JSONPID, PYTANIE_TITLE, ODPOWIEDZI, POPRAWNA_ODP, PYTANIE_OBRAZEK};
        Cursor c = db.query(TABLE_NAME, column, null, null, null, null, null);

        while(c.moveToNext()) {
            Pytanie pytanie = new Pytanie();
            pytanie.setIdQuizu(c.getLong(1));
            pytanie.setTrescPytania(c.getString(2));

            ArrayList<String> odpowiedziList = new ArrayList<>();
            String[] listaOdpowiedzi = convertStringToArray(c.getString(3));
            Collections.addAll(odpowiedziList, listaOdpowiedzi);

            pytanie.setOdpowiedzi(odpowiedziList);
            pytanie.setPoprawnaOdp(c.getString(4));
            pytanie.setObrazekUrl(c.getString(5));
            pytanieList.add(pytanie);
        }

//        for(QuizObject quiz: quizObjectList) {
//            Log.d("Quiz z bazy danych:", String.valueOf(quiz.getIdQuizu()));
//        }

        db.setTransactionSuccessful();
        db.endTransaction();
        c.close();
        db.close();
        return pytanieList;
    }
}

