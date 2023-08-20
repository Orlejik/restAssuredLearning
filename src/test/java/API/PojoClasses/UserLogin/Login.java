package API.PojoClasses.UserLogin;

public class Login {
    private String email;
    private String password;

    public Login(String login, String password) {
        this.email = login;
        this.password = password;
    }

    public String getLogin() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
