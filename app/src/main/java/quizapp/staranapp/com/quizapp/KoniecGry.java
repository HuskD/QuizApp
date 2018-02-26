package quizapp.staranapp.com.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import quizapp.staranapp.com.quizapp.allquizes.ChooseQuizActivity;

/**
 * Created by danie on 15.02.2018.
 */

public class KoniecGry extends AppCompatActivity {
    private TextView procentWynik;
    int wynikProcentowy;
    String currQuizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.koniec_gry);

        procentWynik = findViewById(R.id.procentWynikTextView);

        //gets results of user's input
        wynikProcentowy = getIntent().getExtras().getInt("wynikProcentowy");
        currQuizId = getIntent().getExtras().getString("quizId");

        //displays user's results
        String wynikString = String.valueOf(wynikProcentowy);
        procentWynik.setText(wynikString + " %");
    }

    //this metod will get user back to the main menu
    public void przejdzDoListyQuizow(View view){
        Intent intent = new Intent(this, ChooseQuizActivity.class);
        startActivity(intent);
        finish();
    }

    //play the same again
    public void rozwiazJeszczeRaz(View view){
        Intent intent = new Intent(this, MainActivity.class);
        Bundle b = new Bundle();
        b.putString("quizId", currQuizId);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
}
