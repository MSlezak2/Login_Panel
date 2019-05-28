package ui;

import core.Task;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TaskTableModel extends AbstractTableModel {

    //TABLICA Z  NAZWAMI KOLUMN
    String nazwKol[] = {"id", "title", "description", "status"};

    //STAŁE POZWALAJĄCE NA DOSTĘP DO POSZCZEGÓLNYCH KOLUMN
    final int k1 = 0, k2 = 1, k3 = 2, k4 = 3;
    //STAŁĄ POZWALAJĄCA NA DOSTĘP DO WSZYSTKICH KOLUMN
    final int kM1 = -1;
    //LISTA ZADAŃ
    List<Task> listaZadań;

    public TaskTableModel(List<Task> l) {
        listaZadań = l;
    }

    @Override
    public int getRowCount() {
        return listaZadań.size();
    }

    @Override
    public int getColumnCount() {
        return nazwKol.length;
    }

    public String getColumnName(int col) {
        return nazwKol[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case k1:
                return listaZadań.get(row).getId();

            case k2:
                return listaZadań.get(row).getTytuł();

            case k3:
                return listaZadań.get(row).getOpis();

            case k4:
                return listaZadań.get(row).getStatus();

            case kM1:
                return listaZadań.get(row);

            default:
                return null;
        }

    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }


}
