<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>order</title>

    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <link xmlns="http://www.w3.org/1999/xhtml" href='assets/fonts/glyphicons-halflings-regular.svg' rel='stylesheet' type='image/svg+xml' />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){

            //checks value of quantity coffee
            $(document).on("click", "#confirm", function(){

                var d = document,
                inp = d.getElementsByClassName('quantity'),
                mas = [];

                for (var i = 0; i < inp.length; i++) {
                    mas[i] = inp[i].value;
                    if (mas[i] == ""){
                        alert("Field quantity can't be empty");
                        return false;
                    }
                }
            });
        });
    </script>
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
                            <li class="list-group-item">
                                <h3>${orderItems.getCoffee().getName()}</h3>
                                <br>
                                <div class="row">
                                    <div class="col-md-6 widget">
                                        Quantity:
                                        <input  class="quantity" type="number" name="${orderItems.getCoffee().getName()}" min="1" max="1000" value="${orderItems.getQuantity()}">
                                    </div>
                                    <div class="col-md-6 widget">
                                        <a href="controller?command=DELETE_ORDER_ITEM&coffeeId=${orderItems.getCoffee().getId()}" class="btn btn-danger">delete</a>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                        <input type="hidden" name="command" value="CONFIRM_ORDER" />
                        <br>
                        <br>
                        <button type="submit" id="confirm" class="btn btn-success">
                            confirm order
                        </button>
                    </form>
                </ul>
            </c:if>
        </div>
        <div class="col-md-3 widget">
            <form method="get" action="controller">
                <input type="hidden" name="command" value="LIST_COFFEE" />
                <button type="submit" class="btn btn-info btn-block">
                    list of coffee
                </button>
            </form>
        </div>
    </div>
</div>


</body>
</html>
