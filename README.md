# Customer API Demo Project

### Project Summary
This a simple REST API project that does CRUD operation on a Customer table using Spring boot.
Run the CustomerApplication class to start the project.

### Repository
The application uses a mock service to simulate the CRUD operations.

### API Security
I implemented Basic Authentication to simplify security. The user name password are also mocked. Use user1/password1.

In real application it would be ideal to use token based authentication and authorization like JWT or or use session or cookie based depending on the needs.

### Mobile
Enabled SSL as required by mobile. Also implemented basic HATEAOS links for discovery.
I did not implement paging as the project is using a mock Repository


### Polling
Implemented caching to support the requirement for the consumer that does periodic calls.
In real applications it might be ideal to implement something like a webhook.

