<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 07.01.2017
  Time: 18:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Title</title>
</head>
<body>

<div class="modal fade" id="changePassword-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     style="display: none;">
    <div class="modal-dialog">

        <div class="modalWindow-container">

            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                    aria-hidden="true">&times;</span></button>

            <div class="alert alert-warning alert-dismissable" id="alert" hidden>
                <p>Something goes wrong</p>
            </div>

            <h1>Login to Your Account</h1><br>
            <form action="${pageContext.request.contextPath}/controller" method="post">
                <input name="command" type="hidden" value="change_password">
                <input type="password" name="currentPassword" placeholder="Current password">
                <input type="password" name="newPassword" placeholder="New password">
                <input type="password" name="confirm" placeholder="Confirm new password">
                <input type="submit" class="modalWindow-submit" value="Change">
            </form>
        </div>
    </div>
</div>

<div id="backstage" class="modal-backdrop fade in" hidden></div>

<c:choose>
    <c:when test="${!sessionScope.done && sessionScope.done!=null}">
        <script type="text/javascript">

            $(window).on('load', function(){
                $('#login-modal').modal('show');
            });

            document.getElementById("alert").hidden=false;

            $(".close").click(function(){

                document.getElementById("alert").hidden=true;
            });

        </script>
    </c:when>
</c:choose>

</body>
</html>
