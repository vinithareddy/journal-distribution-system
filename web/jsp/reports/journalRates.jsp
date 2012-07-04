<%--
    Document   : Journal Rate - Print
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="../templates/style.jsp" %>
        <link rel="stylesheet" type="text/css" href="css/report/journalRates.css" />

        <title>Annual Rates for Journal</title>
        <script type="text/javascript">
            $(document).ready(function() {
                jdsAppend("<%=request.getContextPath() + "/CMasterData?md=year"%>","year","year");
                jdsAppend("<%=request.getContextPath() + "/CMasterData?md=subscriberType"%>","subscriberType","subscriberType");
            });
        </script>
        <script>
            var isPageLoaded = false;
            $(function(){

                      $("#datatable").jqGrid({
                        url:"<%=request.getContextPath() + "/reports?action=listRates"%>",
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
                        colNames:['Id','Journal Group Name','Subscriber Type','Year','1 Year','2 Year','3 Year','5 Year'],
                        colModel :[
                          {name:'id', index:'id', width:50, align:'center', xmlmap:'id'},
                          {name:'journalGroupName', index:'journalGroupName', width:50, align:'center', xmlmap:'journalGroupName'},
                          {name:'subtypedesc', index:'subtypedesc', width:50, align:'center', xmlmap:'subtypedesc'},
                          {name:'year', index:'year', width:50, align:'center', xmlmap:'year'},
                          {name:'1_Year', index:'1_Year', width:50, align:'center', xmlmap:'1_Year'},
                          {name:'2_Year', index:'2_Year', width:50, align:'center', xmlmap:'2_Year'},
                          {name:'3_Year', index:'3_Year', width:50, align:'center', xmlmap:'3_Year'},
                          {name:'5_Year', index:'journalRate_Id', width:50, align:'center', xmlmap:'5_Year'},
                        ],
                        xmlReader : {
                          root: "results",
                          row: "row",
                          page: "results>page",
                          total: "results>total",
                          records : "results>records",
                          repeatitems: false,
                          id: "id"
                       },
                        pager: '#pager',
                        rowNum:10,
                        rowList:[10,20,30],
                        viewrecords: true,
                        gridview: true,
                        caption: '&nbsp;',
                    gridComplete: function() {
                        var ids = jQuery("#datatable").jqGrid('getDataIDs');
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
            function getRates(){
                if ($("#year").val() == 0) {
                    alert("Select Year");
                }
                else if ($("#subscriberType").val() == 0){
                    alert("Select Subscriber Type");
                }
                else {
                        isPageLoaded = true;

                        jQuery("#datatable").setGridParam({postData:
                                {journalGroupName          : $("#journalGroupName").val(),
                                year                       : $("#year").val()
                                }
                            });
                        jQuery("#datatable").setGridParam({ datatype: "xml" });
                        jQuery("#datatable").trigger("clearGridData");
                        jQuery("#datatable").trigger("reloadGrid");
                }
            }
        </script>

    </head>
    <body>
        <%@include file="../templates/layout.jsp" %>

        <div id="bodyContainer">
            <form method="post" action="<%=request.getContextPath() + "/reports?action=printRates"%>" name="listRates">
                <div class="MainDiv">
                    <fieldset class="MainFieldset">
                        <legend>List and Print Annual Rates for Journal</legend>

                        <%-----------------------------------------------------------------------------------------------------%>
                        <%-- Search Criteria Field Set --%>
                        <%-----------------------------------------------------------------------------------------------------%>
                        <fieldset class="subMainFieldSet">
                            <legend>Search Criteria</legend>

                            <%-- Search Criteria left div --%>
                            <div class="IASFormLeftDiv">
                                <%--
                                <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>Journal Group Name</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <select class="IASComboBox" TABINDEX="1" name="journalGroupName" id="journalGroupName">
                                            <option value="0" selected>Select</option>
                                        </select>
                                    </span>
                                </div>
                                --%>
                                <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>Year</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <select class="IASComboBox" TABINDEX="2" name="year" id="year">
                                            <option value="0">Select</option>
                                        </select>
                                    </span>
                                </div>
                            </div>

                           <%-- Search Criteria Right div --%>
                            <div class="IASFormLeftDiv">

                                <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>Subscriber Type</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <select class="IASComboBox" TABINDEX="3" name="subscriberType" id="subscriberType">
                                            <option value="0" selected>Select</option>
                                        </select>
                                    </span>
                                </div>
                            </div>

                            <div class="IASFormFieldDiv">
                                <div id="searchBtnDiv">
                                    <input class="IASButton" TABINDEX="3" type="button" onclick="getRates()" value="Search"/>
                                </div>
                            </div>

                        </fieldset>



                        <%-----------------------------------------------------------------------------------------------------%>
                        <%-- Search Result Field Set --%>
                        <%-----------------------------------------------------------------------------------------------------%>
                       <fieldset class="subMainFieldSet">
                            <legend>Result</legend>

                            <table class="datatable" id="datatable"></table>
                            <div id="pager"></div>
                        </fieldset>
                        <%-----------------------------------------------------------------------------------------------------%>
                        <%-- Print Button Field Set --%>
                        <%-----------------------------------------------------------------------------------------------------%>
                        <fieldset class="subMainFieldSet">
                            <div class="IASFormFieldDiv">
                                <div class="singleActionBtnDiv">
                                    <input class="IASButton" type="submit" value="Print" disabled id="printReportBtn"/>
                                </div>
                            </div>
                        </fieldset>
                    </fieldset>
                </div>
            </form>
        </div>
    </body>
</html>