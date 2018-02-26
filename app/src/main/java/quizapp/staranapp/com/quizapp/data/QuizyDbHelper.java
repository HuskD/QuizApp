package quizapp.staranapp.com.quizapp.data;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import quizapp.staranapp.com.quizapp.allquizes.ChooseQuizActivity;
import quizapp.staranapp.com.quizapp.allquizes.QuizObject;

/**
 * Created by danie on 12.02.2018.
 */

public class QuizyDbHelper extends SQLiteOpenHelper {

    private Context context;

    ArrayList<QuizObject> quizObjectArrayList = new ArrayList<>();

    //database name
    public static final String DB_NAME = "Quizy.db";
    //version of database
    public static final int DB_VERSION = 5;
    //Table name
    public static final String TABLE_NAME = "QUIZY";
    //Id of quiz in database
    public static final String QID = "_QID";
    //id of quiz from json
    public static final String JSONQID = "JSONID";
    //title of quiz
    public static final String QUIZ_TITLE = "TITLE";
    //quiz description
    public static final String QUIZ_DESCRIPTION = "DESCRIPTION";
    //quiz image
    public static final String QUIZ_IMAGE = "IMGURL";
    //quiz category
    public static final String QUIZ_CATEGORY = "CATEGORY";
    //create a table first column - quiz id, sec column - quiz json id, third column - quiz title, fourth column - quiz description, fifth column - quiz img url, sixth column - quiz category
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + QID + " INTEGER PRIMARY KEY AUTOINCREMENT , "+ JSONQID + " BIGINT(99999999999999999), " + QUIZ_TITLE + " VARCHAR(255), " + QUIZ_DESCRIPTION + " VARCHAR(255), " + QUIZ_IMAGE + " VARCHAR(255), " + QUIZ_CATEGORY + " VARCHAR(255));";
    //drop table query
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;




    public QuizyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //oncreate is called once
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //onupgrade will be called when we upgrade or increment our database version number
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }



    public void addAllQuizes(ArrayList<QuizObject> allQuizes) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            for (QuizObject quiz : allQuizes) {
                values.put(JSONQID, quiz.getIdQuizu());
                values.put(QUIZ_TITLE, quiz.getTytulQuizu());
                values.put(QUIZ_DESCRIPTION, quiz.getOpisQuizu());
                values.put(QUIZ_IMAGE, quiz.getUrlObrazka());
                values.put(QUIZ_CATEGORY, quiz.getKategoriaQuizu());
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<QuizObject> getAllStoredQuizes() {

        List<QuizObject> quizObjectList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String[] column = {QID, JSONQID, QUIZ_TITLE, QUIZ_DESCRIPTION, QUIZ_IMAGE, QUIZ_CATEGORY};
        Cursor c = db.query(TABLE_NAME, column, null, null, null, null, null);

        while(c.moveToNext()) {
            QuizObject quizObject = new QuizObject();
            quizObject.setIdQuizu(c.getLong(1));
            quizObject.setTytulQuizu(c.getString(2));
            quizObject.setOpisQuizu(c.getString(3));
            quizObject.setUrlObrazka(c.getString(4));
            quizObject.setKategoriaQuizu(c.getString(5));
            quizObjectList.add(quizObject);
        }

//        for(QuizObject quiz: quizObjectList) {
//            Log.d("Quiz z bazy danych:", String.valueOf(quiz.getIdQuizu()));
//        }

        db.setTransactionSuccessful();
        db.endTransaction();
        c.close();
        db.close();
        return quizObjectList;
    }
}
