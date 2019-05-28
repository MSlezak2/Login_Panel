package core;

public class Task {

    int id;// employee_id;
    String tytuł, opis;
    boolean status;

    public void setId(int id) {
        this.id = id;
    }

    public void setTytuł(String tytuł) {
        this.tytuł = tytuł;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public void setStatus(boolean statusl) {
        this.status = statusl;
    }
    /*public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }*/

    public int getId() {
        return id;
    }

    public String getTytuł() {
        return tytuł;
    }

    public String getOpis() {
        return opis;
    }

    public boolean getStatus() {
        return status;
    }
    /*public int getEmployee_id() {
        return employee_id;
    }*/

    public Task(int id, String ty, String op, boolean st) {
        this.id = id;
        tytuł = ty;
        opis = op;
        status = st;
    }

}
