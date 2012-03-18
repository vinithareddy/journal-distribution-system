<%--
    Document   : Search Subject Group
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="../templates/style.jsp" %>
        <link rel="stylesheet" type="text/css" href="css/masterdata/subjectGroup.css" />

        <title>Journal Subject Group</title>
        <script type="text/javascript" src="<%=request.getContextPath() + "/js/masterdata/searchSubGroup.js"%>"></script>
        <script type="text/javascript" src="<%=request.getContextPath() + "/js/masterdata/validateSubGroup.js"%>"></script>
        <script type="text/javascript">
           // var selectedJournal = 0;
            var selectedId = 0;
            //initally set to false, after the first search the flag is set to true
            var isPageLoaded = false;

            $(function(){

                $("#subGroupTable").jqGrid({
                    url:"<%=request.getContextPath() + "/subGroup?action=search"%>",
                    datatype: 'xml',
                    mtype: 'GET',
                    width: '100%',
                    height: 240,
                    autowidth: true,
                    forceFit: true,
                    sortable: true,
                    loadonce: true,
                    rownumbers: true,
                    emptyrecords: "No Record",
                    loadtext: "Loading...",
                    colNames:['Id','subject Group Name','View/Edit'],
                    colModel :[
                        {name:'id', index:'id', width:50, align:'center', xmlmap:'id'},
                        {name:'subGroupName', index:'subGroupName', width:80, align:'center', xmlmap:'subGroupName'},
                        {name:'Action', index:'action', width:80, align:'center',formatter:'showlink'}
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
                        var ids = jQuery("#subGroupTable").jqGrid('getDataIDs');

                        for (var i = 0; i < ids.length; i++) {
                            action = "<a style='color:blue;' href='subGroup?action=view&id=" + ids[i] + "'>View</a><a style='color:blue;' href='subGroup?action=edit&id=" + ids[i] + "'>/ Edit</a>";
                            jQuery("#journalTable").jqGrid('setRowData', ids[i], { Action: action });
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



            // called when the search button is clicked
            function searchSubGroup(){

                    isPageLoaded = true;
                    jQuery("#subGroupTable").setGridParam({ datatype: "xml" });
                    jQuery("#subGroupTable").trigger("clearGridData");
                    jQuery("#subGroupTable").trigger("reloadGrid");
                }

            // draw the date picker.
            //jQueryDatePicker("from","to");

        </script>
    </head>
    <body>
        <%@include file="../templates/layout.jsp" %>

        <div id="bodyContainer">
            <form method="post" action="<%=request.getContextPath() + "/subGroup"%>" name="searchSubGroupForm">
                <div class="MainDiv">
                    <fieldset class="MainFieldset">
                        <legend>Search Subject Group</legend>

                        <%-----------------------------------------------------------------------------------------------------%>
                        <%-- Action Set --%>
                        <%-----------------------------------------------------------------------------------------------------%>
                        <fieldset class="subMainFieldSet">
                            <div class="IASFormFieldDiv">
                                <div class="IASFormLeftDiv">
                                    <div id="searchBtnDiv">
                                        <input class="IASButton" TABINDEX="3" type="button" value="Search Existing Subject Group" onclick="searchPriceGroup()"/>
                                    </div>
                                </div>

                                <div class="IASFormRightDiv">
                                    <input type="hidden" name="action" id="action"/>
                                    <div id="saveBtnDiv">
                                        <input onclick="setActionValue('add')"  class="IASButton" TABINDEX="3" type="submit" value="Add New Subject Group" id="btnAdd" name="btnSubmitAction"/>
                                    </div>
                                </div>
                            </div>
                        </fieldset>

                        <%-----------------------------------------------------------------------------------------------------%>
                        <%-- Search Result Field Set --%>
                        <%-----------------------------------------------------------------------------------------------------%>
                        <fieldset class="subMainFieldSet">
                            <legend>Subject Group View / Edit</legend>

                            <table class="datatable" id="subGroupTable"></table>
                            <div id="pager"></div>
                        </fieldset>

                    </fieldset>
                </div>
            </form>
        </div>
    </body>
</html>