package assignment.fancode.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Logic {

    // Essentially 2 tags for users, hasTodo and percTodoCompleted
    public static void filterUsersWithToDos(List<Map<String, Object>> users,
            List<Map<String, Object>> todos) {

        List<Object> toRemove = new ArrayList<>();
        for (Map<String, Object> user : users) {
            boolean hasTodo = false;
            Double countTodo = 0.0;
            Double completedTodo = 0.0;
            for (Map<String, Object> todo : todos) {
                if (todo.get("userId") == user.get("id")) {
                    hasTodo = true;
                    countTodo++;
                    completedTodo = (Boolean) todo.get("completed") ? completedTodo + 1 : completedTodo;
                }
            }
            Double percTodoCompleted = completedTodo / countTodo;

            user.put("percTodoCompleted", percTodoCompleted);
            if (!hasTodo) {
                toRemove.add(user);
            }
        }
        users.removeAll(toRemove);

    }

    // Filter the users by basically the list of cities provided
    public static void filterusersByCities(List<Map<String, Object>> users, ArrayList<String> cities) {

        ObjectMapper mapper = new ObjectMapper();
        List<Object> toRemove = new ArrayList<>();

        for (Map<String, Object> user : users) {
            Map<String, String> address = mapper.convertValue(user.get("address"), Map.class);
            String userCity = address.get("city");
            if (!cities.contains(userCity)) {
                toRemove.add(user);
            }
        }

        users.removeAll(toRemove);

    }

    // Basically takes out the average of the user list
    public static Double takeOutAverage(List<Map<String, Object>> users) {
        Double total = 0.0;
        for (Map<String, Object> user : users) {
            total = (Double) user.get("percTodoCompleted");
        }
        return total / users.size();
    }

    // Filter out users who have less than X% completion
    public static void filterusersByPercCompletion(List<Map<String, Object>> users, Double completionThreshold) {

        List<Object> toRemove = new ArrayList<>();

        for (Map<String, Object> user : users) {
            if ((Double) user.get("percTodoCompleted") >= completionThreshold) {
                toRemove.add(user);
            }
        }
        users.removeAll(toRemove);

    }
}
