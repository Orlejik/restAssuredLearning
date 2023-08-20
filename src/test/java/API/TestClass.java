package API;

import API.PojoClasses.DelayPojo.Delay;
import API.PojoClasses.Resources.Resources;
import API.PojoClasses.UserLogin.Login;
import API.PojoClasses.UserLogin.LoginResponse;
import API.PojoClasses.UserLogin.UnloginResponse;
import API.PojoClasses.UserRegister.UserDataReg;
import API.PojoClasses.UserRegister.UserRegisterResponse;
import API.PojoClasses.UserUpdate.UserCreated;
import API.PojoClasses.UserUpdate.UserTime;
import API.PojoClasses.UsersGet.UserData;
import API.PojoClasses.UsersPost.CreateUserResponse;
import API.PojoClasses.UsersPost.UserDataCreate;
import Specifications.Spectifications;
import io.restassured.http.ContentType;
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
import static io.restassured.RestAssured.proxy;
import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;

public class TestClass {

    public static final String URL = "https://reqres.in/";

    @Test
    public void TestYearAndID() {
        installSpec(request(URL), responseOK200());
        List<UserData> usersList = given()
                .when()
                .get("/api/users?page=2")
                .then()
//                .log().all()
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
                .then()
//                .log().all()
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
                .then()
//                .log().all()
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
                .then()
//                .log().all()
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
                .then()
//                .log().all()
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
                .then()
//                .log().all()
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
                .then()
//                .log().all()
                .extract().as(CreateUserResponse.class);

        String dateTimeNow = reduceStringLength(formatDate(Date.from(Clock.systemUTC().instant())), 6);
        System.out.println(dateTimeNow);
        String timeCreeated = reduceStringLength(formatDate(successCreate.getCreatedAt()), 6);
        System.out.println(timeCreeated);
        System.out.println(successCreate.getCreatedAt().toString());

        Assert.assertEquals(dateTimeNow, timeCreeated);
    }

    @Test
    public void updateUserInfoPut() {

        installSpec(request(URL), responseOK200());
        UserTime userBody = new UserTime("morpheus", "zion resident");

        UserCreated userTime = given()
                .body(userBody)
                .when()
                .put("/api/users/2")
                .then()
//                .log().all()
                .extract()
                .as(UserCreated.class);

        System.out.println(userTime);
    }

    @Test
    public void updateUserInfoPatch() {
        installSpec(request(URL), responseOK200());
        UserTime userTime = new UserTime("morpheus", "zion resident");

        UserCreated userCreated = given()
                .body(userTime)
                .when()
                .patch("/api/users/2")
                .then()
//                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .as(UserCreated.class);
    }

    @Test
    public void deleteUser() {
        installSpec(request(URL), responseDeleted204());

        given()
                .when()
                .delete("/api/users/2")
                .then()
//                .log().all()
                .assertThat().statusCode(204);
    }

    @Test
    public void userSuccessRegister() {
        installSpec(request(URL), responseOK200());
        UserDataReg userData = new UserDataReg("eve.holt@reqres.in", "pistol");

        String token = "QpwL5tke4Pnpja7X4";
        Integer id = 4;

        UserRegisterResponse userReg = given()
                .body(userData)
                .when()
                .post("/api/register")
                .then()
//                .log().all()
                .extract().as(UserRegisterResponse.class);

        Assert.assertEquals(userReg.getToken(), token);
        Assert.assertEquals(userReg.getId(), id);

    }

    @Test
    public void userUnsuccessRigister() {
        installSpec(request(URL), responseNOK400());
        UserDataReg userData = new UserDataReg("sydney@fife", "");

        UserRegisterResponse userReg = given()
                .body(userData)
                .when()
                .post("/api/register")
                .then()
//                .log().all()
                .extract().as(UserRegisterResponse.class);

    }

    @Test
    public void successfulLogin() {
        installSpec(request(URL), responseOK200());
        Login login = new Login("eve.holt@reqres.in", "cityslicka");
        String token = "QpwL5tke4Pnpja7X4";

        LoginResponse loginResponse = given()
                .body(login)
                .when()
                .post("/api/login")
                .then()
//                .log().all()
                .extract().as(LoginResponse.class);
        Assert.assertEquals(token, loginResponse.getToken());
    }

    @Test
    public void unSuccessfulLogin() {
        installSpec(request(URL), responseNOK400());

        Login login = new Login("peter@klaven", "");

        String errorMsg = "Missing password";

        UnloginResponse unLoginResponse = given()
                .body(login)
                .when()
                .post("/api/login")
                .then()
//                .log().all()
                .extract().as(UnloginResponse.class);

        Assert.assertEquals(errorMsg, unLoginResponse.getError());
    }

    @Test
    public void delayedResponse() {
        installSpec(request(URL), responseOK200());

        List<Delay> delay = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users?delay=3")
                .then()
//                .log().all()
                .extract().jsonPath()
                .getList("data", Delay.class);

        delay.stream().forEach(x->Assert.assertTrue(x.getEmail().endsWith("reqres.in")));
        delay.forEach(x->Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
    }
}
