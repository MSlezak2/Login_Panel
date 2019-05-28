package core;

import java.util.ArrayList;

public class Employee {

    int id;
    String imię, nazwisko, stanowisko, mail;
    ArrayList<Task> listaZadań;

    public void setId(int id) {
        this.id = id;
    }

    public void setImię(String imię) {
        this.imię = imię;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public void setStanowisko(String stanowisko) {
        this.stanowisko = stanowisko;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setListaZadań(ArrayList<Task> listaZadań) {
        this.listaZadań = listaZadań;
    }

    public int getId() {
        return id;
    }

    public String getImię() {
        return imię;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getStanowisko() {
        return stanowisko;
    }

    public String getMail() {
        return mail;
    }

    public ArrayList<Task> getListaZadań() {
        return listaZadań;
    }

    public Employee(int id, String im, String na, String st, String ma) {
        this.id = id;
        imię = im;
        nazwisko = na;
        stanowisko = st;
        mail = ma;
        listaZadań = new ArrayList<Task>();
    }

}
