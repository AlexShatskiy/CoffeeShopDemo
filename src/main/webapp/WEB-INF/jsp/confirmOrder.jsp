<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>confirmOrder</title>

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
        <div class="col-md-6 widget">
            <form method="post" action="controller">
                <input type="hidden" name="command" value="EXECUTE_ORDER" />
                Address:
                <input type="text" class="form-control"  name="address" required/>
                <br>
                Name:
                <input type="text" class="form-control"  name="name" required/>
                <br>
                Phone (in the format 8XXXXXXXXXX):
                <input type="tel" class="form-control"  name="phone" pattern="8[0-9]{10}"  required/>
                <br>
                <button type="submit" class="btn btn-success">
                    Order
                </button>
                <a href="controller?command=ORDER_PAGE" class="btn btn-warning">change order</a>
            </form>
        </div>
        <div class="col-md-6 widget">
            <h3>Your order:</h3>
            <br>
            <ul class="list-group">
                <c:forEach var="orderItems" items="${sessionScope.order.getItems()}" >
                    <li class="list-group-item">${coffee.getName()}
                        <h3>${orderItems.getCoffee().getName()}</h3>
                        <p>Quantity: ${orderItems.getQuantity()}</p>
                    </li>
                </c:forEach>
                <br>
                <p>Price: ${sessionScope.order.getPrice()} $</p>
            </ul>
        </div>
    </div>
</div>


</body>
</html>
