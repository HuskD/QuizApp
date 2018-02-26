package quizapp.staranapp.com.quizapp.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import quizapp.staranapp.com.quizapp.MainActivity;
import quizapp.staranapp.com.quizapp.allquizes.QuizObject;
import quizapp.staranapp.com.quizapp.pytania.Pytanie;

/**
 * Created by danie on 13.02.2018.
 */

public class BackgroundTask extends AsyncTask<Void, Void, Void> {


    ProgressDialog progressDialog;
    String quizesJsonUrl = "http://quiz.o2.pl/api/v1/quizzes/0/100";
    Context mContext;
    ArrayList<QuizObject> downloadedQuizes = new ArrayList<>();

    public BackgroundTask(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Prosze czekac...");
        progressDialog.setMessage("Lista quizow jest ladowana...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... strings) {
        try {
            URL url = new URL(quizesJsonUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
                Thread.sleep(500);
            }

            String jsonData = stringBuilder.toString().trim();

            JSONObject mainObject = new JSONObject(jsonData);

            JSONArray jsonArray = mainObject.getJSONArray("items");
            QuizyDbHelper quizyDbHelper = new QuizyDbHelper(mContext);
            SQLiteDatabase db = quizyDbHelper.getWritableDatabase();



            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);




                long idQuizu = obj.getLong("id");

                String tytulQuizu = obj.getString("title");
                String opisQuizu = obj.getString("content");
                String kategoriaQuizu = obj.getJSONObject("category").getString("name");
                String urlObrazka = "";
                if(obj.has("mainPhoto")) {
                     urlObrazka = obj.getJSONObject("mainPhoto").getString("url");
                }
                QuizObject quizObject = new QuizObject(idQuizu, tytulQuizu, opisQuizu, kategoriaQuizu, urlObrazka);
                downloadedQuizes.add(quizObject);
            }

            quizyDbHelper.addAllQuizes(downloadedQuizes);







        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();

        PytaniaDbHelper pytaniaDbHelper = new PytaniaDbHelper(mContext);
        pytaniaDbHelper.getWritableDatabase();

        if(pytaniaDbHelper.getAllStoredPytania().size() == 0) {



            for (QuizObject q : downloadedQuizes) {
                PytaniaBackgroundTask pytaniaBackgroundTask = new PytaniaBackgroundTask(mContext, q.getIdQuizu());
                pytaniaBackgroundTask.execute();
                Log.e("Pytania", "zaladowane");
            }



        }
    }




}
