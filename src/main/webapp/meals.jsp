<%--
  Created by IntelliJ IDEA.
  User: gosha
  Date: 10.06.2019
  Time: 19:54
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style type="text/css">
        .red-meals { color: crimson }
        .green-meals { color: darkgreen }
        .table {width: 600px;}
        .table td, th { padding: 10px; text-align: left;}
        body {padding: 100px}

    </style>
</head>
<body>

<h1> Питание по дням </h1>
<hr>
<table class="table">
    <thead>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>
    </thead>

    <tbody>

    <c:forEach var="meal" items="${list}">

        <c:set var="mealColor" value="${meal.getExcess() == true ? 'red-meals' : 'green-meals'}"/>

        <tr class="${mealColor}">
            <td>${meal.getDate()} ${meal.getTime()}</td>
            <td>${meal.getDescription()}</td>
            <td>${meal.getCalories()}</td>
            <td>
                <form action="meals" method="post">
                    <input name="deleteId" id="deleteId" value="${meal.getId()}" hidden >
                    <button>Удалить</button>
                </form>
            </td>
        </tr>

    </c:forEach>

    </tbody>
</table>
<hr>
<p> Установленно ограничений не более ${caloriesPerDay} калорий в день </p>
<p> Красным выделены превышения нормы в день </p>


</body>
</html>
