package core;

public class User {

    int id;
    String login;
    String hasło;

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setHasło(String hasło) {
        this.hasło = hasło;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getHasło() {
        return hasło;
    }

    public User(int id, String log, String has) {
        this.id = id;
        login = log;
        hasło = has;
    }

}
