# Product Management
This is a simple Java Based REST service for managing Products

### PREREQUISITES
Below are the prerequisites to run the application.

* Java 8
* Maven

### EXTRACT and BUILD PROJECT

1. Extract the project folder 
2. Run command "mvn clean install"

### HOW TO RUN As CMD

* Run "java -jar target\EmployeeMicroservice-0.0.1.jar"

### HOW TO RUN in Eclipse

1. Import project in Eclipse as Maven project
2. Run EmployeeMicroserviceApplication as 'Java Application'

### Test the CRUD URLs

* The Application will run on port 8080 as default port in Spring Boot
* Test below URLs on Postman or any REST Client.

1. Create new Product

  http://localhost:8080/product <br>
  Method = POST <br>
  Payload: { "name":"Iphone12", "description":"Iphone12 description", "price": 2000.99 } <br>
  
  It will create a Product, and return the Link with Generated ID of the product.
  
2. Get a single product
 
  http://localhost:8080/product/2?currency=EUR <br>
  Method = GET
  
  This will return the details of a single product whose ID = 2. If currency is provided, it will convert the price using exchange rate.
  Interally this will also increment the hit count of this product.
  
3. List the most viewed products

  http://localhost:8080/product/mostViewed?currency=EUR&limit=6 <br>
  Method = GET
  
  It will return the most viewed products (whose hit count > = 1). The optional currency value will convert the price using exchange rate. The limit parameter is used to filter the response.

4. Update Employee

  http://localhost:8080/product/2 <br>
  Method = DELETE
  
  This will internally update the active value of product to false. As per requirement the Product must me there for auditing purpose.
  
### External APIs

Used https://open.exchangerate-api.com/v6/latest for retrieveing the latest exchange rates.

### Technolgy stacks
1. Spring Boot Web - For creating APIs
2. H2 as a database
3. GSON - To convert 3rd party rest response to JSON
4. Spring boot starter cache - For caching API response

### Database schema
Product (
  id INT(20) PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(250) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  description VARCHAR(250) DEFAULT NULL,
  active BOOLEAN DEFAULT true,
  currency VARCHAR(250) DEFAULT NULL
);

Product_Hit (
  id INT,
  hit INT
);

### Features to note
1. The Currency API fetches exchange rate data from 3rd party API and stores in Cache on Application Bootstrap.
2. The Scehduler runs every one hour i.e. 3600000 milliseconds(customizable) to call 3rd party again and update the data in cache.
3. The /mostViewed service will only return products which are not marked deleted.

