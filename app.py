import json
import logging
import os
import sys

arguments = sys.argv[1:]

if len(arguments) != 2:
    logging.error("Malformed argument list. Usage: python app.py <path to config.json file> <log level -> Debug | INFO >. Exiting")
    sys.exit(1)

logging_level = logging.DEBUG if arguments[1] == "DEBUG" else logging.INFO

logging.basicConfig(level=logging_level,
                    format='%(asctime)s - %(levelname)s - %(message)s')

class Constants:
    def __init__(self, config_json):
        self.host : str = config_json['resources']['host']
        self.todo_path : str = config_json['resources']['todo_path']
        self.user_path : str = config_json['resources']['users_path']
        self.auth_token : str | None = config_json['resources'].get('auth_token')
        self.city_check : str = config_json['city_check']
        self.task_cutoff_perc : int = config_json['task_cutoff_perc']



def main(config_file_path):
    try:
        logging.debug(os.getcwd()+"/"+config_file_path)
        with open(os.getcwd()+"/"+config_file_path, "r") as configfile:
            config = json.load(configfile)
        constants = Constants(config)
    except Exception as e:
        logging.error(f"Malformed Configuration file - {str(e)}. Exiting")
        sys.exit(1)

    logging.info(f"Info on configuration: {constants.host}, {constants.todo_path}, {constants.user_path}, {constants.auth_token}, {constants.city_check}, {constants.task_cutoff_perc}")

if __name__=="__main__":
    config_file_path = arguments[0]
    main(config_file_path)