<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MedReview</title>
    <link rel="stylesheet" href="resources/css/bootstrap.css">
    <link rel="stylesheet" href="resources/css/index.css">
    <script src="resources/javascript/jquery-3.1.1.min.js"></script>
    <script src="resources/javascript/bootstrap.min.js"></script>
</head>
<body>
<div id="login-container" class="container">
    <h3 class="text-center" style="margin-bottom: 30px;">MedReview</h3>
    <hr>
    <form method="POST" action="/user/login">
        <div class="form-group">
            <input placeholder="Login" class="form-control" type="text">
        </div>
        <div class="form-group">
            <input placeholder="Password" class="form-control" type="password">
        </div>
        <div class="form-group text-right">
            <a href="/main.jsp">Forgot password?</a>
        </div>
        <button id="login" class="btn btn-primary center-block" type="submit">Login</button>
    </form>
</div>
</body>
</html>