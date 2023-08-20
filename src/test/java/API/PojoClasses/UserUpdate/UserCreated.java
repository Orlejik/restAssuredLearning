package API.PojoClasses.UserUpdate;

public class UserCreated extends UserTime{
    private String createdAt;

    public UserCreated(String name, String jod, String createdAt) {
        super(name, jod);
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
