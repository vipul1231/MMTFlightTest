
**Flight application**

Basic curl request:
````
IXC -> COK

curl -X GET \
'http://localhost:8080/flights/get-route?startAirport=IXC&endAirport=COK' \
-H 'cache-control: no-cache' \
-H 'postman-token: 8e36a68a-6d89-6b4f-1893-2efe8ae467fc'

````


````
ATQ -> BLR

curl -X GET \
'http://localhost:8080/flights/get-route?startAirport=ATQ&endAirport=BLR' \
-H 'cache-control: no-cache' \
-H 'postman-token: 1c406865-194d-9d08-1323-689a193c16c1'