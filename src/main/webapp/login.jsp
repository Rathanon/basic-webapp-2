<%@ page contentType="text/html; ISO-8859-1" language="java"%>

<html>
<title>Home page</title>
<body>
<p>
    ${error}
</p>
<p>
<form action="/login" method="post">

    <input type="text" placeholder="Enter Username" name="username" required><br>
    <input type="password" placeholder="Enter Password" name="password" required><br>
    <button type="submit">Login</button>

</form>
</p>
</body>
</html>
