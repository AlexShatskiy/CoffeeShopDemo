<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>coffeeList</title>

    <link href="assets/css/bootstrap.css" rel="stylesheet">
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
                                <form method="post" action="controller">
                                    <div class="col-sm-6 widget">
                                        Quantity:
                                        <input type="number" name="quantity" min="1" max="100" required>
                                    </div>
                                    <div class="col-sm-6 widget">
                                        <input type="hidden" name="command" value="ADD_ORDERITEM" />
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
            <form method="post" action="admin">
                <input type="hidden"  name="command" value="LOGIN" />
                <input type="text" class="form-control" name="login" placeholder="login" required/>
                <br>
                <input type="password" class="form-control" name="password" placeholder="password" required/>
                <br>
                <button type="submit" class="btn btn-info">
                    <span class="glyphicon glyphicon-log-in"> Login</span>
                </button>
            </form>
            <br>
            <hr>
            <c:if test="${not empty sessionScope.order}">
                <form method="get" action="controller">
                    <input type="hidden" name="command" value="ORDER_PAGE" />
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
