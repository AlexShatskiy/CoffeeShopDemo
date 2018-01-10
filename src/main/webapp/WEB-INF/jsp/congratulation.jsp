<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>congratulation</title>

    <link href="assets/css/bootstrap.css" rel="stylesheet">
    <link xmlns="http://www.w3.org/1999/xhtml" href='assets/fonts/glyphicons-halflings-regular.svg' rel='stylesheet' type='image/svg+xml' />
</head>
<body>
<header>
    <br>
    <br>
    <br>
    <c:if test="${empty sessionScope.congratulation}">
        <c:redirect url="controller?command=LIST_COFFEE"/>
    </c:if>
</header>
<div class="container">
    <div class="row">
        <div class="col-md-3 widget">

        </div>
        <div class="col-md-6 widget">

            <h2>Your order is now being prepared. Thank you for choosing us.</h2>
            <br>
            <br>
            <form method="get" action="controller">
                <input type="hidden" name="command" value="LIST_COFFEE" />
                <button type="submit" class="btn btn-info btn-block">
                    list of coffee
                </button>
            </form>
        </div>

        <div class="col-md-3 widget">

        </div>
    </div>
</div>

</body>
</html>
