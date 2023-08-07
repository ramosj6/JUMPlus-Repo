from mongo_connection import client
from pprint import pprint

db = client["employees_db"]
employees_info = db["employeeInfo"]
employees_position = db["employeePosition"]

# Mongo Exercise query questions
# 1. Write a query to fetch the EmpFname from the EmployeeInfo collection
result = employees_info.find({}, {"EmpFname": 1})

for name in result:
    print(name["EmpFname"])

# 2. Write a query to fetch the number of employees working in the department ‘HR’.
count_hr_employees = employees_info.count_documents({"Department": "HR"})
print(f"Number of employees in the 'HR' department: {count_hr_employees}")

# 3. Write a query to get the current date.
current_date = db.command({"serverStatus": 1})['localTime']

print("Current date from MongoDB:", current_date)

# 4. Write a query to retrieve the first four characters of EmpLname from the EmployeeInfo collection.
pipeline = [
    {"$project": {"FirstFourCharsOfEmpLname": {"$substr": ["$EmpLname", 0, 4]}}}
]

result = employees_info.aggregate(pipeline)

for name in result:
    print(name["FirstFourCharsOfEmpLname"])

# 5. Write a query to fetch only the place name(string before brackets) from the
# Address field of EmployeeInfo collection.
pipeline = [
    {
        "$project": {
            "PlaceName": {
                "$arrayElemAt": [{"$split": ["$Address.City", "("]}, 0]
            }
        }
    }
]

result = employees_info.aggregate(pipeline)
print("\nPlaceName: ")
for place in result:
    print(place["PlaceName"])

# 6. Write a query to find all the employees whose salary is between 50000 to 100000.
pipeline = [
    {
        "$match": {"Salary": {"$gte": 50000, "$lte": 100000}}
    },
    {
        "$lookup": {
            "from": "employeeInfo",
            "localField": "EmpID",
            "foreignField": "EmpID",
            "as": "employeeDetails"
        }
    },
    {
        "$unwind": "$employeeDetails"
    },
    {
        "$group": {
            "_id": "$EmpID",
            "EmpFname": {"$first": "$employeeDetails.EmpFname"},
            "EmpLname": {"$first": "$employeeDetails.EmpLname"},
            "EmpPosition": {"$first": "$EmpPosition"},
            "DateOfJoining": {"$first": "$DateOfJoining"},
            "Salary": {"$first": "$Salary"}
        }
    }
]
result = employees_position.aggregate(pipeline)
print("\nEmployees within the salary range: ")
for name in result:
    print(name["EmpFname"])

# 7. Write a query to find the names of employees that begin with ‘S’
query = {"EmpFname": {"$regex": "^S", "$options": "i"}}
result = employees_info.find(query)

print("\nEmployees beginning with the letter 'S': ")
for emp in result:
    pprint(emp)

# 8. Write a query to retrieve the EmpFname and EmpLname in a single field
# “FullName”. The first name and the last name must be separated with space.
pipeline = [
    {
        "$project": {
            "_id": 0,
            "FullName": {
                "$concat": ["$EmpFname", " ", "$EmpLname"]
            }
        }
    }
]

print("\nFull names of Employee: ")
result = employees_info.aggregate(pipeline)
for name in result:
    print(name["FullName"])

# 9. Write a query to fetch all the records from the EmployeeInfo collection
# ordered by EmpLname in descending order and Department in the ascending
# order.
sort_order = [("EmpLname", -1), ("Department", 1)]

result = employees_info.find({}).sort(sort_order)

print("\n Fetching all the records from the EmployeeInfo collection ordered by EmpLname in descending order and Department in the ascending order")
for rec in result:
    pprint(rec)

# 10. Write a query to fetch details of all employees excluding the employees
# with first names, “Sanjay” and “Sonia” from the EmployeeInfo collection.
query = {"EmpFname": {"$nin": ["Sanjay", "Sonia"]}}
result = employees_info.find(query)

print("\nAll details of employees excluding Sanjay and Sonia:")
for emp in result:
    pprint(emp)

# 11. Write a query to fetch details of employees with the address as “DELHI(DEL)”.
query = {"Address.City": "Delhi(DEL)"}
result = employees_info.find(query)

print("\nEmployees with the address as 'Delhi':")
for emp in result:
    pprint(emp)

# 12. Write a query to fetch all employees who also hold the managerial position.
pipeline = [
    {
        "$match": {"EmpPosition": "Manager"}
    },
    {
        "$lookup": {
            "from": "employeeInfo",
            "localField": "EmpID",
            "foreignField": "EmpID",
            "as": "employeeDetails"
        }
    },
    {
        "$unwind": "$employeeDetails"
    },
    {
        "$group": {
            "_id": "$EmpID",
            "EmpFname": {"$first": "$employeeDetails.EmpFname"},
            "EmpLname": {"$first": "$employeeDetails.EmpLname"}
        }
    }
]

