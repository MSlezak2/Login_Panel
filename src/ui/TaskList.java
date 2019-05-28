package ui;

import core.Employee;
import core.Task;
import core.User;
import dao.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaskList extends JFrame implements ActionListener {

    DBConnection pole1;
    User użytkownik;
    Employee pracownik;
    JPanel plan;
    JLabel nazwUż, imie, nazwisko, stanowisko, mail;
    JTable lista;
    JButton zmianaStat, wyśwOpis, wylog;

    public TaskList(DBConnection dbc, User usr) {
        pole1 = dbc;
        użytkownik = usr;

        //NADANIE TYTUŁU OKNA, JEGO WYMIARÓW ORAZ LAYOUTU
        setTitle("Aplikacja - zalogowano");
        setBounds(100, 100, 584, 300);
        getContentPane().setLayout(new BorderLayout());

        //INICJALIZACJA OBIEKTU KLASY JPANEL, USTAWIENIE JEGO OBRAMOWAŃ, LAYOUTU
        //ORAZ DODANIE GO DO OKNA
        plan = new JPanel();
        plan.setBorder(new EmptyBorder(5, 5, 5, 5));
        plan.setLayout(new BorderLayout());
        setContentPane(plan);

        //UTWORZENIE LOKALNEGO OBIEKTU KLASY JPANEL GRUPUJĄCEGO DANE UŻYTKOWNIKA I PRACOWNIKA...
        JPanel jPanel = new JPanel();
        plan.add(jPanel, BorderLayout.NORTH);
        jPanel.setLayout(new BorderLayout());

        //UTWORZENIE LOKALNEGO OBIEKTU TYPU JPANEL GRUPUJĄCEGO DANE UŻYTKOWNIKA...
        JPanel panelUż = new JPanel();
        jPanel.add(panelUż, BorderLayout.NORTH);
        //ZAINICJOWANIE POLA JLABEL PRZECHOWUJĄCEGO NAZWĘ UŻYTKOWNIKA...
        nazwUż = new JLabel();
        panelUż.add(nazwUż);

        //UTWORZENIE LOKALNEGO OBIEKTU KLASY JPANEL GRUPUJĄCEGO DANE PRACOWNIKA...
        JPanel panelPra = new JPanel();
        jPanel.add(panelPra);
        //ZAINICJOWANIE PÓL JLABEL PRZECHOWUJĄCYCH DANE PRACOWNIKA
        imie = new JLabel();
        nazwisko = new JLabel();
        stanowisko = new JLabel();
        mail = new JLabel();
        panelPra.add(imie);
        panelPra.add(nazwisko);
        panelPra.add(stanowisko);
        panelPra.add(mail);

        //ZAINICJOWANIE POLA TYPU JSCROLLPANEL...
        JScrollPane jScrollPane = new JScrollPane();
        plan.add(jScrollPane, BorderLayout.CENTER);

        //ZAINICJOWANIE POLA TYPU JTABLE
        lista = new JTable();
        jScrollPane.setViewportView(lista);

        //WYWOŁANIE METOD WCZYTUJĄCEJ I ODŚWIEŻJĄCEJ
        wczytanie(null);
        odśwież();

        //UTWORZENIE LOKALNEGO OBIEKTU KLASY JPANEL GRUPUJĄCEGO PRZYCISKI
        JPanel przyciski = new JPanel();
        plan.add(przyciski, BorderLayout.SOUTH);

        //ZAINICJOWANIE PRZYCISKÓW
        zmianaStat = new JButton("Zmiana statusu");
        wyśwOpis = new JButton("Wyświetl opis");
        wylog = new JButton("Wyloguj");
        zmianaStat.addActionListener(this);
        wyśwOpis.addActionListener(this);
        wylog.addActionListener(this);
        przyciski.add(zmianaStat);
        przyciski.add(wyśwOpis);
        przyciski.add(wylog);

        //DODANIE DO OKNA INTERFEJSU WINDOWLISTENER
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                pole1.odłączenie(true, null, null);
                System.exit(0);
            }
        });
    }

    //METODA URUCHAMIAJĄCA PANEL LOGOWANIA
    public void logowanie(DBConnection dbc) {
        LoginPanel lp = new LoginPanel(dbc);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        lp.setVisible(true);
    }

    //METODA WCZYTUJĄCA DANE PRACOWNIKA Z BAZY DANYCH
    public void wczytanie(ResultSet rs) {
        String zapytanie = "SELECT * FROM Employees WHERE user_id='" + użytkownik.getId() + "'";
        ResultSet rezultat;

        //WCZYTYWANIE DANYCH PRACOWNIKA
        rezultat = pole1.zapytanie(zapytanie);
        try {
            if (rezultat != null) {
                if (rezultat.next()) {
                    pracownik = new Employee(rezultat.getInt("id"),
                            rezultat.getString("first_name"),
                            rezultat.getString("last_name"),
                            rezultat.getString("position"),
                            rezultat.getString("email"));
                    pole1.zniszczenie(rezultat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //WCZYTYWANIE DANYCH ZADANIA
        String zapytanie2 = "SELECT * FROM Tasks WHERE employee_id='" + pracownik.getId() + "'";

        rezultat = pole1.zapytanie(zapytanie2);
        try {
            if (rezultat != null) {
                ArrayList<Task> al = new ArrayList<Task>();
                while (rezultat.next()) {
                    Task task = new Task(rezultat.getInt("id"),
                            //rezultat.getInt("employee_id"),
                            rezultat.getString("title"),
                            rezultat.getString("description"),
                            rezultat.getBoolean("status"));
                    al.add(task);
                }
                pracownik.setListaZadań(al);
                pole1.zniszczenie(rezultat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //METODA ODŚWIEŻAJĄCA WIDOK
    public void odśwież() {
        nazwUż.setText(użytkownik.getLogin());
        imie.setText(pracownik.getImię());
        nazwisko.setText(pracownik.getNazwisko());
        stanowisko.setText(pracownik.getStanowisko());
        mail.setText(pracownik.getMail());

        TaskTableModel ttm = new TaskTableModel(pracownik.getListaZadań());

        lista.setModel(ttm);
    }


    public static void main(String[] args) {
        DBConnection db = DBConnection.getInstance();
        LoginPanel lp = new LoginPanel(db);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        //WCIŚNIĘCIE PRZYCISKU "WYLOGUJ"
        if (source == wylog) {
            logowanie(pole1);
            setVisible(false);
            dispose();
        }
        //WCIŚNIĘCIE PRZYCISKU "WYŚWIETL OPIS"
        else if (source == wyśwOpis) {
            //ZCZYTANIE KTÓRY WIERSZ ZOSTAŁ ZAZNACZONY
            int nrWiersza = lista.getSelectedRow();

            if (nrWiersza < 0) {
                //WYŚWIETLENIE KOMUNIKATU "NIE WYBRANO ZADANIA"
                JOptionPane.showMessageDialog(TaskList.this,
                        "Nie wybrano żadnego zadania...", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                //UTWORZENIE OPISU ZADANIA (OBIEKTU KLASY TaskDescription) I WYŚWIETLENIE GO
                Task task = new Task((int) lista.getValueAt(nrWiersza, 0),
                        (String) lista.getValueAt(nrWiersza, 1),
                        (String) lista.getValueAt(nrWiersza, 2),
                        (boolean) lista.getValueAt(nrWiersza, 3));
                TaskDescription td = new TaskDescription(task);
                td.setVisible(true);
            }
        }
        //WCIŚNIĘCIE PRZYCISKU "ZMIANA STATUSU"
        else if (source == zmianaStat) {
            //ZCZYTANIE KTÓRE ZADANIE ZOSTAŁO WYBRANE
            int nrWiersza = lista.getSelectedRow();
            if (nrWiersza < 0) {
                //WYŚWIETLENIE KOMUNIKATU "NIE WYBRANO ZADANIA"
                JOptionPane.showMessageDialog(TaskList.this,
                        "Nie wybrano żadnego zadania...", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                //ZMIANA STATUSU W BAZIE DANYCH ORAZ NA OBIEKCIE KLASY Employee
                Task task = new Task((int) lista.getValueAt(nrWiersza, 0),
                        (String) lista.getValueAt(nrWiersza, 1),
                        (String) lista.getValueAt(nrWiersza, 2),
                        (boolean) lista.getValueAt(nrWiersza, 3));
                String zapytanie = "UPDATE Tasks "
                        + "SET status = "
                        + "CASE "
                        + "WHEN status=1 THEN 0 "
                        + "WHEN status=0 THEN 1 "
                        + "END "
                        + "WHERE id=" + task.getId() + ";";
                boolean mod = pole1.modyfikacja(zapytanie);
                if (mod) {
                    ArrayList<Task> lz = pracownik.getListaZadań();
                    Task zadanie = lz.get(nrWiersza);
                    if (zadanie.getStatus()) zadanie.setStatus(false);
                    else zadanie.setStatus(true);
                    lz.set(nrWiersza, zadanie);
                    pracownik.setListaZadań(lz);
                    odśwież();
                } else {
                    JOptionPane.showMessageDialog(TaskList.this,
                            "Nie udało się wprowadzić zmiany",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
    }
}
