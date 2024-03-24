<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error Handling</title>
    <script>
        window.onload = function() {
         var errorMessage = "<%= request.getAttribute("errorMessage").toString() %>";
            alert(errorMessage);
            window.history.back(); // 사용자를 이전 페이지로 이동
        };
    </script>
</head>
<body>
</body>
</html>