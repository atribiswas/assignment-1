package assignment.fancode.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Logic {

    // Essentially 2 tags for users, hasTodo and percTodoCompleted
    public static void filterUsersWithToDos(List<Map<String, Object>> userResponse,
            List<Map<String, Object>> todoResponse) {

        List<Object> toRemove = new ArrayList<>();
        for (Map<String, Object> user : userResponse) {
            boolean hasTodo = false;
            Double percTodoCompleted = 0.0;
            Integer countTodo = 1;
            for (Map<String, Object> todo : todoResponse) {
                if (todo.get("userId") == user.get("id")) {
                    hasTodo = true;
                    countTodo++;
                    percTodoCompleted = (boolean) todo.get("completed") ? percTodoCompleted
                            : percTodoCompleted - (1 / countTodo);
                }
            }
            user.put("percTodoCompleted", percTodoCompleted);
            if (!hasTodo) {
                toRemove.add(user);
            }
        }
        userResponse.removeAll(toRemove);

    }

    // Filter the users by basically the list of cities provided
    public static void filterusersByCities(List<Map<String, Object>> userResponse, ArrayList<String> cities) {

        ObjectMapper mapper = new ObjectMapper();
        List<Object> toRemove = new ArrayList<>();

        for (Map<String, Object> user : userResponse) {
            Map<String, String> address = mapper.convertValue(user.get("address"), Map.class);
            String userCity = address.get("city");
            if (!cities.contains(userCity)) {
                toRemove.add(user);
            }
        }

        userResponse.removeAll(toRemove);

    }
}
