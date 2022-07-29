
# THEAM CRM SERVICE

This is a Spring Boot API for a shop that serves as the backend service for a CRM application. It is used to manage the shop's customer data. The main kinds of entities here are users and customers.


## Prerequisites

- Java 11
- Docker. Install from [Docker Compose](https://docs.docker.com/compose/) 
- Postman. Install from [Postman](https://www.postman.com/downloads/)
## Documentation

[Click me to view Postman Documentation](https://documenter.getpostman.com/view/4634888/UzXNVxwC)

There are two types of roles:
- admin (this translates to ROLE_ADMIN in the database)
- user (this translates to ROLE_USER in the database)
## Features

- Login - the token (JWT) from this endpoint is then used for all subsequent requests.
- User signup (with user or admin role)
- get all users
- update user
- delete user
- add customer (with photo)
- get all customers
- get single customer by id
- update customer
- delete customer


## Tech Stack

- Spring Boot
- Java
- Postgres
- JSON Web Token
- minIO for image uploads (web)
- Docker
- Flyway for DB Migration
- JPA Auditing
- Lombok
- JUnit 5, Hamcrest, Mockito
## Run Locally

Clone the project

```bash
  git clone https://github.com/pato7755/theam-crm
```

Go to the project directory

```bash
  cd theam-crm
```

Installation

```bash
  mvn install
```

Start the application

```bash
  docker-compose up
```

At application startup, an admin user is created. This is with the concept of having some sort of super admin who can add other admins or users. The initial admin's credentials are:
- username - *admin* 
- password - *admin1234*

These credentials can be changed.
## Improvements

- Continuous Deployment using AWS
- Add more integration tests
- Cache customer images
## Support

For support, email pat.essiam701@gmail.com

I am happy to receive feedback or questions.