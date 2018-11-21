
This is my "Ticket system" project. 
In this application users can submit tickets with their issues and admin can resolve it. There is two different interface, one for the user and one for admin. The application detects who is who by looking at the user list CSV file. 
This application connected to my free MySQL database, so it has fixed size. Therefore, if someone wants to use it with your own database, you probably need to write another connection string, and manually call "createTable" and "addUsers" methods.
In order to explore how it is works, you can find executable jar file inside of the project folder. 
Admin profile:   Username: Mykola,  Password: admin1
User profile:    Username: John,    Password: user1234
