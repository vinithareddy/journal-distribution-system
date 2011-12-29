
<%@page language="java" import="java.util.*" %>
<jsp:useBean class="IAS.Bean.subscriberFormBean" id="subscriberFormBean" scope="request"></jsp:useBean>
<script>
    $(document).ready(function() {
        jdsAppend("/JDS/CMasterData?md=city","city","city");
        jdsAppend("/JDS/CMasterData?md=country","country","country");
        jdsAppend("/JDS/CMasterData?md=state","state","state");
        jdsAppend("/JDS/CMasterData?md=district","district","district");
        jdsAppend("/JDS/CMasterData?md=sub_type","subtype","subtype");
    });
</script>
<fieldset class="subMainFieldSet">
    <legend>Subscriber</legend>

    <div class="IASFormLeftDiv">

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Subscriber Number:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASTextBox" READONLY TABINDEX="1" type="text" name="subscriberNumber" id="subscriberNumber" value="${subscriberFormBean.subscriberNumber}"/>
            </span>
        </div>


        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Subscriber Name:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASTextBoxMandatoryWide" TABINDEX="2" type="text" name="subscriberName" id="subscriberName" value="${subscriberFormBean.subscriberName}"/>
            </span>
        </div>


        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Address:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <textarea class="IASTextAreaMandatory" TABINDEX="3" name="subscriberAddress" id="subscriberAddress">${subscriberFormBean.subscriberAddress}</textarea>
            </span>
        </div>


        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>City:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <select class="IASComboBoxMandatory" TABINDEX="9" name="city" id="city">
                    <option value="0" selected >Select</option>
                    <%
                        if (!subscriberFormBean.getCity().isEmpty()) {
                            out.println("<option value=" + "\"" + subscriberFormBean.getCity() + "\"" + " selected >" + subscriberFormBean.getCity() + "</option>");
                        }
                    %>
                </select>
            </span>
        </div>

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>District:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <select class="IASComboBox" TABINDEX="9" name="district" id="district">
                    <option value="0" selected >Select</option>
                    <%
                        if (!subscriberFormBean.getDistrict().isEmpty()) {
                            out.println("<option value=" + "\"" + subscriberFormBean.getDistrict() + "\"" + " selected >" + subscriberFormBean.getDistrict() + "</option>");
                        }
                    %>

                </select>
            </span>
        </div>

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>State:</label>
            </span>
            <span class="IASFormDivSpanInputBox">
                <select class="IASComboBoxMandatory" TABINDEX="3" name="state" id="state">
                    <option value="0" selected >Select</option>
                    <%
                        if (!subscriberFormBean.getState().isEmpty()) {
                            out.println("<option value=" + "\"" + subscriberFormBean.getState() + "\"" + " selected >" + subscriberFormBean.getState() + "</option>");
                        }
                    %>
                </select>
                <%--<input class="IASTextBoxMandatory" TABINDEX="3" name="state" id="state" value="<jsp:getProperty name="inwardFormBean" property="state"/>"--%>
            </span>
        </div>

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Country:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <select class="IASComboBoxMandatory" TABINDEX="9" name="country" id="country">
                    <option value="0" selected >Select</option>
                    <%
                        if (!subscriberFormBean.getCountry().isEmpty()) {
                            out.println("<option value=" + "\"" + subscriberFormBean.getCountry() + "\"" + " selected >" + subscriberFormBean.getCountry() + "</option>");
                        }
                    %>
                </select>
            </span>
        </div>

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Pin Code:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASTextBox" TABINDEX="5" type="text" name="pincode" id="pincode" value="${subscriberFormBean.pincodeAsText}"/>
            </span>
        </div>


        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Subscriber Type:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <select class="IASComboBoxSmallMandatory" TABINDEX="9" name="subtype" id="subtype"
                        onchange="subtypeAppend()">
                    <option value="0" selected >Select</option>
                    <%
                        if (!subscriberFormBean.getSubtype().isEmpty()) {
                            out.println("<option value=" + "\"" + subscriberFormBean.getSubtype() + "\"" + " selected >" + subscriberFormBean.getSubtype() + "</option>");
                        }
                    %>
                </select>
                <select class="IASComboBoxMandatory" TABINDEX="9" name="subtypedesc" id="subtypedesc"
                        onchange="subtypeCodeAppend()">
                    <option value="0">Select</option>
                    <%
                        if (!subscriberFormBean.getSubtypedesc().isEmpty()) {
                            out.println("<option value=" + "\"" + subscriberFormBean.getSubtypedesc() + "\"" + " selected >" + subscriberFormBean.getSubtypedesc() + "</option>");
                        }
                    %>
                </select>
                <label id="subtypecode"></label>
            </span>
        </div>

    </div>

    <div class="IASFormRightDiv">

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Creation Date:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASDateTextBox" readonly type="text" name="subscriberCreationDate" id="subscriberCreationDate" value="${subscriberFormBean.subscriberCreationDate}"/>
            </span>
        </div>


        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Department:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASTextBox" TABINDEX="7" type="text" name="department" id="department" value="${subscriberFormBean.department}"/>
            </span>
        </div>

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Institution:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASTextBox" TABINDEX="7" type="text" name="institution" id="institution" value="${subscriberFormBean.institution}"/>
            </span>
        </div>


        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Email:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASEmailTextBox" TABINDEX="9" type="text" name="email" id="email" onblur="validateEmail(this.id)" value="${subscriberFormBean.email}"/>
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

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Deactivate</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input type="checkbox" class="IASCheckBox" disabled TABINDEX="11" name="isDeactived" id="isDeactived" onclick="deActivateSubscriber()"/>
            </span>
        </div>

        <div class="IASFormFieldDiv">
            <span class="IASFormDivSpanLabel">
                <label>Deactivation Date:</label>
            </span>

            <span class="IASFormDivSpanInputBox">
                <input class="IASDateTextBox" disabled readonly type="text" name="deactivationDate" id="deactivationDate" value=""/>
            </span>
        </div>

    </div>

</fieldset>

<%--Actions--%>

<fieldset class="subMainFieldSet">
    <div class="actionBtnDiv">
        <input onclick="setActionValue('edit')" class="IASButton" TABINDEX="100" type="submit" value="Edit Subscriber" id="btnEditSubscriber" name="btnSubmitAction"/>
        <input onclick="setActionValue('save')" class="IASButton" TABINDEX="101" type="submit" value="Save" id="btnSaveSubscriber" name="btnSubmitAction"/>
        <input onclick="setActionValue('display')" class="IASButton" TABINDEX="102" type="submit" value="View Subscriber" id="btnDisplaySubscriber" name="btnSubmitAction"/>
        <input onclick="setActionValue('view')" class="IASButton" TABINDEX="102" type="submit" value="View Subscription" id="btnViewSubscription" name="btnSubmitAction"/>
        <input onclick="setActionValue('add')" class="IASButton" TABINDEX="102" type="submit" value="Add Subscription" id="btnAddSubscription" name="btnSubmitAction"/>
    </div>
</fieldset>
