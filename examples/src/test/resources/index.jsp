<%@ page language="java" contentType="text/html;charset=UTF-8" %>

<html>
<body>
<jsp:useBean id="greeterBean" class="arquillian.jsp.Greeter" scope="session"/>
Greeting: <%= greeterBean.sayHello() %>
</body>
</html>
