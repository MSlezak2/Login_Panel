package ui;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import core.User;
import dao.DBConnection;
import org.jasypt.digest.config.SimpleDigesterConfig;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPanel extends JFrame implements ActionListener {

    DBConnection pole1;
    User użytkownik;
    JPanel plan;
    JTextField nazwa;
    JPasswordField hasło;
    JButton potwierdź, zamknij;

    public LoginPanel(DBConnection dbc) {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pole1 = dbc;
        //USTAWIENIE TYTUŁU I WYMIARÓW OKNA
        setTitle("Aplikacja");
        setBounds(100, 100, 450, 168);

        //USTALENIE LAYOUTU OKNA
        getContentPane().setLayout(new BorderLayout());

        //ZAINICJOWANIE OBIEKTU KLASY JPANEL, USTAWIENIE ROZMIARU I ROZMIESZCZENIA
        plan = new JPanel();

        plan.setBorder(new EmptyBorder(5, 5, 5, 5));

        getContentPane().add(plan, BorderLayout.CENTER);

        //DODANIE LAYOUTU DO OBIEKTU KLASY JPANEL
        plan.setLayout(new FormLayout(new ColumnSpec[]{
                FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
        },
                new RowSpec[]{
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC,
                }
        ));

        //DODANIE ETYKIET I PÓL TEKSTOWYCH
        JLabel lblUser = new JLabel("Login");
        plan.add(lblUser, "2,2,right,default");
        nazwa = new JTextField();
        nazwa.setText("PonJa");
        plan.add(nazwa, "4,2,fill,default");

        JLabel lblPassword = new JLabel("Hasło");
        plan.add(lblPassword, "2,4,right,default");
        hasło = new JPasswordField();
        hasło.setText("e00cf25ad42683b3df678c61f42c6bda");
        plan.add(hasło, "4,4,fill,default");

        //UTWORZENIE LOKALNEGO OBIEKTU KLASY JPANEL, USTAWIENIE LAYOUTU
        // I DODANIE GO DO STARSZEGO POLA KLASY JPANEL
        JPanel jPanel = new JPanel();

        jPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        getContentPane().add(jPanel, BorderLayout.SOUTH);

        //ZAINICJOWANIE PRZYCISKÓW
        potwierdź = new JButton("Potwierdź");
        potwierdź.addActionListener(this);
        jPanel.add(potwierdź);

        zamknij = new JButton("Zamknij");
        zamknij.addActionListener(this);
        jPanel.add(zamknij);

        //DODANIE INTERFEJSU WINDOWLISTENER
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                pole1.odłączenie(true, null, null);
                System.exit(0);
            }
        });

        setVisible(true);

    }

    //METODA SPRAWDZAJĄCA POPRAWNOŚĆ DANYCH LOGOWANIA
    public boolean sprawdzenie() {

        //USTAWIENIE KONFIGURACJI ALGORYTMU SZYFROWANIA (ALGORYTM SZYFRUJĄCY, LICZBA ITERACJI, LICZBA BITÓW SALT
        SimpleDigesterConfig KONFIG = new SimpleDigesterConfig();
        KONFIG.setAlgorithm("MD5");
        KONFIG.setIterations(1);
        KONFIG.setSaltSizeBytes(0);

        //UTWORZENIE OBIEKTU SZYFRUJĄCEGO
        ConfigurablePasswordEncryptor SZYFR = new ConfigurablePasswordEncryptor();
        SZYFR.setConfig(KONFIG);
        SZYFR.setStringOutputType("hexadecimal");

        /*          TEGO FRAGMENTU KODU UŻYŁEM TYLKO ZA PIERWSZYM RAZEM ABY DODAĆ DO
                    BAZY DANYCH NOWEJ KOLUMNY PRZECHOWUJĄCEJ HASH HASŁA
        String passUser1=SZYFR.encryptPassword("e00cf25ad42683b3df678c61f42c6bda");
        String passUser2=SZYFR.encryptPassword("c84258e9c39059a89ab77d846ddab909");
        String passUser3=SZYFR.encryptPassword("32cacb2f994f6b42183a1300d9a3e8d6");
        System.out.println(passUser1+"\n"+passUser2+"\n"+passUser3);*/

        //ZASZYFROWANIE HASŁA PODAWANEGO PRZEZ UŻYTKOWNIKA
        String ZASZYFROWANIE = SZYFR.encryptPassword(new String(hasło.getPassword()));

        //ZAPYTANIE KTÓRE MA BYĆ WYSŁANE DO BAZY DANYCH (ZMODYFIKOWANE)
        String zapytanie = "SELECT * FROM Users WHERE name='" + nazwa.getText() +
                "' AND password_hash='" + ZASZYFROWANIE + "'";

        ResultSet rezultat = pole1.zapytanie(zapytanie);
        //SPRAWDZENIE CZY DANE SĄ POPRAWNE
        try {
            if (rezultat == null) {
                rezultat.close();
                return false;
            } else {
                if (rezultat.next()) {
                    użytkownik = new User(rezultat.getInt("id"),
                            rezultat.getString("name"),
                            rezultat.getString("password"));
                    pole1.zniszczenie(rezultat);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //METODA POZWALAJĄCA NA ZALOGOWANIE SIĘ DO SYSTEMU
    public void zaloguj() {
        //ZAMKNIĘCIE OKNA LOGOWANIA
        setVisible(false);
        dispose();
        //OTWORZENIE NOWEGO OKNA
        TaskList tl = new TaskList(pole1, użytkownik);
        tl.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        //WCIŚNIĘCIE PRZYCISKU POTWIERDZAJĄCEGO
        if (source == potwierdź) {
            if (sprawdzenie()) {
                zaloguj();
            } else {
                JOptionPane.showMessageDialog(LoginPanel.this,
                        "WPROWADZONO BŁĘDNE DANE.\nPROSZĘ WPROWADZIĆ " +
                                "PONOWNIE...",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else if (source == zamknij) {
            pole1.odłączenie(true, null, null);
            dispose();
        }
    }
}
