<%--
    Document   : Circulation Figure for Journal
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="../templates/style.jsp" %>
        <link rel="stylesheet" type="text/css" href="css/report/circulationFigures.css" />
        <title>Circulation Figures</title>
        <script type="text/javascript">
            $(document).ready(function() {
                jdsAppend("<%=request.getContextPath() + "/CMasterData?md=year"%>","year","year");
            });
        </script>

        <script>
            var isPageLoaded = false;

            $(function(){

                      $("#circulationTable").jqGrid({
                        url:"<%=request.getContextPath() + "/reports?action=circulationFigures"%>",
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
                        colNames:['Journal Code','Journal Name', 'Inst. India','Inst. Abroad','Indi. India','Indi. Abroad','Comp','Auth','Total Copies','Print Order','Balance Copies'],
                        colModel :[
                          {name:'journalCode', index:'journalCode', width:50, align:'center', xmlmap:'journalCode'},
                          {name:'journalName', index:'journalName', width:50, align:'center', xmlmap:'journalName'},
                          {name:'instIndia', index:'instIndia', width:50, align:'center', xmlmap:'instIndia'},
                          {name:'instAbroad', index:'instAbroad', width:50, align:'center', xmlmap:'instAbroad'},
                          {name:'indiIndia', index:'indiIndia', width:50, align:'center', xmlmap:'indiIndia'},
                          {name:'indiAbroad', index:'indiAbroad', width:50, align:'center', xmlmap:'indiAbroad'},
                          {name:'comp', index:'comp', width:50, align:'center', xmlmap:'comp'},
                          {name:'auth', index:'auth', width:50, align:'center', xmlmap:'auth'},
                          {name:'totalCopies', index:'totalCopies', width:50, align:'center', xmlmap:'totalCopies'},
                          {name:'printOrder', index:'printOrder', width:50, align:'center', xmlmap:'printOrder'},
                          {name:'balanceCopies', index:'balanceCopies', width:50, align:'center', xmlmap:'balanceCopies'}

                        ],
                        xmlReader : {
                          root: "results",
                          row: "row",
                          page: "results>page",
                          total: "results>total",
                          records : "results>records",
                          repeatitems: false,
                          id: "journal_id"
                       },
                        pager: '#pager',
                        rowNum:10,
                        rowList:[10,20,30],
                        viewrecords: true,
                        gridview: true,
                        caption: '&nbsp;',
                        gridComplete: function() {
                            var ids = jQuery("#circulationTable").jqGrid('getDataIDs');
                            if(ids.length > 0){
                                $("#printReportBtn").button("enable");
                            }
                        },
                        beforeRequest: function(){
                          return isPageLoaded;
                        },
                        loadError: function(xhr,status,error){
                            alert("Failed getting data from server: " + status);
                        }
                    });
            });

            function getReport(){
                if($("#year").val() == 0)
                {
                        alert("Select year for which report has to be generated");
                }
                else{
                    isPageLoaded = true;
                    jQuery("#circulationTable").setGridParam({postData:
                            {year            : $("#year").val()
                        }});
                    jQuery("#circulationTable").setGridParam({ datatype: "xml" });
                    jQuery("#circulationTable").trigger("clearGridData");
                    jQuery("#circulationTable").trigger("reloadGrid");

                }
            }

        </script>
    </head>
    <body>
        <%@include file="../templates/layout.jsp" %>

        <div id="bodyContainer">
            <form method="post" action="<%=request.getContextPath() + "/reports?action=printCirculationFigures"%>" name="circulationFigures">
                <div class="MainDiv">
                    <fieldset class="MainFieldset">
                        <legend>List and Print Journals</legend>

                        <%-----------------------------------------------------------------------------------------------------%>
                        <%-- Search Criteria Field Set --%>
                        <%-----------------------------------------------------------------------------------------------------%>
                        <fieldset class="subMainFieldSet">
                            <legend>Search Criteria</legend>

                            <%-- Search Criteria left div --%>
                            <div class="IASFormLeftDiv">
                                <div class="IASFormFieldDiv">
                                    <span class="IASFormDivSpanLabel">
                                        <label>Year</label>
                                    </span>
                                    <span class="IASFormDivSpanInputBox">
                                        <select class="IASComboBox" TABINDEX="1" name="year" id="year">
                                            <option value="0">Select</option>
                                        </select>
                                    </span>
                                </div>
                            </div>
 
                            <div class="IASFormFieldDiv">
                                <div id="searchBtnDiv">
                                    <input class="IASButton" TABINDEX="2" type="button" onclick="getReport()" value="Search"/>
                                </div>
                                    
                                <div id="resetBtnDiv">
                                    <input class="IASButton" TABINDEX="3" type="reset" value="Reset"/>
                                </div>
                            </div>

                        </fieldset>


                        <%-----------------------------------------------------------------------------------------------------%>
                        <%-- Search Result Field Set --%>
                        <%-----------------------------------------------------------------------------------------------------%>
                        <fieldset class="subMainFieldSet">
                            <legend>Search Result</legend>

                            <table class="circulationTable" id="circulationTable"></table>
                            <div id="pager"></div>
                        </fieldset>
                        
                        <%-----------------------------------------------------------------------------------------------------%>
                        <%-- Print Action Field Set --%>
                        <%-----------------------------------------------------------------------------------------------------%>
                        
                        
                        <fieldset class="subMainFieldSet">
                            <div class="IASFormFieldDiv">
                                <div class="singleActionBtnDiv">
                                    <%--<input class="IASButton" type="button" value="Print" onclick="javascript:window.print();"/>--%>
                                    <input class="IASButton" type="submit" TABINDEX="4" value="Print" disabled id="printReportBtn"/>
                                </div>
                            </div>
                        </fieldset>
                    </fieldset>
                </div>
            </form>
        </div>
    </body>
</html>