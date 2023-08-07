from mongo_connection import client

db = client["employees_db"]

# inserting in employeeInfo Collection
collection = db["employeeInfo"]
data_to_insert = [
    {
        "EmpID": 1,
        "EmpFname": "Sanjay",
        "EmpLname": "Mehra",
        "Department": "HR",
        "Project": "P1",
        "Address": {
            "City": "Hyderabad(HYD)",
            "State": "Telangana"
        },
        "DOB": {
            "day": 1,
            "month": 12,
            "year": 1976
        },
        "Gender": "M"
    },
    {
        "EmpID": 2,
        "EmpFname": "Ananya",
        "EmpLname": "Mishra",
        "Department": "Admin",
        "Project": "P2",
        "Address": {
            "City": "Delhi(DEL)",
        },
        "DOB": {
            "day": 2,
            "month": 5,
            "year": 1968
        },
        "Gender": "F"
    },
    {
        "EmpID": 3,
        "EmpFname": "Rohan",
        "EmpLname": "Diwan",
        "Department": "Account",
        "Project": "P3",
        "Address": {
            "City": "Mumbai(BOM)",
            "State": "Maharashtra"
        },
        "DOB": {
            "day": 1,
            "month": 1,
            "year": 1980
        },
        "Gender": "M"
    },
    {
        "EmpID": 4,
        "EmpFname": "Sonia",
        "EmpLname": "Kulkarni",
        "Department": "HR",
        "Project": "P1",
        "Address": {
            "City": "Hyderabad(HYD)",
            "State": "Telangana"
        },
        "DOB": {
            "day": 2,
            "month": 5,
            "year": 1992
        },
        "Gender": "F"
    },
    {
        "EmpID": 5,
        "EmpFname": "Ankit",
        "EmpLname": "Kapoor",
        "Department": "Admin",
        "Project": "P2",
        "Address": {
            "City": "Delhi(DEL)",
        },
        "DOB": {
            "day": 3,
            "month": 7,
            "year": 1994
        },
        "Gender": "M"
    }
]

collection.insert_many(data_to_insert)

# inserting in employeePosition Collection
collection = db["employeePosition"]
data_to_insert = [
    {
        "EmpID": 1,
        "EmpPosition": "Manager",
        "DateOfJoining": {
            "day": 1,
            "month": 5,
            "year": 2022
        },
        "Salary": 500000
    },
    {
        "EmpID": 2,
        "EmpPosition": "Executive",
        "DateOfJoining": {
            "day": 2,
            "month": 5,
            "year": 2022
        },
        "Salary": 75000
    },
    {
        "EmpID": 3,
        "EmpPosition": "Manager",
        "DateOfJoining": {
            "day": 1,
            "month": 5,
            "year": 2022
        },
        "Salary": 90000
    },
    {
        "EmpID": 2,
        "EmpPosition": "Lead",
        "DateOfJoining": {
            "day": 2,
            "month": 5,
            "year": 2022
        },
        "Salary": 85000
    },
    {
        "EmpID": 1,
        "EmpPosition": "Executive",
        "DateOfJoining": {
            "day": 1,
            "month": 5,
            "year": 2022
        },
        "Salary": 300000
    }
]

# Insert data into the collection
collection.insert_many(data_to_insert)

# Close the MongoDB connection
client.close()

print("Data inserted successfully into our employees database!")
