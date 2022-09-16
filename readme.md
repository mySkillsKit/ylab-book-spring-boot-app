1. собрать приложение mvn clean install
2. проверить работу приложения возможно по этому ендпоинту: http://localhost:8091/app/actuator
3. посмотреть сваггер возможно тут: http://localhost:8091/app/swagger-ui/index.html

http://localhost:8091/app/api/v1/user/create

{
"userRequest": {
"fullName": "test",
"title": "reader",
"age": 33
},
"bookRequests": [
{
"title": "book name",
"author": "test author",
"pageCount": 222
},
{
"title": "book name test",
"author": "test author second",
"pageCount": 555
}
]
}

rqid requestId1010101