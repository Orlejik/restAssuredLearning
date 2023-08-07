package API.PojoClasses.UsersPost;

import java.util.Date;

public class CreateUserResponse extends UserDataCreate {
    private String id;
    private Date createdAt;

    public CreateUserResponse(String name, String job, String id, Date createdAt) {
        super(name, job);
        this.id = id;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
