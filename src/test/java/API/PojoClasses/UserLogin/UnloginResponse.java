package API.PojoClasses.UserLogin;

public class UnloginResponse {
    private String error;

    public UnloginResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
