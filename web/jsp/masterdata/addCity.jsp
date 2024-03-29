<%--
    Document   : Add City
    Author     : Deepali
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../templates/style.jsp"></jsp:include>
        <link rel="stylesheet" type="text/css" href="css/masterdata/city.css"/>
        <title>Add City</title>
        <script type="text/javascript" src="js/masterdata/addCity.js"></script>
        <script type="text/javascript" src="js/masterdata/validateCity.js"></script>
        <script>
            $(document).ready(function(){
                makeCreateReadOnly();
            });
        </script>
    </head>
    <body>

        <%@include file="../templates/layout.jsp" %>
        <div id="bodyContainer">
            <form method="post" action="<%=request.getContextPath() + "/city"%>" name="cityForm" onsubmit="return validateCity()">
                <div class="MainDiv">
                    <fieldset class="MainFieldset">
                        <legend>Add City</legend>
                        <%@include file="city.jsp"%>
                    </fieldset>
                </div>
            </form>
        </div>
    </body>
</html>