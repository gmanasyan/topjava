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

    <c:forEach var="num" items="${list}">

        <c:choose>
            <c:when test="${num.getExcess() == true}">
                <c:set var="mealColor" value="red-meals"/>
            </c:when>
            <c:otherwise>
                <c:set var="mealColor" value="green-meals"/>
            </c:otherwise>
        </c:choose>

        <tr class="${mealColor}">
            <td>${num.getDate()} ${num.getTime()}</td>
            <td>${num.getDescription()}</td>
            <td>${num.getCalories()}</td>
            <td>
                <form action="meals" method="post">
                    <input name="delete-id" id="delete-id" value="${num.getId()}" hidden >
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

<!--p> Удалить  ${delete-id}  </p-->

</body>
</html>
