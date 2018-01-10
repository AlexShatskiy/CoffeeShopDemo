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
<header>
    <br>
    <br>
    <br>
</header>
<div class="container">
    <div class="row">
        <div class="col-md-9 widget">
            <c:if test="${not empty requestScope.listCoffee}">
                <ul class="list-group">
                <c:forEach var="coffee" items="${requestScope.listCoffee}" >
                    <li class="list-group-item">${coffee.getName()}
                        <p>${coffee.getDescription()}</p>
                        <p>${coffee.getPrice()}</p>
                        <br>
                        <div class="row">
                            <form method="post" action="controller">
                                <div class="col-md-6 widget">
                                    <input type="number" name="quantity" min="1" max="20" required>
                                </div>
                                <div class="col-md-6 widget">
                                    <input type="hidden" name="command" value="ADD_ORDERITEM" />
                                    <input type="hidden" name="coffeeId" value="${coffee.getId()}" />
                                    <input type="hidden" name="coffeeName" value="${coffee.getName()}" />
                                    <button type="submit" class="btn">
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
        <div class="col-md-3 widget">
            <c:if test="${not empty sessionScope.order}">
                <form method="get" action="controller">
                    <input type="hidden" name="command" value="ORDER_PAGE" />
                    <button type="submit" class="btn">
                        order
                    </button>
                </form>
            </c:if>
        </div>
    </div>
</div>


</body>
</html>
