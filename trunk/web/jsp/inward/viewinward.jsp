<%--
    Document   : Create Inward
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="../templates/style.jsp" %>
        <link rel="stylesheet" type="text/css" href="../css/inward.css" />
        <title>View Inward</title>
        <script type="text/javascript" src="../js/viewinward.js"></script>
        <script>
            addOnloadEvent(makeReadOnly);
            addOnloadEvent(makeInwardReadOnly);
        </script>
    </head>
    <body>

        <%@include file="../templates/layout.jsp" %>
        <div id="bodyContainer">
            <form method="post" action="" name="inwardForm">
                <div class="MainDiv">
                    <fieldset class="MainFieldset">
                        <legend>View Inward</legend>
                        <%@include file="inward.jsp"%>
                        </fieldset>
                </div>
            </form>
        </div>
    </body>
</html>