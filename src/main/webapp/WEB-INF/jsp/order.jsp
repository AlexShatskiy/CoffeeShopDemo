<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>order</title>

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link xmlns="http://www.w3.org/1999/xhtml" href='assets/fonts/glyphicons-halflings-regular.svg' rel='stylesheet' type='image/svg+xml' />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){

            displayPrice();

            //checks change quantity coffee
            $(document).on("change", ".quantity", function(){

                displayPrice();
            });

            //display coffee price
            function displayPrice(){
                var result = "";

                var quantityArray = $( ".quantity" ).toArray().reverse();
                var priceArray  = $( ".price" ).toArray().reverse();

                for ( var i = 0; i < quantityArray.length; i++ ) {
                    result = +result + quantityArray[i].value * priceArray[i].value;
                }
                if (result == 0){
                    return false;
                }

                $("#priceId").text(result);
            }

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
                    <form method="post" action="confirmOrder">
                        <c:forEach var="orderItems" items="${sessionScope.order.getItems()}" >
                            <li class="list-group-item">
                                <h3>${orderItems.getCoffee().getName()}</h3>
                                <br>
                                <div class="row">
                                    <div class="col-md-6 widget">
                                        Quantity:
                                        <input  class="quantity" type="number" name="${orderItems.getCoffee().getName()}" min="1" max="1000" value="${orderItems.getQuantity()}" required>
                                        <input type="hidden" class="price" value="${orderItems.getCoffee().getPrice()}" />
                                    </div>
                                    <div class="col-md-6 widget">
                                        <a href="deleteOrderItem?coffeeId=${orderItems.getCoffee().getId()}" class="btn btn-danger">delete</a>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                        <li class="list-group-item">
                            <p>Price (from server): ${sessionScope.order.getPrice()} $</p>
                            <p>Price (from js): <span id="priceId"></span> $</p>
                        </li>
                        <br>
                        <br>

                        <input type="submit" class="btn btn-success" value="confirm order" name="confirm">
                        <input type="submit" class="btn btn-info" value="recount order" name="recount">

                    </form>
                </ul>
            </c:if>
        </div>
        <div class="col-md-3 widget">
            <form method="get" action="coffeeList">
                <button type="submit" class="btn btn-info btn-block">
                    list of coffee
                </button>
            </form>
        </div>
    </div>
</div>


</body>
</html>
