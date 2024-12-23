package Model;

public class Utilisateur {
    private String username;
    private String password;
    private String email;
    private String role;
    private String age;
    private int id;

    
    public Utilisateur(String username, String password, String email, String role, String age, int id) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.age = age;
        this.id = id;
    }

    public Utilisateur(String username, String password, String email, String role, String age) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.age = age;
    }

    public Utilisateur(String username, String email, String role, String age) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.age = age;
    }

    public Utilisateur(String username) {
        this.username = username;
    }



    public String getNom() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getAge() {
        return age;
    }
}
