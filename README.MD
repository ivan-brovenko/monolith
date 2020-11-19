Represents a simple Spring Boot application with monolithic architecture.

REST:
- POST Register user
- GET All users
- GET User by email
- PUT Update user by id
- DELETE User by id
- POST User's favourite actor from TMDB
- DELETE User's favourite actor

For starting the app: 
1. Upadte tmdb.apikey in src\main\resources\application.yml
2. Run mvn clean install
3. Run mvn spring-boot:run