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

## Endpoints and curls request
# Creates a new user.
curl --location 'localhost:8080/api/v1/user/create' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=164417AD50D853715BAE229D6B489B6F' \
--data-raw '{
"identification_type":"CC",
"identification_number":12345678923,
"first_name":"andres",
"last_name":"perez",
"email":"somos@soytu.com",
"birth_date": "1987-12-01"
}'
# get all users
curl --location 'localhost:8080/api/v1/user/get-all' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Cookie: JSESSIONID=164417AD50D853715BAE229D6B489B6F'
# find user by identification_number
curl --location 'localhost:8080/api/v1/user/12345678915' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Cookie: JSESSIONID=164417AD50D853715BAE229D6B489B6F'
# update user
curl --location --request PUT 'localhost:8080/api/v1/user/12345678910' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=164417AD50D853715BAE229D6B489B6F' \
--data-raw '{
"identification_type":"CC",
"first_name":"juan",
"last_name":"ibanez",
"email":"soytu@soyyo.com",
"birth_date": "2005-12-01"
}'
# delete user
curl --location --request DELETE 'localhost:8080/api/v1/user/12345678910' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Cookie: JSESSIONID=164417AD50D853715BAE229D6B489B6F'
# create product
curl --location 'localhost:8080/api/v1/products/create/2' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=164417AD50D853715BAE229D6B489B6F' \
--data '{
"account_type":"current",
"account_balance":50000,
"exenta_gmf":false,
"user_id":2
}'
# get all products
curl --location 'localhost:8080/api/v1/products/all' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Cookie: JSESSIONID=164417AD50D853715BAE229D6B489B6F'
# get product by id
curl --location --globoff 'localhost:8080/api/v1/products/{id}' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Cookie: JSESSIONID=164417AD50D853715BAE229D6B489B6F'
# update product
curl --location --globoff --request PUT 'localhost:8080/api/v1/products/{id}' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=164417AD50D853715BAE229D6B489B6F' \
--data '{
"account_type":"current",
"account_number":"331234567890",
"account_balance":2000,
"status":"active",
"exenta_gmf":false
}'
# delete product
curl --location --globoff --request DELETE 'localhost:8080/api/v1/products/{id}' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=164417AD50D853715BAE229D6B489B6F' \
--data '{
"status":"inactive"
}'
# update product status
curl --location --globoff --request PUT 'localhost:8080/api/v1/products/{id}/status' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=164417AD50D853715BAE229D6B489B6F' \
--data '{
"status":"active"
}'
# deposit transaction
curl --location 'localhost:8080/api/v1/transactions/deposit/1' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=164417AD50D853715BAE229D6B489B6F' \
--data '{
"amount":2000
}'
# withdrawal transaction
curl --location 'localhost:8080/api/v1/transactions/withdraw/1' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=164417AD50D853715BAE229D6B489B6F' \
--data '{
"amount":2000
}'
# transfer transaction
curl --location 'localhost:8080/api/v1/transactions/transfer/2/1' \
--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=164417AD50D853715BAE229D6B489B6F' \
--data '{
"amount":2000
}'