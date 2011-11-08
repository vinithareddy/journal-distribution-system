<%--
    Document   : Subscriber and Subscription
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="templates/style.jsp"></jsp:include>
        <link rel="stylesheet" type="text/css" href="css/subscriber_subscription.css" />

        <title>Subscriber and Subscription</title>
    </head>
    <body>
        <%@include file="templates/layout.jsp" %>
        <div id="bodyContainer">
            <div class="MainDiv">
                <fieldset class="MainFieldset">
                    <legend>Subscriber Info</legend>
                    <fieldset class="subMainFieldSet">
                        <legend>Subscriber</legend>

                        <div class="IASFormLeftDiv">

                            <div class="IASFormFieldDiv">
                                <span class="IASFormDivSpanLabel">
                                    <label>Subscriber ID:</label>
                                </span>

                                <span class="IASFormDivSpanInputBox">
                                    <input class="IASTextBox" TABINDEX="1" type="text" name="subscriberID" id="subscriberID" value=""/>
                                </span>
                            </div>


                            <div class="IASFormFieldDiv">
                                <span class="IASFormDivSpanLabel">
                                    <label>Subscriber Name:</label>
                                </span>

                                <span class="IASFormDivSpanInputBox">
                                    <input class="IASTextBox" TABINDEX="2" type="text" name="subscriberName" id="subscriberName" value=""/>
                                </span>
                            </div>


                            <div class="IASFormFieldDiv">
                                <span class="IASFormDivSpanLabel">
                                    <label>Address:</label>
                                </span>

                                <span class="IASFormDivSpanInputBox">
                                    <textarea class="IASTextArea" TABINDEX="3" name="subscriberAddress" id="subscriberAddress"></textarea>
                                </span>
                            </div>


                            <div class="IASFormFieldDiv">
                                <span class="IASFormDivSpanLabel">
                                    <label>City:</label>
                                </span>

                                <span class="IASFormDivSpanInputBox">
                                    <input class="IASTextBox" TABINDEX="4" type="text" name="subscriberCity" id="subscriberCity" value=""/>
                                </span>
                            </div>


                            <div class="IASFormFieldDiv">
                                <span class="IASFormDivSpanLabel">
                                    <label>Pin Code:</label>
                                </span>

                                <span class="IASFormDivSpanInputBox">
                                    <input class="IASTextBox" TABINDEX="5" type="text" name="subscriberPinCode" id="subscriberPinCode" value=""/>
                                </span>
                            </div>


                            <div class="IASFormFieldDiv">
                                <span class="IASFormDivSpanLabel">
                                    <label>Subscriber Type:</label>
                                </span>

                                <span class="IASFormDivSpanInputBox">
                                    <select class="IASComboBox" TABINDEX="6" name="subscriberType" id="subscriberType">
                                        <option value ="test1">Free Subscriber</option>
                                    </select>
                                </span>
                            </div>

                        </div>

                        <div class="IASFormRightDiv">

                            <div class="IASFormFieldDiv">
                                <span class="IASFormDivSpanLabel">
                                    <label>Creation Date:</label>
                                </span>

                                <span class="IASFormDivSpanInputBox">
                                    <input class="IASDateTextBox" readonly type="text" name="creationDate" id="creationDate" value=""/>
                                </span>
                            </div>


                            <div class="IASFormFieldDiv">
                                <span class="IASFormDivSpanLabel">
                                    <label>Department:</label>
                                </span>

                                <span class="IASFormDivSpanInputBox">
                                    <input class="IASTextBox" TABINDEX="7" type="text" name="department" id="department" value=""/>
                                </span>
                            </div>


                            <div class="IASFormFieldDiv">
                                <span class="IASFormDivSpanLabel">
                                    <label>Country:</label>
                                </span>

                                <span class="IASFormDivSpanInputBox">
                                    <select class="IASComboBox" TABINDEX="8" name="subscriberCountry" id="subscriberCountry">
                                        <option value ="India">India</option>
                                    </select>
                                </span>
                            </div>


                            <div class="IASFormFieldDiv">
                                <span class="IASFormDivSpanLabel">
                                    <label>Email:</label>
                                </span>

                                <span class="IASFormDivSpanInputBox">
                                    <input class="IASEmailTextBox" TABINDEX="9" type="text" name="subscriberEmail" id="subscriberEmail" value=""/>
                                </span>
                            </div>


                            <div class="IASFormFieldDiv">
                                <span class="IASFormDivSpanLabel">
                                    <label>Agent:</label>
                                </span>

                                <span class="IASFormDivSpanInputBox">
                                    <select class="IASComboBox" TABINDEX="10" name="subscriberAgent" id="subscriberAgent">
                                        <option value ="IASAgent">IASAgent</option>
                                    </select>
                                </span>
                            </div>

                        </div>

                    </fieldset>

                    <fieldset class="subMainFieldSet">
                        <legend>Subscription Details</legend>

                        <div class="IASFormLeftDiv">
                            <div class="IASFormFieldDiv">
                                <span class="IASFormDivSpanLabel">
                                    <label>Start Year:</label>
                                </span>

                                <span class="IASFormDivSpanInputBox">
                                    <select class="IASComboBox" TABINDEX="11" name="subscriptionStartYear" id="subscriptionStartYear">
                                        <option value ="IASAgent">2007</option>
                                        <option value ="IASAgent">2008</option>
                                    </select>
                                </span>
                            </div>

                            <div class="IASFormFieldDiv">
                                <span class="IASFormDivSpanLabel">
                                    <label>Remarks</label>
                                </span>

                                <span class="IASFormDivSpanInputBox">
                                    <textarea class="IASTextArea" TABINDEX="12" name="subscriberRemarks" id="subscriberRemarks"></textarea>
                                </span>
                            </div>
                        </div>

                        <div class="IASFormRightDiv">

                            <div class="IASFormFieldDiv">
                                <span class="IASFormDivSpanLabel">
                                    <label>Refund(If any):</label>
                                </span>

                                <span class="IASFormDivSpanInputBox">
                                    <input class="IASTextBox" TABINDEX="13" type="text" name="subscriptionRefund" id="subscriptionRefund" value=""/>
                                </span>

                                <span class="IASFormDivSpanLabel">
                                    <input class="IASButton" TABINDEX="14" type="submit" value="Calculate Refund"/>
                                </span>
                            </div>
                        </div>

                        <table class="datatable" TABINDEX="15">
                            <thead>
                                <tr>
                                    <td>Select</td>
                                    <td>Journal Code</td>
                                    <td>Journal Name</td>
                                    <td>Number Of Copies</td>
                                    <td>Number Of Year</td>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><input type="checkbox" name="Select Journal" value=" "/></td>
                                    <td>1</td>
                                    <td>Pramana1</td>
                                    <td>2</td>
                                    <td>3</td>
                                </tr>
                                <tr>
                                    <td><input type="checkbox" name="Select Journal" value=" "/></td>
                                    <td>1</td>
                                    <td>Pramana2</td>
                                    <td>2</td>
                                    <td>1</td>
                                </tr>
                            </tbody>
                        </table>
                    </fieldset>


                    <%--Actions--%>

                    <fieldset class="subMainFieldSet">
                        <div class="IASFormFieldDiv">
                            <div id="backToInwardBtnDiv">
                                <input class="IASButton" TABINDEX="100" type="submit" value="Back to Inward List" name="btnBack"/>
                            </div>
                            <div id="saveBtnDiv">
                                <input class="IASButton" TABINDEX="101" type="submit" value="Save" name="btnSave"/>
                            </div>
                            <div id="processNextInwardBtnDiv">
                                <input class="IASButton" TABINDEX="102" type="submit" value="Process Next Inward" name="btnProcessInward"/>
                            </div>
                        </div>
                    </fieldset>
                </fieldset>
            </div>
        </div>
    </body>
</html>