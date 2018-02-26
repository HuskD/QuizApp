package quizapp.staranapp.com.quizapp.allquizes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import quizapp.staranapp.com.quizapp.MainActivity;
import quizapp.staranapp.com.quizapp.R;

/**
 * Created by danie on 10.02.2018.
 */

public class QuizViewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView idQuizu, tytulQuizu, opisQuizu, kategoriaQuizu, ostatniwynikProgress;
    public CardView cardViewQuizu;
    public ImageView cardBgImage;

    public QuizViewsHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        idQuizu = itemView.findViewById(R.id.idQuizuTextView);
        tytulQuizu = itemView.findViewById(R.id.tytulQuizuId);
        opisQuizu = itemView.findViewById(R.id.opisQuizuId);
        kategoriaQuizu = itemView.findViewById(R.id.kategoriaTextId);
        ostatniwynikProgress = itemView.findViewById(R.id.ostatniWynikProgressText);
        cardViewQuizu = itemView.findViewById(R.id.quizCardView);
        cardBgImage = itemView.findViewById(R.id.cardBgImage);


    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        Bundle b = new Bundle();
        b.putString("quizId", idQuizu.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}
