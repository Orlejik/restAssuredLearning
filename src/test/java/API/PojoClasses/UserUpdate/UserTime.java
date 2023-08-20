package API.PojoClasses.UserUpdate;

public class UserTime {
    private String name;
    private String jod;

    public UserTime(String name, String jod) {
        this.name = name;
        this.jod = jod;
    }

    public String getName() {
        return name;
    }

    public String getJod() {
        return jod;
    }
}
