<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>order</title>

    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <link xmlns="http://www.w3.org/1999/xhtml" href='assets/fonts/glyphicons-halflings-regular.svg' rel='stylesheet' type='image/svg+xml' />
</head>
<body>
<header>
    <br>
    <br>
    <br>
</header>
<div class="container">
    <div class="row">
        <div class="col-md-9 widget">
            <c:if test="${not empty sessionScope.order}">
                <ul class="list-group">
                    <form method="post" action="controller">
                    <c:forEach var="orderItems" items="${sessionScope.order.getItems()}" >
                        <li class="list-group-item">${coffee.getName()}
                            <p>${orderItems.getCoffee().getName()}</p>
                            <p>${orderItems.getQuantity()}</p>
                            <br>
                            <div class="row">
                                <div class="col-md-6 widget">
                                    <input type="number" name="${orderItems.getCoffee().getName()}" min="1" value="${orderItems.getQuantity()}">
                                </div>
                                <div class="col-md-6 widget">
                                    <a href="controller?command=DELETE_ORDER_ITEM&coffeeId=${orderItems.getCoffee().getId()}" class="btn btn-warning">delete</a>
                                </div>
                            </div>
                        </li>
                    </c:forEach>
                        <input type="hidden" name="command" value="CONFIRM_ORDER" />
                        <button type="submit" class="btn">
                            confirm order
                        </button>
                    </form>
                </ul>
            </c:if>
        </div>
        <div class="col-md-3 widget">
            <form method="get" action="controller">
                <input type="hidden" name="command" value="ORDER_PAGE" />
                <button type="submit" class="btn">
                    TODO
                </button>
            </form>
        </div>
    </div>
</div>


</body>
</html>
