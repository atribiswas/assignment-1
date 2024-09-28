package assignment.fancode.paths;

import assignment.fancode.utils.Constants;
import io.restassured.response.Response;
import io.restassured.RestAssured;

public class Users {

    public static String path = "/users";

    public static Response getUsers() {

        Response response = RestAssured.get(Constants.urlHost + path);
        return response;

    }

}
