package quizapp.staranapp.com.quizapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import quizapp.staranapp.com.quizapp.allquizes.ChooseQuizActivity;
import quizapp.staranapp.com.quizapp.allquizes.QuizAdapter;
import quizapp.staranapp.com.quizapp.allquizes.QuizObject;
import quizapp.staranapp.com.quizapp.data.BackgroundTask;
import quizapp.staranapp.com.quizapp.data.PytaniaDbHelper;
import quizapp.staranapp.com.quizapp.pytania.Pytanie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    public static final String QUIZ_URL = "http://quiz.o2.pl/api/v1/quizzes/0/100";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView currentQuestionTextView;
    private RadioButton r1, r2, r3, r4;
    private Pytanie aktualnePytanie;
    private ArrayList<Pytanie> uzytePytania = new ArrayList<>();
    private ProgressBar progressBar;
    private ImageView qImageView;

    String mQuizId;
    PytaniaDbHelper pytaniaDbHelper;
    List<Pytanie> pytania;
    List<Pytanie> pytaniaTegoQuizu;

    int poprawneLicznik = 0;
    int bledneLicznik = 0;
    int iloscWszystkichPytan;
    int iloscUzytychPytan = 0;
    int qid = 0;
    boolean isFinished = false;
    boolean isFinishedExternal;
    //unique keys created for usage in shared prefs, contain unique quiz id + variable identifier
    String boolkey;
    String wynikUkonczeniaKey;
    String procentUkonczeniaKey;
    String qidKey;
    String poprawneOdpowiedziKey;
    String wszystkiePytaniaKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get current quiz id from ChooseQuizActivity
        mQuizId = getIntent().getExtras().getString("quizId");

        //assign values to keys based on mQuizId
        boolkey = mQuizId + "bool";
        wynikUkonczeniaKey = mQuizId + "wynUk";
        procentUkonczeniaKey = mQuizId + "procUk";
        qidKey = mQuizId + "qid";
        poprawneOdpowiedziKey = mQuizId + "popr";
        wszystkiePytaniaKey = mQuizId + "wszyst";

        currentQuestionTextView = findViewById(R.id.questionTextView);
        qImageView = findViewById(R.id.questionImageView);
        r1 = findViewById(R.id.radioButton1);
        r2 = findViewById(R.id.radioButton2);
        r3 = findViewById(R.id.radioButton3);
        r4 = findViewById(R.id.radioButton4);


        isFinishedExternal = HelperSharedPreferences.getSharedPreferencesBoolean(getApplicationContext(), boolkey, false);

        //check from data stored in shared preferences if quiz is finished
        if(!isFinishedExternal) {
            qid = HelperSharedPreferences.getSharedPreferencesInt(getApplicationContext(), qidKey, 0);
            poprawneLicznik = HelperSharedPreferences.getSharedPreferencesInt(getApplicationContext(), poprawneOdpowiedziKey, 0);
        }


        //get data from questions database
        pytaniaDbHelper = new PytaniaDbHelper(MainActivity.this);
        pytaniaDbHelper.getWritableDatabase();

        BackgroundTask backgroundTask = new BackgroundTask(MainActivity.this);
        if(pytaniaDbHelper.getAllStoredPytania().size() == 0) {
            backgroundTask.execute();
            // mAdapter.notifyDataSetChanged();
        }

            //get questions
            pytania = pytaniaDbHelper.getAllStoredPytania();

            //assign questions for displayed quiz
            pytaniaTegoQuizu = getPytaniaTegoQuizu(pytania);

            //get current displayed question
            aktualnePytanie = pytaniaTegoQuizu.get(qid);

            //count how many questions this quiz has (this information can also be obtained from json file)
            iloscWszystkichPytan = pytaniaTegoQuizu.size();

            progressBar = findViewById(R.id.progressBar);
            progressBar.setMax(iloscWszystkichPytan);

            //this metod set the question and 2 or more answers
            updatePytanieAndOdpowiedzi();



    }

    //radio buttons onclick listeners
    public void radioButton1(View view){
        if(aktualnePytanie.getPoprawnaOdp().equals(r1.getText())) {
            poprawneLicznik++;
            Log.d("poprawne", String.valueOf(poprawneLicznik));
            nastepnePytanie();
        }else{
            bledneLicznik++;
            Log.d("bledne", String.valueOf(bledneLicznik));
            nastepnePytanie();
        }
    }

    public void radioButton2(View view){
        if(aktualnePytanie.getPoprawnaOdp().equals(r2.getText())) {
            poprawneLicznik++;
            Log.d("poprawne", String.valueOf(poprawneLicznik));
            nastepnePytanie();
        }else{
            bledneLicznik++;
            Log.d("bledne", String.valueOf(bledneLicznik));
            nastepnePytanie();
        }
    }

    public void radioButton3(View view){
        if(aktualnePytanie.getPoprawnaOdp().equals(r3.getText())) {
            poprawneLicznik++;
            Log.d("poprawne", String.valueOf(poprawneLicznik));
            nastepnePytanie();
        }else{
            bledneLicznik++;
            Log.d("bledne", String.valueOf(bledneLicznik));
            nastepnePytanie();
        }
    }

    public void radioButton4(View view){
        if(aktualnePytanie.getPoprawnaOdp().equals(r4.getText())) {
            poprawneLicznik++;
            Log.d("poprawne", String.valueOf(poprawneLicznik));
            nastepnePytanie();
        }else{
            bledneLicznik++;
            Log.d("bledne", String.valueOf(bledneLicznik));
            nastepnePytanie();
        }
    }


    //this method saves user's game progress and displays next question in quiz
    public void nastepnePytanie(){
        int wynikProcentowy = (int) ((poprawneLicznik * 100) / iloscWszystkichPytan);
        int procentUkonczenia = ((poprawneLicznik + bledneLicznik) * 100) / iloscWszystkichPytan;
        if(qid < pytaniaTegoQuizu.size() - 1) {
            r1.setChecked(false);
            r2.setChecked(false);
            r3.setChecked(false);
            r4.setChecked(false);
            qid++;
            aktualnePytanie = pytaniaTegoQuizu.get(qid);
            updatePytanieAndOdpowiedzi();

            progressBar.setProgress(qid);


            HelperSharedPreferences.putSharedPreferencesInt(getApplicationContext(), procentUkonczeniaKey, procentUkonczenia);
            HelperSharedPreferences.putSharedPreferencesBoolean(getApplicationContext(), boolkey, isFinished);
            HelperSharedPreferences.putSharedPreferencesInt(getApplicationContext(), qidKey, qid);
        }
        else{
            progressBar.setProgress(qid+1);
            isFinished = true;
            HelperSharedPreferences.putSharedPreferencesBoolean(getApplicationContext(), boolkey, isFinished);
            HelperSharedPreferences.putSharedPreferencesInt(getApplicationContext(), wynikUkonczeniaKey, wynikProcentowy);
            HelperSharedPreferences.putSharedPreferencesInt(getApplicationContext(), poprawneOdpowiedziKey, poprawneLicznik);
            HelperSharedPreferences.putSharedPreferencesInt(getApplicationContext(), wszystkiePytaniaKey, iloscWszystkichPytan);
            Intent i = new Intent(this, KoniecGry.class);
            Bundle b = new Bundle();
            b.putInt("wynikProcentowy", wynikProcentowy);
            b.putString("quizId", mQuizId);
            i.putExtras(b);
            startActivity(i);
            finish();

        }
    }


    //this method returns array of questions used in this particular quiz
    public List<Pytanie> getPytaniaTegoQuizu (List<Pytanie> pytania) {
        List<Pytanie> aktualne = new ArrayList<>();
        for(Pytanie p : pytania) {
            if(String.valueOf(p.getIdQuizu()).equals(mQuizId) && !aktualne.contains(p)) {
                aktualne.add(p);
//                Log.e("Aktualne pytanie:", p.getPoprawnaOdp());
//
//                Log.e("Poprawna odpowiedz:", p.getPoprawnaOdp());
            }
        }
        return aktualne;
    }


    public void updatePytanieAndOdpowiedzi() {
        if(!aktualnePytanie.getObrazekUrl().equals("")) {
            Picasso.with(this)
                    .load(aktualnePytanie.getObrazekUrl())
                    .into(qImageView);
        }
        progressBar.setProgress(qid);
        currentQuestionTextView.setText(aktualnePytanie.getTrescPytania());
        r1.setText(aktualnePytanie.getOdpowiedzi().get(0));
        r2.setText(aktualnePytanie.getOdpowiedzi().get(1));
        if(aktualnePytanie.getOdpowiedzi().size() >= 3) {
            r3.setText(aktualnePytanie.getOdpowiedzi().get(2));
        }else{
            r3.setVisibility(View.INVISIBLE);
        }
        if(aktualnePytanie.getOdpowiedzi().size() >= 4){
            r4.setText(aktualnePytanie.getOdpowiedzi().get(3));
        }else{
            r4.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //when user presses back button recyclerview's adapter will be notified
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, ChooseQuizActivity.class);
        startActivityForResult(i, 0);
        finish();
    }
}
