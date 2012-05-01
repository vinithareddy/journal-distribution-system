<%--
    Document   : Search Subscriber
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="../templates/style.jsp" %>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() + "/css/subscriber.css"%>"/>
        <script type="text/javascript" src="<%=request.getContextPath() + "/js/subscriber/searchsubscriber.js"%>"></script>
        <title>Search Subscriber</title>
        <script type="text/javascript">
            $(document).ready(function() {
                jdsAppend("<%=request.getContextPath() + "/CMasterData?md=country"%>","country","country");
                jdsAppend("<%=request.getContextPath() + "/CMasterData?md=state"%>","state","state");
                jdsAppend("<%=request.getContextPath() + "/CMasterData?md=city"%>","city","city");
                jdsAppend("<%=request.getContextPath() + "/CMasterData?md=journalGroupName"%>","journalGroupName","journalGroupName");
                jdsAppend("<%=request.getContextPath() + "/CMasterData?md=subscriberType"%>","subscriberType","subscriberType");
            });
        </script>
        <script type="text/javascript">
            //initally set to false, after the first search the flag is set to true
            var isPageLoaded = false;
            $(document).ready(function (){
                $(function(){
                        $("#subscriberTable").jqGrid({
                        url:"<%=request.getContextPath() + "/subscriber?action=listSubscribers"%>",
                        datatype: 'xml',
                        mtype: 'GET',
                        width: '100%',
                        height: 250,
                        autowidth: true,
                        forceFit: true,
                        sortable: true,
                        loadonce: true,
                        rownumbers: true,
                        emptyrecords: "No subscribers to view",
                        loadtext: "Loading...",
                        colNames:['Subscriber Number','Subscriber Name', 'Department','City','Pin Code','Country'],
                        colModel :[
                            {name:'Subscriber Number', index:'subscriberNumber', width:40, align:'center', xmlmap:'subscriberNumber'},
                            {name:'Subscriber Name', index:'subscriberName', width:40, align:'center', xmlmap:'subscriberName'},
                            {name:'Department', index:'department', width:40, align:'center', xmlmap:'department'},
                            {name:'City', index:'city', width:30, align:'center', sortable: true, sorttype: 'int',xmlmap:'city'},
                            {name:'Pin Code', index:'pincode', width:30, align:'center', sortable:false, xmlmap:'pincode'},
                            {name:'Country', index:'country', width:30, align:'center', xmlmap:'country'},
                          ],
                        xmlReader : {
                            root: "results",
                            row: "row",
                            page: "results>page",
                            total: "results>total",
                            records : "results>records",
                            repeatitems: false,
                            id: "subscriberNumber"
                        },
                        pager: '#pager',
                        pginput: true,
                        rowNum:10,
                        rowList:[10,20,30],
                        viewrecords: true,
                        gridview: true,
                        caption: '&nbsp;',

                        gridComplete: function() {
                            var ids = jQuery("#subscriberTable").jqGrid('getDataIDs');
                            for (var i = 0; i < ids.length; i++) {
                                var subscriberId = ids[i];
                                action = "<a style='color:blue;' href='subscriber?action=display&subscriberNumber=" + subscriberId + "'>View</a><a style='color:blue;' href='subscriber?action=edit&subscriberNumber=" + subscriberId + "'>Edit</a>";
                                jQuery("#subscriberTable").jqGrid('setRowData', ids[i], { Action: action });
                            }
                        },
                        beforeRequest: function(){
                            return isPageLoaded;
                        },
                        loadError: function(xhr,status,error){
                            alert("Failed getting data from server " + status);
                        }
                    });
                });
            });



            // called when the search button is clicked
            function searchSubscriber(){
                jQuery("#subscriberTable").setGridParam({ datatype: "xml" });
                if(validateSearchSubscriber() == true){
                    isPageLoaded = true;
                    jQuery("#subscriberTable").setGridParam({postData:
                            {city               : $("#city").val(),
                            country               : $("#country").val(),
                            state               : $("#state").val(),
                            journalGroupName    : $("#journalGroupName").val(),
                            subtype             : $("#subtype").val(),
                            nationality         : $("#nationality").val(),
                            institutional       : $("#institutional").val(),
                            subscriberType      : $("#subscriberType").val(),
                            from                : $("#from").val(),
                            to                  : $("#to").val()
                        }});
                    jQuery("#subscriberTable").trigger("clearGridData");
                    jQuery("#subscriberTable").trigger("reloadGrid");
                }

            }
            
            // draw the date picker.
            jQueryDatePicker("from","to");
        </script>
    </head>
    <body>
        <%@include file="../templates/layout.jsp" %>

        <div id="bodyContainer">
            <form method="post" action="" name="searchSubscriberForm">
                <div class="MainDiv">
                    <fieldset class="MainFieldset">
                        <legend>Search Subscriber</legend>

                        <%-----------------------------------------------------------------------------------------------------%>
                        <%-- Search Criteria Field Set --%>
                        <%-----------------------------------------------------------------------------------------------------%>
                        <fieldset class="subMainFieldSet">
                            <legend>Search Criteria</legend>

                            <%-- Search Criteria left div --%>
                            <div class="IASFormLeftDiv">


                                <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>Free/ Paid</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">

                                        <select class="IASComboBoxMandatory" TABINDEX="4" name="subtype" id="subtype">
                                            <option value ="">Select</option>
                                            <option value ="Paid">Paid</option>
                                            <option value ="Free">Free</option>
                                        </select>
                                    </span>
                                </div>

                                <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>Nationality</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <select class="IASComboBoxMandatory" TABINDEX="5" name="nationality" id="nationality">
                                            <option value ="">Select</option>
                                            <option value ="I">Indian</option>
                                            <option value ="F">Foreign</option>
                                        </select>
                                    </span>
                                </div>

                                <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>Institutional</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <select class="IASComboBoxMandatory" TABINDEX="6" name="institutional" id="institutional">
                                            <option value ="">Select</option>
                                            <option value ="I">Institute</option>
                                            <option value ="P">Personal</option>
                                        </select>
                                    </span>
                                </div>

                               <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>Subscriber Type</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <select class="IASComboBox" TABINDEX="1" name="subscriberType" id="subscriberType">
                                            <option value="0" selected>Select</option>
                                        </select>
                                    </span>
                                </div> 
                                
                                <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>Journal Group:</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <select class="IASComboBox" TABINDEX="1" name="journalGroupName" id="journalGroupName">
                                            <option value="0" selected>Select</option>
                                        </select>
                                    </span>
                                </div>                               
                            </div>


                            <%-- Search Criteria right div --%>
                            <div class="IASFormRightDiv">

