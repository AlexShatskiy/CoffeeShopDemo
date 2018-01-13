<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>coffeeList</title>

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link xmlns="http://www.w3.org/1999/xhtml" href='assets/fonts/glyphicons-halflings-regular.svg' rel='stylesheet' type='image/svg+xml' />
</head>
<body>
<div class="container">
    <br>
    <br>
    <br>
</div>
<div class="container">
    <div class="row">
        <div class="col-sm-9 widget">
            <c:if test="${not empty requestScope.listCoffee}">
                <ul class="list-group">
                    <c:forEach var="coffee" items="${requestScope.listCoffee}" >
                        <li class="list-group-item">
                            <h3>${coffee.getName()}</h3>
                            <p>${coffee.getDescription()}</p>
                            <p>Price: ${coffee.getPrice()} $</p>
                            <br>
                            <div class="row">
                                <form method="post" action="addOrderItem">
                                    <div class="col-sm-6 widget">
                                        Quantity:
                                        <input type="number" name="quantity" min="1" max="100" required>
                                    </div>
                                    <div class="col-sm-6 widget">
                                        <input type="hidden" name="coffeeId" value="${coffee.getId()}" />
                                        <button type="submit" class="btn btn-primary">
                                            add coffee
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
        <div class="col-sm-3 widget">
            <form action='<spring:url value="signin"/>' method="post">
                <input type="text" class="form-control" name="userid" placeholder="login" required/>
                <br>
                <input type="password" class="form-control"  name="passwd" placeholder="password" required/>
                <br>
                <button type="submit" class="btn btn-info">
                    <span class="glyphicon glyphicon-log-in"> Login</span>
                </button>
            </form>
            <c:if test="${not empty sessionScope.message}">
                <br>
                <div class="alert alert-warning">
                    <span style="color:green"><c:out value="${sessionScope.message}"/></span>
                    <c:remove var="message" scope="session" />
                </div>
            </c:if>
            <br>
            <hr>
            <c:if test="${not empty sessionScope.order}">
                <form method="get" action="orderPage">
                    <button type="submit" class="btn btn-success btn-lg btn-block">
                        <span class="glyphicon glyphicon-shopping-cart">  Order basket</span>
                    </button>
                </form>
            </c:if>
        </div>
    </div>
</div>


</body>
</html>
