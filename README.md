# TuitionToAll-v2
 
- Tuition to all is a centralized lms system that allows every institute, teachers and students to connect to together


- TuitionToAll-v2 is a comprehensive redevelopment of our TuitionToALl-v1 backend API, transitioning from v1.0.0 to v2.0.0. This overhaul aims to introduce 
  1. Enhanced features.
  2. Resolve current bugs.
  3. Improve overall performance. 


- In this system users have 3 three roles which are institute, teacher and student.Each role has own functionalities like creating courses,adding assignments downloading etc.

<hr />

### All Features 

1. User Registration
2. User Login
   1. Using email and password

### API Endpoints and Request/Response Body

1. User Registration
   - User can register as a institute, teacher or student.
     - ### Institute registration
        ``` 
        http://localhost:8080/api/v2/auth/register/institute
        ```
       - #### Request Body
           ```json
           {
             "instituteName": "Sample Institute",
             "address": "sample address",
             "contact": "0000000000",
             "email": "example@email.com",
             "password": "Password@123"
           }
           ```
       - #### Response Body
           ```json
           {
             "message": "Institute Registered Successfully!",
              "data": {
                "id": 61,
                "instituteName": "Sample Institute",
                "address": "sample address",
                "contact": "0000000000",
                "email": "example@email.com",
                "role": "ROLE_INSTITUTE",
                "disabled": false
              }    
           }
           ```    
     - ### Teacher registration
        ``` 
        http://localhost:8080/api/v2/auth/register/teacher
        ```
       - #### Request Body
           ```json
           {
             "firstName": "SampleFirstName",
             "lastName": "SampleLastName",
             "dob": "yyyy-mm-dd",
             "address": "sample address",
             "contact": "0000000000",
             "email": "example@email.com",
             "password": "Password@123"
           }
           ```
       - #### Response Body
           ```json
           {
             "message": "Teacher Registered Successfully!",
              "data": {
                "id": 62,
                "firstName": "SampleFirstName",
                "lastName": "SampleLastName",
                "dob": "yyyy-mm-dd",
                "address": "sample address",
                "contact": "0000000000",
                "email": "example@email.com",
                "role": "ROLE_TEACHER",
                "disabled": false
              }    
           }
           ```    
     - ### Student registration
        ``` 
        http://localhost:8080/api/v2/auth/register/student
        ```
       - #### Request Body
           ```json
           {
             "firstName": "SampleFirstName",
             "lastName": "SampleLastName",
             "dob": "yyyy-mm-dd",
             "address": "sample address",
             "contact": "0000000000",
             "email": "example@email.com",
             "password": "Password@123"
           }
           ```
       - #### Response Body
           ```json
           {
             "message": "Student Registered Successfully!",
              "data": {
                "id": 62,
                "firstName": "SampleFirstName",
                "lastName": "SampleLastName",
                "dob": "yyyy-mm-dd",
                "address": "sample address",
                "contact": "0000000000",
                "email": "example@email.com",
                "role": "ROLE_TEACHER",
                "disabled": false
              }     
           }
           ```
2. User Login
   1. Using email and password
        - ### Institute login
            ``` 
            http://localhost:8080/api/v2/auth/login
            ```
        - #### Request Body
            ```json
            {
                "token": "YOUR_JWT_TOKEN",
                "user": {
                     "id": 5,
                    "firstName": "Vinuth",
                    "lastName": "Sri Arampath",
                    "dob": "2004-09-18",
                    "address": "75/6A Kottikawaththa Road,Gothatuwa New Town,Angoda",
                    "contact": "0719401853",
                    "email": "vinuthsriarampath.testing@outlook.com",
                    "role": "ROLE_STUDENT",
                    "disabled": false
                } 
            }
            ```
  

      


### Keep your eye open to this project readme to get to know about the status of this project...
#### `system.out.println("Bye Bye for now...👋👋👋")`