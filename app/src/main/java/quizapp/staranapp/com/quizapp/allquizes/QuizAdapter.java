package quizapp.staranapp.com.quizapp.allquizes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import quizapp.staranapp.com.quizapp.R;

/**
 * Created by danie on 10.02.2018.
 */

public class QuizAdapter extends RecyclerView.Adapter<QuizViewsHolder> {
    private List<QuizObject> quizList;
    private Context context;


    public QuizAdapter(List<QuizObject> quizList, Context context) {
        this.quizList = quizList;
        this.context = context;
    }

    @Override
    public QuizViewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);


        return new QuizViewsHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(QuizViewsHolder holder, int position) {

        holder.opisQuizu.setText(quizList.get(position).getOpisQuizu());
        holder.tytulQuizu.setText(quizList.get(position).getTytulQuizu());
        holder.kategoriaQuizu.setText("Kategoria: " + quizList.get(position).getKategoriaQuizu());

    }

    @Override
    public int getItemCount() {
        return this.quizList.size();
    }
}