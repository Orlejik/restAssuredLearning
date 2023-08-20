package API.PojoClasses.UserRegister;

public class UserRegisterResponse {

    private Integer id;
    private String token;

    public UserRegisterResponse(Integer id, String token) {
        this.id = id;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
