<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 18.01.2017
  Time: 23:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>


<c:import url="parts/header.jsp"/>
<head>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.13/css/dataTables.bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="../resource/bootstrap/css/bootstrap.css"/>

    <link rel="stylesheet" type="text/css" href="../resource/bootstrap/css/font-awesome.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script type="text/javascript" charset="utf8"
            src="https://cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" charset="utf8"
            src="https://cdn.datatables.net/1.10.13/js/dataTables.bootstrap.min.js"></script>

</head>
<body>



<h1>role : ${role}</h1>
<style>
    #add-button {
        margin-bottom: 20px !important;
    }
    #sort-disabled:after{
        content: "" !important;
    }
    #sort-disabled{
        cursor: default !important;
    }
</style>

<div class="container">

    <c:if test="${sessionScope.role==0}">
        <a class="btn btn-default" href="/jsp/service-add.jsp" role="button" id="add-button">
            <i class="fa fa-plus" aria-hidden="true"></i>
            Add new tariff
        </a>
    </c:if>

    <table id="example" class="table table-striped table-bordered" cellspacing="0" width="100%">
        <thead>
        <tr>
            <th id="sort-disabled">Name</th>
            <th>Description</th>
            <th>Validity</th>
            <th onclick="sortCost(tbody, 3, 1)">Cost</th>
            <c:if test="${sessionScope.role!=null}">
                <th id="sort-disabled">Options</th>
            </c:if>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="service" items="${sessionScope.services}">
            <tr>
                <td>${service.getName()}</td>
                <td>${service.getDescription()}</td>
                <td>${service.getValidity()}</td>
                <td>${service.getCost()}</td>

                <c:if test="${sessionScope.role==0}">
                    <td>
                        <ul class="list-inline">
                            <li>
                                <a class="btn btn-default" onclick="return confirmDelete()"
                                   href="controller?command=delete_service&name=${service.getName()}">DELETE</a>
                            </li>
                        </ul>
                    </td>
                </c:if>
                <c:if test="${sessionScope.role==1}">
                    <td>
                        <ul class="list-inline">
                            <li>
                                <a class="btn btn-default" onclick="return confirmSign()"
                                   href="controller?command=sign_service&service_name=${service.getName()}">SIGN</a>
                            </li>
                        </ul>
                    </td>
                </c:if>

            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script>


    function confirmSign() {
       return confirm("Want to sign?");
    }


    function confirmDelete() {
        return confirm("Want to delete?");
    }

    $(document).ready(function () {
        $('#example').DataTable();
    });

    $('#example').DataTable({

        "aoColumnDefs": [
            {'bSortable': false, 'aTargets': [0]},
            {'bSortable': false, 'aTargets': [1]},
            {'bSortable': false, 'aTargets': [2]}
        ]


    });



</script>



</body>
<c:import url="parts/footer.jsp"/>

</html>
