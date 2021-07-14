<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<html>
<body>
<h2>welcome ${username}</h2>
${users}
//let get jstl to work
    <c:forEach var="user" items="${users}" >
        Item <c:out value = "${user.username}"/><p>
    </c:forEach>
</body>
</html>
