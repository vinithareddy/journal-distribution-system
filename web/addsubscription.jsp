<%--
    Document   : searchinward
    Created on : Oct 22, 2011, 6:28:28 PM
    Author     : Shailendra Mahapatra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="templates/style.jsp"></jsp:include>
        <link rel="stylesheet" type="text/css" href="css/inward.css" />
        <script src="js/CalendarPopup.js" type="text/javascript"></script>
        <script>
            var calFrom = new CalendarPopup("dateDivFrom");
            calFrom.showNavigationDropdowns();
            var calTo = new CalendarPopup("dateDivTo");
            calTo.showNavigationDropdowns();
        </script>
        <title>New Subscriber and Subscription</title>
    </head>
    <body>
        <jsp:include page="templates/header.jsp"></jsp:include>
        <jsp:include page="templates/sidebar.jsp"></jsp:include>
        <div id="bodyContainer">
            <div class="MainDiv">
                <fieldset class="MainFieldset">
                    <fieldset class="subMainFieldSet">
                        <legend>Add Subscriber</legend>
                        <div class="IASFormRow">
                            <span class="IASFormCell">
                                <label>Subscriber ID:</label>
                                <input class="IASTextBox" type="text" name="subscriberID" id="subscriberID" value=""></input>
                            </span>
                            <span class="IASFormCell">
                                <label>Creation Date:</label>
                                <input class="IASTextBox" type="text" name="subscriberCreationDate" id="subscriberCreationDate" value=""></input>
                            </span>

                        </div>
                        <div class="IASFormRow">
                            <label>Subscriber Name:</label>
                            <input type="text" name="subscriberName" id="subscriberName" value=""></input>
                            <label>Department</label>
                            <input type="text" name="subscriberDepartment" id="subscriberDepartment" value=""></input>
                        </div>
                    </fieldset>
                    <fieldset class="subMainFieldSet">
                        <legend>Add Subscription</legend>
                    </fieldset>


                </fieldset>
            </div>
        </div>
    </body>
</html>