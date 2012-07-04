<%--
    Document   : List Journal - Statement
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="../templates/style.jsp" %>
        <link rel="stylesheet" type="text/css" href="css/report/statement.css" />

        <script type="text/javascript">
            $(document).ready(function() {
                jdsAppend("<%=request.getContextPath() + "/CMasterData?md=journalName"%>","journalName","journalName");
                jdsAppend("<%=request.getContextPath() + "/CMasterData?md=year"%>","year","year");
                jdsAppend("<%=request.getContextPath() + "/CMasterData?md=subscriberType"%>","subscriberType","subscriberType");
            });
        </script>

        <script type="text/javascript">
            var isPageLoaded = false;

            // draw the date picker.
            jQueryDatePicker("from","to");

            $(function(){

                      $("#statementTable").jqGrid({
                        url:"<%=request.getContextPath() + "/reports?action=statement"%>",
                        datatype: 'xml',
                        mtype: 'GET',
                        width: '100%',
                        height: 240,
                        autowidth: true,
                        forceFit: true,
                        sortable: true,
                        loadonce: true,
                        rownumbers: true,
                        emptyrecords: "No records to view",
                        loadtext: "Loading...",
                        colNames:['Journal Code','Journal Name', 'Subscriber Type','Subscriber Count','No. Of Copies'],
                        colModel :[
                          {name:'journalCode', index:'journalCode', width:50, align:'center', xmlmap:'journalCode'},
                          {name:'journalName', index:'journalName', width:80, align:'center', xmlmap:'journalName'},
                          {name:'subtypecode', index:'subtypecode', width:80, align:'center', xmlmap:'subtypecode'},
                          {name:'subscriberCount', index:'subscriberCount', width:80, align:'center', sortable: true, sorttype: 'int',xmlmap:'subscriberCount'},
                          {name:'copies', index:'copies', width:80, align:'center', sortable:false, xmlmap:'copies'},
                        ],
                        xmlReader : {
                          root: "results",
                          row: "row",
                          page: "results>page",
                          total: "results>total",
                          records : "results>records",
                          repeatitems: false,
                          id: "journalCode"
                       },
                        pager: '#pager',
                        rowNum:10,
                        rowList:[10,20,30],
                        viewrecords: true,
                        gridview: true,
                        caption: '&nbsp;',
                        gridComplete: function() {
                            var ids = jQuery("#statementTable").jqGrid('getDataIDs');
                            if(ids.length > 0){
                                $("#printReportBtn").button("enable");
                            }
                        },
                        beforeRequest: function(){
                          return isPageLoaded;
                        },
                        loadError: function(xhr,status,error){
                            alert("Failed getting data from server" + status);
                        }

               });

            });

            // called when the search button is clicked
            function getStatements(){
                if(($("#journalName").val() == 0) && $("#year").val() == 0 && $("#subscriberType").val() == 0)
                {
                        alert("Atleast one search parameter should be selected along with Year");
                }
                else if ($("#year").val() == 0)
                {
                        alert("Select Year");
                }    
                else{
                    isPageLoaded = true;
                    jQuery("#statementTable").setGridParam({postData:
                            {journalName    : $("#journalName").val(),
                            year            : $("#year").val(),
                            subscriberType  : $("#subscriberType").val()
                        }});
                    jQuery("#statementTable").setGridParam({ datatype: "xml" });
                    jQuery("#statementTable").trigger("clearGridData");
                    jQuery("#statementTable").trigger("reloadGrid");

                }
            }


        </script>
        <title>Statement</title>
    </head>
    <body>
        <%@include file="../templates/layout.jsp" %>

        <div id="bodyContainer">
            <form method="post" action="<%=request.getContextPath() + "/reports?action=printStatement"%>" name="statement">
                <div class="MainDiv">
                    <fieldset class="MainFieldset">
                        <legend>Statement</legend>

                        <%-----------------------------------------------------------------------------------------------------%>
                        <%-- Search Criteria Field Set --%>
                        <%-----------------------------------------------------------------------------------------------------%>
                        <fieldset class="subMainFieldSet">
                            <legend>Statement for Journal</legend>

                            <%-- Search Criteria left div --%>
                            <div class="IASFormLeftDiv">

                                <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>Journal Name</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <select class="IASComboBoxWide" TABINDEX="1" name="journalName" id="journalName">
                                            <option value="0" selected>Select</option>
                                        </select>
                                    </span>
                                </div>

                                <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>Year</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <select class="IASComboBoxSmallMandatory" TABINDEX="2" name="year" id="year">
                                            <option value="0">Select</option>
                                        </select>
                                    </span>
                                </div>
                            </div>

                            <%-- Search Criteria Right div --%>
                            <div class="IASFormRightDiv">

                                <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>Subscriber Type</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <select class="IASComboBoxWide" TABINDEX="3" name="subscriberType" id="subscriberType">
                                            <option value="0" selected>Select</option>
                                        </select>
                                    </span>
                                </div>

                            </div>
                            <div class="IASFormFieldDiv">
                                <div id="searchBtnDiv">
                                    <input class="IASButton" TABINDEX="6" type="button" value="Search" onclick="getStatements()"/>
                                </div>

                                <div id="resetBtnDiv">
                                    <input class="IASButton" TABINDEX="7" type="reset" value="Reset"/>
                                </div>
                            </div>

                        </fieldset>

                        <%-----------------------------------------------------------------------------------------------------%>
                        <%-- Search Result Field Set --%>
                        <%-----------------------------------------------------------------------------------------------------%>
                        <fieldset class="subMainFieldSet">
                            <legend>Statement Result</legend>
                            <table class="datatable" id="statementTable"></table>
                            <div id="pager"></div>
                        </fieldset>
                        <fieldset class="subMainFieldSet">
                            <div class="IASFormFieldDiv">
                                <div class="singleActionBtnDiv">
                                    <%--<input class="IASButton" type="button" value="Print" onclick="javascript:window.print();"/>--%>
                                    <input class="IASButton" type="submit" TABINDEX="8" value="Print" disabled id="printReportBtn"/>
                                </div>
                            </div>
                        </fieldset>
                    </fieldset>
                </div>
            </form>
        </div>
    </body>
</html>