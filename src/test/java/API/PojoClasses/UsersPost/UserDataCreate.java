package API.PojoClasses.UsersPost;

public class UserDataCreate {
    private String name;
    private String job;

    public UserDataCreate(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }
}
