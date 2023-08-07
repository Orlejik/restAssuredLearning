package API;

import API.PojoClasses.Resources.Resources;
import API.PojoClasses.UsersGet.UserData;
import API.PojoClasses.UsersPost.CreateUserResponse;
import API.PojoClasses.UsersPost.UserDataCreate;
import org.junit.Assert;
import org.junit.Test;

import java.time.Clock;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static Helpers.StringHelper.formatDate;
import static Helpers.StringHelper.reduceStringLength;
import static Specifications.Spectifications.*;

import static io.restassured.RestAssured.given;

public class TestClass {

    public static final String URL = "https://reqres.in/";

    @Test
    public void TestYearAndID() {
        installSpec(request(URL), responseOK200());
        List<UserData> usersList = given()
                .when()
                .get("/api/users?page=2")
                .then().log().all()
                .extract()
                .body()
                .jsonPath()
                .getList("data", UserData.class);

        System.out.println(usersList);

        usersList.forEach(user -> Assert.assertTrue(user.getAvatar().contains(user.getId().toString())));
        usersList.forEach(user -> Assert.assertTrue(user.getEmail().endsWith("reqres.in")));

    }

    @Test
    public void getSingleUser() {
        installSpec(request(URL), responseOK200());
        UserData user = given()
                .when()
                .get("/api/users/2")
                .then().log().all()
                .extract()
                .body()
                .jsonPath()
                .getObject("data", UserData.class);

        System.out.println(user);
    }

    @Test
    public void singleUserNotFound() {
        installSpec(request(URL), responseNOK404());

        given()
                .when()
                .get("/api/users/23")
                .then().log().all()
                .assertThat().statusCode(404)
                .extract()
                .as(UserData.class);


    }

    @Test
    public void getAllResources() {
        installSpec(request(URL), responseOK200());

        List<Resources> resources = given()
                .when()
                .get("/api/unknown")
                .then().log().all()
                .extract().body().jsonPath()
                .getList("data", Resources.class);

//        String name = name;
        List<String> names = resources.stream().map(Resources::getName).collect(Collectors.toList());
//        Integer year = year;
        List<Integer> years = resources.stream().map(Resources::getYear).collect(Collectors.toList());
//        String color = color;
        List<String> colors = resources.stream().map(Resources::getColor).collect(Collectors.toList());
//        String pantone_value = pantone_value;
        List<String> pantone_values = resources.stream().map(Resources::getPantone_value).collect(Collectors.toList());

        List<Integer> sortedYears = years.stream().sorted().collect(Collectors.toList());
        System.out.println(years);
        System.out.println(sortedYears);
        Assert.assertEquals(years, sortedYears);


    }

    @Test
    public void getSingleResource() {
        installSpec(request(URL), responseOK200());
        Resources resource = given()
                .when()
                .get("/api/unknown/2")
                .then().log().all()
                .extract().body().jsonPath().getObject("data", Resources.class);

        System.out.println(resource);

        Assert.assertEquals("2001", resource.getYear().toString());
    }

    @Test
    public void singleResourceNotFound() {
        installSpec(request(URL), responseNOK404());

        given()
                .when()
                .get("/api/unknown/23")
                .then().log().all()
                .assertThat().statusCode(404)
                .extract().as(Resources.class);

    }

    @Test
    public void createNewUser() {
        installSpec(request(URL), responseCreated201());

        String regex = "(.{5})$";

        UserDataCreate newUser = new UserDataCreate("morpheus", "leader");

        CreateUserResponse successCreate = given()
                .body(newUser)
                .when()
                .post("/api/users")
                .then().log().all()
                .extract().as(CreateUserResponse.class);

        String dateTimeNow = reduceStringLength(formatDate(Date.from(Clock.systemUTC().instant())), 6);
        System.out.println(dateTimeNow);
        String timeCreeated = reduceStringLength(formatDate(successCreate.getCreatedAt()), 6);
        System.out.println(timeCreeated);
        System.out.println(successCreate.getCreatedAt().toString());

        Assert.assertEquals(dateTimeNow, timeCreeated);
    }
}