<                               <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>Country:</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <select class="IASComboBox" TABINDEX="2" name="country" id="country">
                                            <option value="0" selected >Select</option>
                                        </select>                                        
                                    </span>
                                </div>
                                <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>State:</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <select class="IASComboBox" TABINDEX="3" name="state" id="state">
                                            <option value="" selected >Select</option>
                                        </select>
                                        <%--<input class="IASTextBoxMandatory" TABINDEX="3" name="state" id="state" value="<jsp:getProperty name="inwardFormBean" property="state"/>"--%>
                                    </span>
                                </div>                                     
                                <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>City:</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <select class="IASComboBox" TABINDEX="4" name="city" id="city">
                                            <option value="NULL">Select</option>
                                        </select>
                                    </span>
                                </div>
                                <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>Date Range:</label>
                                    </span>
                                    <div class="dateDiv"></div> 
                                    <span class="IASFormDivSpanInputBox">
                                        <input class="IASDateTextBox" TABINDEX="5" readonly size="10" type="text" id="from" name="from"/>
                                    </span>
                                    <span class="IASFormDivSpanForHyphen">
                                        <label> to </label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <input class="IASDateTextBox" TABINDEX="6" readonly size="10" type="text" id="to" name="to"/>
                                    </span>
                                </div>
                            </div>

                            <div class="actionBtnDiv">
                                <input class="IASButton" TABINDEX="6" type="button" value="Search" onclick="searchSubscriber()"/>
                                <input class="IASButton" TABINDEX="7" type="reset" value="Reset"/>
                            </div>

                        </fieldset>

                        <fieldset class="subMainFieldSet">
                            <legend>Subscriber List</legend>
                            <table class="datatable" id="subscriberTable"></table>
                            <div id="pager"></div>
                        </fieldset>
                    </fieldset>
                </div>
            </form>
        </div>
    </body>
</html>