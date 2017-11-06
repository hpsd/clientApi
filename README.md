#clientApi Project

This project constitutes the Back-end client based on Spring Boot.

It includes a self-contained Server and in-memory H2 Database.

CRUD operations can be performed by the user using HTTP/REST.

 This client calls another Back-end REST Server to fetch Data.
 
 That Back-end REST Server is in another project based on the play framework.
 
      Spring Boot REST Endpoint ------------------- Play framework REST Server 
      (CRUD and storage of Data)                   (GET User data only)
 
 
 The REST API exposed by this project can be explored using the following links:  
 


##SEARCH :
 
GET http://localhost:8080/clientapi/v1/users/search/getMaxId

GET http://localhost:8080/clientapi/v1/users/search/findByName{?name}

GET http://localhost:8080/clientapi/v1/users/search/findByNameIgnoreCaseContaining{?name}

GET http://localhost:8080/clientapi/v1/users{?page,size,sort}

GET http://localhost:8080/clientapi/v1/profile/users



##CRUD :
  
GET http://localhost:8080/clientapi/v1/users/1

    Response Status : 200, 404


POST http://localhost:8080/clientapi/v1/users/

    Response Status : 201


PUT http://localhost:8080/clientapi/v1/users/25

    Response Status : 204


DELETE  http://localhost:8080/clientapi/v1/users/7

    Response Status : 204


##Sample JSON:  

```json
{
"name": "name__post_1",
  "email": "email_post1_1@server.com",
  "address1": "address_line_1_post1_1",
  "address2": "address_line_2__post1_1",
  "townCity": "town__post1_1",
  "postCode": 1001,
  "country": "country_post1_1",
  "telephoneList": [
    "phone_1_post1_1",
    "phone_2_post1_1",
    "phone_3_post1_1"
  ],
  "retrievalTimeStamp": "2017-11-05T14:50:45.344+0000"
}
```
