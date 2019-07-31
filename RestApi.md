# Rest API
- For localhost server path is SERVER_PATH = http://localhost:8080/topjava

### Get all meals
- You can get all meals by GET SERVER_PATH/rest/meals path 

Request:
 <pre>curl "SERVER_PATH/rest/meals/"</pre>
 
 Response example:
 <pre>
    [{
      "id": 100007,
      "dateTime": "2015-05-31T20:00:00",
      "description": "Ужин",
      "calories": 510,
      "excess": true
   },
      {
      "id": 100006,
      "dateTime": "2015-05-31T13:00:00",
      "description": "Обед",
      "calories": 1000,
      "excess": true
   },
      {
      "id": 100005,
      "dateTime": "2015-05-31T10:00:00",
      "description": "Завтрак",
      "calories": 500,
      "excess": true
   }]
   </pre>

### Get one meal
- You can get one meal by his id. For instance: GET SERVER_PATH/rest/meals/{mealId} 
 <pre>curl "SERVER_PATH/rest/meals/{mealId}"</pre>

### Delete meal
- You can delete meal by id. Use: DELETE SERVER_PATH/rest/meals/{mealId} 
 <pre>curl -X DELETE "SERVER_PATH/rest/meals/{mealId}"</pre>

### Update meal 
- Update meal with {id} form filename. Use: PUT SERVER_PATH/rest/meals/{mealId} 
 <pre>curl -T filename "SERVER_PATH/rest/meals/{mealId}"</pre>
 
 ### Create meal 
 - Create meal from filename. Use: POST SERVER_PATH/rest/meals/ 
  <pre>curl -d filename "SERVER_PATH/rest/meals/{mealId}"</pre>

 ### Filter meal 
 - Filter by startDate, startTime, endDate, endTime. Use: GET SERVER_PATH/rest/meals/filter?startDate=2015-05-31 
  <pre>curl "SERVER_PATH/rest/meals/filter?startDate=2015-05-31"</pre>

