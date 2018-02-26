package quizapp.staranapp.com.quizapp.data;

import android.app.ProgressDialog;
import android.content.Context;
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

import quizapp.staranapp.com.quizapp.allquizes.QuizObject;
import quizapp.staranapp.com.quizapp.pytania.Pytanie;

/**
 * Created by danie on 13.02.2018.
 */

public class PytaniaBackgroundTask extends AsyncTask<Void, Void, Void> {

    ProgressDialog progressDialog;
    String urlLeft = "http://quiz.o2.pl/api/v1/quiz/";
    long quizId;
    String urlRight = "/0";
    String jsonUrl = urlLeft + String.valueOf(quizId)+ urlRight;
    Context mContext;
    ArrayList<Pytanie> downloadedPytania = new ArrayList<>();

    //http://quiz.o2.pl/api/v1/quiz/{id_quizu}/0


    public PytaniaBackgroundTask(Context context, long quizId) {
        this.mContext = context;
        this.quizId = quizId;
    }

    String getJsonUrl() {
        return urlLeft + String.valueOf(this.quizId) + urlRight;
    }


    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Prosze czekac...");
        progressDialog.setMessage("Ladowanie pytan... Uwaga: baza pytań jest ładowana!");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... strings) {
        try {
            URL url = new URL(getJsonUrl());
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

            JSONArray questionsArray = mainObject.getJSONArray("questions");
            PytaniaDbHelper pytaniaDbHelper = new PytaniaDbHelper(mContext);
            SQLiteDatabase db = pytaniaDbHelper.getWritableDatabase();


            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject obj = questionsArray.getJSONObject(i);

                JSONArray answers = obj.getJSONArray("answers");
                String trescPytania = obj.getString("text");
                JSONObject questionImage = obj.getJSONObject("image");
                String obrazekUrl = questionImage.getString("url");
                String poprawnaOdpowiedz = "";
                ArrayList<String> odpowiedzi = new ArrayList<>();
                for(int j = 0; j < answers.length(); j++) {
                    odpowiedzi.add(answers.getJSONObject(j).getString("text"));
                    if(answers.getJSONObject(j).has("isCorrect")) {
                        poprawnaOdpowiedz = answers.getJSONObject(j).getString("text");
                    }
                }

                Pytanie pytanie = new Pytanie(quizId, trescPytania, odpowiedzi, poprawnaOdpowiedz, obrazekUrl);
                downloadedPytania.add(pytanie);
            }

                pytaniaDbHelper.addAllPytania(downloadedPytania);

            for(Pytanie p: downloadedPytania) {
                Log.e("Pobrane pytania:", p.getTrescPytania());
                Log.e("id quizu tego p", String.valueOf(p.getIdQuizu()));
            }



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

    public long getQuizId() {
        return quizId;
    }

    public void setQuizId(long quizId) {
        this.quizId = quizId;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
    }

}
