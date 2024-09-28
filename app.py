import json
import logging
import os
import sys
import requests

#Setup
arguments = sys.argv[1:]
# arguments = ["config.json", "DEBUG"]
if len(arguments) != 2:
    logging.error("Malformed argument list. Usage: python app.py <path to config.json file> <log level -> Debug | INFO >. Exiting")
    sys.exit(1)

logging_level = logging.DEBUG if arguments[1] == "DEBUG" else logging.INFO

logging.basicConfig(level=logging_level,
                    format='%(asctime)s - %(levelname)s - %(message)s')

#Using configurations as constants
class Constants:
    def __init__(self, config_json):
        self.host : str = config_json['resources']['host']
        self.todo_path : str = config_json['resources']['todo_path']
        self.user_path : str = config_json['resources']['users_path']
        self.auth_token : str | None = config_json['resources'].get('auth_token')
        self.city_check : str = config_json['city_check']
        self.task_cutoff_perc : int = config_json['task_cutoff_perc']
        self.output_file : int = config_json['output_file']



def main(config_file_path):
    try:
        logging.debug(os.getcwd()+"/"+config_file_path)
        with open(os.getcwd()+"/"+config_file_path, "r") as configfile:
            config = json.load(configfile)
        constants = Constants(config)
    except Exception as e:
        logging.error(f"Malformed Configuration file - {str(e)}. Exiting")
        sys.exit(1)

    logging.debug(f"""Info on configuration: {constants.host}, 
                  {constants.todo_path}, 
                  {constants.user_path}, 
                  {constants.auth_token}, 
                  {constants.city_check}, 
                  {constants.task_cutoff_perc},
                  {constants.output_file}""")
    
    # get users
    logging.debug(constants.host+"/"+constants.user_path)
    logging.debug(f"Authorization: Bearer {constants.auth_token}")
    response = requests.get(constants.host+"/"+constants.user_path,
                         headers=f"Authorization: Bearer {constants.auth_token}" if constants.auth_token else None)
    if(response.status_code != 200):
        logging.error("Error fetching user details, non OK status code")
        sys.exit(1)

    users = response.json()
    logging.info(f"Users: {users}")

    # filter user has toDo tasks
    response = requests.get(constants.host+"/"+constants.todo_path,
                            headers=f"Authorization: Bearer {constants.auth_token}" if constants.auth_token else None)
    if(response.status_code != 200):
        logging.error("Error fetching user details, non OK status code")
        sys.exit(1) 
    
    todoTasks = response.json()
    logging.info(f"TODO tasks: {todoTasks}")

    filtered_users_bytodo = [user for user in users if user['id'] in {todo['userId'] for todo in todoTasks}]
    logging.info(f"Users filtered by todos {filtered_users_bytodo}")

    # user belongs to assigned city?
    filtered_users_bycity = [user for user in users if user['address']['city'] in constants.city_check]
    logging.info(f"Users filtered by city {filtered_users_bycity}")

    # % of toDo tasks completed
    for user in filtered_users_bycity:
        tasks = [task for task in todoTasks if task['userId'] == user['id']]
        user['perc_task_completed'] = len([1 for task in tasks if task['completed']])/len(tasks)
        logging.info(f"% Task completed by user {user['username']} is {round(user['perc_task_completed']*100)}%")

    # bifocate users into below and above threshold, store metadata
    result = {
        'total_users': len(filtered_users_bycity),
        'overall_perc_tasks_completed': sum([user['perc_task_completed'] for user in filtered_users_bycity]) / len(filtered_users_bycity),
        'users_above_threshold': [user['username'] for user in filtered_users_bycity if user['perc_task_completed']>=constants.task_cutoff_perc],
        'users_below_threshold': [user['username'] for user in filtered_users_bycity if user['perc_task_completed']<constants.task_cutoff_perc]
    }
    logging.info(f"output-> {result}")

    with open(os.getcwd()+"/"+constants.output_file, "w") as jsonfile:
        json.dump(result, jsonfile, indent=4)

    # assert if all users from assigned city are above threshold
    assert result['users_below_threshold'] == [], "There are users below the given threshold. Test Case Failed."
    logging.info("No users found below the given threshold. Test Case Passed.")

if __name__=="__main__":
    config_file_path = arguments[0]
    main(config_file_path)