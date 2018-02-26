package quizapp.staranapp.com.quizapp.pytania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import quizapp.staranapp.com.quizapp.data.PytaniaBackgroundTask;

/**
 * Created by danie on 13.02.2018.
 */

public class Pytanie {
    String trescPytania;
    long idQuizu;



    String obrazekUrl;



    ArrayList<String> odpowiedzi;
    String poprawnaOdp;

    public Pytanie() {

    }

    public Pytanie(long idQuizu) {
        this.idQuizu = idQuizu;
    }

    public Pytanie(long idQuizu, String trescPytania, ArrayList<String> odpowiedzi, String poprawnaOdp, String obrazekUrl) {
        this.idQuizu = idQuizu;
        this.trescPytania = trescPytania;
        this.odpowiedzi = odpowiedzi;
        this.poprawnaOdp = poprawnaOdp;
        this.obrazekUrl = obrazekUrl;
    }

    public String getTrescPytania() {
        return trescPytania;
    }

    public void setTrescPytania(String trescPytania) {
        this.trescPytania = trescPytania;
    }



    public String getPoprawnaOdp() {
        return poprawnaOdp;
    }

    public void setPoprawnaOdp(String poprawnaOdp) {
        this.poprawnaOdp = poprawnaOdp;
    }

    public ArrayList<String> getOdpowiedzi() {
        return odpowiedzi;
    }

    public void setOdpowiedzi(ArrayList<String> odpowiedzi) {
        this.odpowiedzi = odpowiedzi;
    }

    public long getIdQuizu() {
        return idQuizu;
    }

    public void setIdQuizu(long idQuizu) {
        this.idQuizu = idQuizu;
    }

    public String getObrazekUrl() {
        return obrazekUrl;
    }

    public void setObrazekUrl(String obrazekUrl) {
        this.obrazekUrl = obrazekUrl;
    }




}
