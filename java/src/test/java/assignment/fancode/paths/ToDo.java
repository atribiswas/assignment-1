package assignment.fancode.paths;

import assignment.fancode.utils.Constants;
import io.restassured.response.Response;
import io.restassured.RestAssured;

public class ToDo {

    public static String path = "/todos";

    public static Response getToDos() {

        Response response = RestAssured.get(Constants.urlHost + path);
        return response;

    }

}
