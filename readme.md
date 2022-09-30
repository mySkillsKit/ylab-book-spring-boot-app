1. собрать приложение mvn clean install
2. проверить работу приложения возможно по этому ендпоинту: http://localhost:8091/app/actuator
3. посмотреть сваггер возможно тут: http://localhost:8091/app/swagger-ui/index.html

```docker run --name mpl_ulab_db -d -p 5432:5432 -e POSTGRES_USER=test -e POSTGRES_PASSWORD=test postgres:14```

```CREATE DATABASE mpl_ulab_db;```


rqid requestId1010101
Post Request```http://localhost:8091/app/api/v1/user/create```
user id = 1
```
{
    "userRequest": 
        {   "fullName": "Tom Smith",
               "title": "reader", 
                 "age": 35 
         },
 
    "bookRequests": 
 [ 
        {      "title": "Anna Karenina", 
              "author": "Leo Tolstoy", 
           "pageCount": 432 }, 

        {      "title": "Moby Dick ", 
              "author": "Herman Melville", 
           "pageCount": 349 }, 
        {
               "title": "The Odyssey ",
              "author": "Homer",
           "pageCount": 755 } 
  ]
   }
```
200 OK -> 

```
{
    "userId": 100,
    "booksIdList": [
        200,
        201,
        202
    ]
} 
```

---> post Request user id = 2

```
{
    "userRequest": 
        {   "fullName": "John Adoms",
               "title": "reader", 
                 "age": 35 
         },
 
    "bookRequests": 
 [ 
        {      "title": "War and Peace", 
              "author": "Leo Tolstoy", 
           "pageCount": 1222 }, 

        {      "title": "Crime and Punishment", 
              "author": "Fyodor Dostoyevsky", 
           "pageCount": 289 }, 
        {
               "title": "The Odyssey ",
              "author": "Homer",
           "pageCount": 755 } 
  ]
   }
```


200 OK ->
```
{
    "userId": 101,
    "booksIdList": [
        203,
        204,
        205
    ]
}

```

Get Request ```http://localhost:8091/app/api/v1/user/get/101```

200 OK ->
```
{
    "userId": 101,
    "booksIdList": [
        203,
        204,
        205
    ]
}

```

Put Request  ```http://localhost:8091/app/api/v1/user/update```

```
{
    "userRequest": 
       {   "fullName": "John Adoms",
               "title": "reader", 
                 "age": 40 
         },
 
    "bookRequests": 
 [ 
        {      "title": "Anna Karenina", 
              "author": "Leo Tolstoy", 
           "pageCount": 432 }, 

        {      "title": "Moby Dick ", 
              "author": "Herman Melville", 
           "pageCount": 349 }, 
           
        {      "title": "War and Peace", 
              "author": "Leo Tolstoy", 
           "pageCount": 1222 }, 

         {      "title": "Crime and Punishment", 
              "author": "Fyodor Dostoyevsky", 
           "pageCount": 289 }
           
  ]
   }
```

200 OK ->

```
{
    "userId": 101,
    "booksIdList": [
        206,
        207,
        208,
        209
    ]
}

```

