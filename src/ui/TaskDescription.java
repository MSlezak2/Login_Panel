package ui;

import core.Task;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskDescription extends JDialog implements ActionListener {

    JPanel plan;
    JLabel tytuł;
    JTextArea opis;
    JButton zamknij;

    public TaskDescription(Task task) {
        //NADANIE TYTUŁU, ROZMIARU LAYOUTU OKNA
        setTitle("Opis zadania");
        setBounds(100, 100, 450, 168);
        getContentPane().setLayout(new BorderLayout());

        //ZAINICJOWANIE, USTAWIENIE MIEJSCA I LAYOUTU PANELU
        plan = new JPanel();
        plan.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(plan, BorderLayout.CENTER);
        plan.setLayout(new BorderLayout());

        //DODANIE ETYKIETY "TYTUŁ"
        tytuł = new JLabel("Tytuł");
        plan.add(tytuł, BorderLayout.NORTH);

        //DODANIE POLA Z TEKSTEM TREŚCI ZADANIA
        opis = new JTextArea(task.getOpis());
        opis.setLineWrap(true);
        plan.add(opis, BorderLayout.CENTER);

        //UTWORZENIE LOKALNEGO POLA GRUPUJĄCEGO PRZYCISKI
        JPanel przyciski = new JPanel();
        przyciski.setLayout(new FlowLayout(FlowLayout.RIGHT));
        plan.add(przyciski, BorderLayout.SOUTH);

        //DODANIE PRZYCISKU "ZAMKNIJ"
        zamknij = new JButton("Zamknij");
        zamknij.addActionListener(this);
        przyciski.add(zamknij);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
    }
}