result = employees_position.aggregate(pipeline)

print("\nEmployees who also holds the Manager position:")
for emp in result:
    print(emp["EmpFname"], emp["EmpLname"])

# 13. Write a query to fetch the department-wise count of employees sorted by
# department’s count in ascending order.
pipeline = [
    {
        "$group": {
            "_id": "$Department",
            "count": {"$sum": 1}
        }
    },
    {
        "$sort": {"count": 1}
    }
]

result = employees_info.aggregate(pipeline)

print("\nDepartment wise count in ascending order:")
for emp in result:
    pprint(emp)

#14. Write a query to retrieve two minimum and maximum salaries from the EmployeePosition collection
sort_order_min = [("Salary", 1)]
sort_order_max = [("Salary", -1)]

min_salaries = employees_position.find().sort(sort_order_min).limit(2)
max_salaries = employees_position.find().sort(sort_order_max).limit(2)

print("\nQuestion 14:")
# Print the results for minimum salaries
print("Two Minimum Salaries:")
for min in min_salaries:
    print(min["Salary"])

# Print the results for maximum salaries
print("\nTwo Maximum Salaries:")
for max in max_salaries:
    print(max["Salary"])

# 15. Write a query to retrieve duplicate records from a collection.
pipeline = [
    {
        "$group": {
            "_id": "$EmpFname",
            "count": {"$sum": 1},
            "docs": {"$push": "$_id"}
        }
    },
    {
        "$match": {
            "count": {"$gt": 1}
        }
    }
]

result = employees_info.aggregate(pipeline)

print("\nQuestion 15:")
# If nothing prints, no duplicates found
for doc in result:
    duplicate_ids = doc["docs"]
    duplicate_documents = employees_info.find({"_id": {"$in": duplicate_ids}})
    for duplicate_doc in duplicate_documents:
        print(duplicate_doc)

# 16. Write a query to retrieve the list of employees working in the same department.
pipeline = [
    {
        "$group": {
            "_id": "$Department",
            "employees": {
                "$push": {
                    "EmpFname": "$EmpFname",
                    "EmpLname": "$EmpLname"
                }
            }
        }
    }
]

result = employees_info.aggregate(pipeline)

print("\nQuestion 16:")
for emp in result:
    print("Department:", emp["_id"])
    for employee in emp["employees"]:
        print("  ", employee["EmpFname"], employee["EmpLname"])

# 17. Write a query to retrieve the last 3 records from the EmployeeInfo collection.
sort_order = [("_id", -1)]
result = employees_info.find().sort(sort_order).limit(3)

print("\nQuestion 17:")
for emp in result:
    pprint(emp)

# 18. Write a query to find the third-highest salary from the EmpPosition collection.
distinct_salaries = employees_position.distinct("Salary")
sorted_salaries = sorted(distinct_salaries, reverse=True)

third_highest_salary = None
if len(sorted_salaries) >= 3:
    third_highest_salary = sorted_salaries[2]

print("\nQuestion 18:")
print("Third-highest salary:", third_highest_salary)

# 19. Write a query to display the first and the last record from the EmployeeInfo collection.
first_record = employees_info.find_one({}, sort=[("_id", 1)])

# Find the last record
last_record = employees_info.find_one({}, sort=[("_id", -1)])

# Print the results
print("\nQuestion 19:")
print("First record:")
pprint(first_record)
print("Last record:")
pprint(last_record)

# 20. Write a query to retrieve Departments who have less than 2 employees
# working in it.
pipeline = [
    {
        "$group": {
            "_id": "$Department",
            "employeeCount": {"$sum": 1}
        }
    },
    {
        "$match": {
            "employeeCount": {"$lt": 2}
        }
    }
]

result = employees_info.aggregate(pipeline)

print("\nQuestion 20:")
for emp in result:
    print("Department:", emp["_id"], "\n Employee Count:", emp["employeeCount"])

#21. Write a query to retrieve EmpPostion along with total salaries paid for each of them.
pipeline = [
    {
        "$group": {
            "_id": "$EmpPosition",
            "totalSalary": {"$sum": "$Salary"}
        }
    }
]

result = employees_position.aggregate(pipeline)

print("\nQuestion 21:")
for emp in result:
    print("EmpPosition:", emp["_id"], "\n Total Salary:", emp["totalSalary"])


client.close()