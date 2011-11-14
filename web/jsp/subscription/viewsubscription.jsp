<%--
    Document   : View Subscription
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../templates/style.jsp"></jsp:include>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() + "/css/subscription.css"%>"/>
        <title>View Subscription</title>
        <script type="text/javascript" src="<%=request.getContextPath() + "js/common.js"%>"></script>
        <script type="text/javascript" src="<%=request.getContextPath() + "/js/subscription/viewsubscription.js"%>"></script>
        <script>
            addOnloadEvent(makeReadOnly);
            addOnloadEvent(makeViewSubscriptionReadOnly);
        </script>
    </head>
    <body>

        <%@include file="../templates/layout.jsp" %>
        <div id="bodyContainer">
            <form method="post" action="<%=request.getContextPath() + "/subscription"%>" name="subscriptionForm">
                <div class="MainDiv">
                    <fieldset class="MainFieldset">
                        <legend>View Subscription</legend>
                        <%@include file="subscription.jsp"%>
                    </fieldset>
                </div>
            </form>
        </div>
    </body>
</html>