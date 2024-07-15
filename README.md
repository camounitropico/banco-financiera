# banco-financiera
## Description

This is an application for managing customers of a financial institution. It allows the registration, updating, and deletion of customers, the creation of financial products for customers, and the execution of transactional movements in these products.

## Requirements

- Java 17
- Gradle
- postgresql

## Instructions to start the project

1. Clone the repository: `git clone https://github.com/username/repository.git`
2. Navigate to the project directory: `cd repository`
3. Compile the project: `gradle build`
4. Start the application: `gradle bootRun`

## run application
1. First, you need to have Docker installed on your machine. If you haven't done so yet, you can download it from the official Docker page.
2. You will need a docker compose image for postgresql. The next image is a good option: https://hub.docker.com/_/postgres
   version: '3.8'
   services:

postgres:
image: postgres:latest
environment:
- POSTGRES_DB=banco-financiera
- POSTGRES_USER=admin
- POSTGRES_PASSWORD=admin
ports:
- 5432:5432

copy an paste de text in a file called docker-compose.yml 
3. Once you have Docker installed, you can run the following command to start a PostgreSQL container: docker compose up console command (in file before created with name docker-compose.yml ) 

run BancoFinancieraApplication.java

## Endpoints

### POST /api/customers

Creates a new user.


