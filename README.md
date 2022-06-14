## Demand
### Demand Assign Ip
request
```http request
POST /api/v1/demand/assign
-H 'X-Authorization-Id: 34'
-d '{
    "title": "",
    "description": "",
    "usage": "",
    "expireAt": ""
    }'
```
```json5
{
  "title": "",
  "description": "",
  "usage": "",
  "expireAt": "",
  "loginToken": ""
}
```
response
```json5
{}
```
### Cancel AssignIp Demand
request
```http request
DELETE /api/v1/demand/assign
-H 'X-Authorization-Id: 34'
-d '{ "demandId": 34 }'
```
response
```json5
{}
```