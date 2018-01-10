<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>admin</title>

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
            <c:if test="${not empty requestScope.listNewOrder}">
                <h3>New order:</h3>
                <br>
                <ul class="list-group">
                    <c:forEach var="order" items="${requestScope.listNewOrder}" >
                        <li class="list-group-item">
                            <div class="row">
                                <div class="col-md-6 widget">
                                    <p>Name: ${order.getCustomername()}</p>
                                    <p>Address: ${order.getCustomeraddress()}</p>
                                    <p>Phone: ${order.getPhone()}</p>
                                    <p>Price: ${order.getPrice()} $</p>
                                </div>
                                <div class="col-md-6 widget">
                                    <p>Order: </p>
                                    <br>
                                    <ul class="list-group">
                                        <c:forEach var="orderItem" items="${order.getItems()}" >
                                            <li class="list-group-item">
                                                <p>Coffee: ${orderItem.getCoffee().getName()}</p>
                                                <p>Quantity: ${orderItem.getQuantity()}</p>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                            <br>
                            <div class="row">
                                <form method="post" action="admin">
                                    <input type="hidden" name="command" value="DELIVERED_ORDER" />
                                    <input type="hidden" name="orderId" value="${order.getId()}" />

                                    <button type="submit" class="btn">
                                        Delivered
                                    </button>
                                </form>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
        <div class="col-md-3 widget">
            <form method="post" action="admin">
                <input type="hidden" name="command" value="LOGOUT" />
                <button type="submit" class="btn btn-danger">
                    <span class="glyphicon glyphicon-log-out">  Logout</span>
                </button>
            </form>
            <br>
            <br>
            <c:if test="${not empty requestScope.listOldOrder}">
                <h3>Old order:</h3>
                <br>
                <ul class="list-group">
                    <c:forEach var="order" items="${requestScope.listOldOrder}" >
                        <li class="list-group-item">
                            <p>Address: ${order.getCustomeraddress()}</p>
                            <p>Price: ${order.getPrice()} $</p>
                            <br>
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
        </div>
    </div>
</div>


</body>
</html>
