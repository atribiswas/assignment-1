# Assignment submission

## About the assignment
- Scenario :- All the users of City `FanCode` should have more than half of their todos task completed.
- Given User has the todo tasks
- And User belongs to the city FanCode
- Then User Completed task percentage should be greater than 50%

## How to run
Python version used: `3.11.7`

Dependecies used: `requests`

To install dependencies use
```
# Optionalal: create a virtual env
pythonn -m venv venv

# Install dependecies
pip install -r requirements.txt
```

To run the script
```
# Format is: python/python3 app.py <relative path to config file> <Logging level -> INFO/DEBUG>
python app.py config.json INFO
```

## Config file

The file looks like this:
```
{
    "resources": {
        "host": "http://jsonplaceholder.typicode.com", // This is the host
        "todo_path": "todos", // These are the paths (Some are not used. Auth is also integrated but optional)
        "posts_path": "posts",
        "comments_path": "comments",
        "album_path": "album",
        "photos_path": "photos",
        "users_path": "users",
        "auth_token": null
    },
    "city_check": [ // We can put one or more cities here, eg: FanCode, Gwenborough, Wisokyburgh
        "Gwenborough",
        "Wisokyburgh"
    ],
    "task_cutoff_perc": 0.5, // The cutoff threshold to check for in user's todo list
    "output_file": "data/output/output.json" // Relative path where we want to store the results
}
```

## Output file

The file looks like this
```
{
    "total_users": 2, // Total users who have a TODO and belong to the city mentioned
    "overall_perc_tasks_completed": 0.47500000000000003, // Their average completion of TODOs
    "users_above_threshold": [ // Users above the threshold, eg has completed more than 50% of TODOs
        "Bret"
    ],
    "users_below_threshold": [ // Users below the threshold, eg has completed less than 50% of TODOs
        "Antonette"
    ]
}
```