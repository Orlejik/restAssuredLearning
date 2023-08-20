package API.PojoClasses.UserRegister;

public class UserDataReg {
    private String email;
    private String password;

    public UserDataReg(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
