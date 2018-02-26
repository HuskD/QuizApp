package quizapp.staranapp.com.quizapp.allquizes;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import quizapp.staranapp.com.quizapp.R;
import quizapp.staranapp.com.quizapp.data.BackgroundTask;
import quizapp.staranapp.com.quizapp.data.PytaniaBackgroundTask;
import quizapp.staranapp.com.quizapp.data.PytaniaDbHelper;
import quizapp.staranapp.com.quizapp.data.QuizyDbHelper;
import quizapp.staranapp.com.quizapp.pytania.Pytanie;

public class ChooseQuizActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    QuizyDbHelper quizyDbHelper;
    List<QuizObject> quizy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quiz);

        //get all quizes and load them
        quizyDbHelper = new QuizyDbHelper(this);
        //make db writeable
        quizyDbHelper.getWritableDatabase();


        BackgroundTask backgroundTask = new BackgroundTask(ChooseQuizActivity.this);
        if(quizyDbHelper.getAllStoredQuizes().size() == 0) {
            backgroundTask.execute();

            //restart the app after downloading and storing quizes
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }, 83000);



        }

        quizy = quizyDbHelper.getAllStoredQuizes();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new QuizAdapter(quizy, ChooseQuizActivity.this);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ChooseQuizActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAdapter.notifyDataSetChanged();
    }

}
