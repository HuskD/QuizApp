package quizapp.staranapp.com.quizapp.allquizes;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import quizapp.staranapp.com.quizapp.pytania.Pytanie;

/**
 * Created by danie on 10.02.2018.
 */

public class QuizObject {


    private long idQuizu;
    private String tytulQuizu;
    private String opisQuizu;
    private String kategoriaQuizu;
    private String urlObrazka;


    public QuizObject() {

    }

    public QuizObject (long idQuizu, String tytulQuizu, String opisQuizu, String kategoriaQuizu, String urlObrazka) {

        this.idQuizu = idQuizu;
        this.tytulQuizu = tytulQuizu;
        this.opisQuizu = opisQuizu;
        this.kategoriaQuizu = kategoriaQuizu;
        this.urlObrazka = urlObrazka;
    }

    public String getTytulQuizu() {
        return tytulQuizu;
    }

    public void setTytulQuizu(String tytulQuizu) {
        this.tytulQuizu = tytulQuizu;
    }

    public String getOpisQuizu() {
        return opisQuizu;
    }

    public void setOpisQuizu(String opisQuizu) {
        this.opisQuizu = opisQuizu;
    }

    public String getKategoriaQuizu() {
        return kategoriaQuizu;
    }

    public void setKategoriaQuizu(String kategoriaQuizu) {
        this.kategoriaQuizu = kategoriaQuizu;
    }

    public String getUrlObrazka() {
        return urlObrazka;
    }

    public void setUrlObrazka(String urlObrazka) {
        this.urlObrazka = urlObrazka;
    }

    public long getIdQuizu() {
        return idQuizu;
    }

    public void setIdQuizu(long idQuizu) {
        this.idQuizu = idQuizu;
    }


}
