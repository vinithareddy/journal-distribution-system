<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="../templates/style.jsp"></jsp:include>
        <title>Search Subscriber</title>
        <script>
            window.returnValue=null;
            var subscriber = window.dialogArguments;
            var selectedSubscriberId = null;
            var selectedsubscriber;

            $(function(){
                $("#subscriberList").jqGrid({
                    url:"<%=request.getContextPath() + "/subscriber?action=search"%>",
                    postData:{
                        city            : subscriber.city,
                        subscriberName  : subscriber.name,
                        country         : subscriber.country,
                        state           : subscriber.state,
                        pincode         : subscriber.pincode,
                        institution     : subscriber.institution,
                        department      : subscriber.department,
                        email           : subscriber.email
                    },
                    datatype: 'xml',
                    mtype: 'GET',
                    width: '100%',
                    height: 350,
                    autowidth: true,
                    forceFit: true,
                    sortable: true,
                    loadonce: false,
                    rownumbers: true,
                    emptyrecords: "No subscribers to view",
                    loadtext: "Loading...",
                    colNames:['Select','Subscriber Id','Subscriber Name', 'Department','Institute','City','Pin Code','Country', 'Email'],
                    colModel :[
                        {name:'Select', index:'select', width:15, align:'center',xmlmap:'subscriberNumber'
                            /*formatter:function (cellvalue, options, rowObject) {
                                return '<input onclick="selectedSubscriberId=this.value" type="radio" id="selectedSubscriberRadio" name="selectedSubscriberRadio" value="' + cellvalue + '"/>';
                            }*/
                        },
                        {name:'SubscriberNumber', index:'subscriberNumber', width:35, align:'center', sortable:false, xmlmap:'subscriberNumber'},
                        {name:'SubscriberName', index:'subscriberName', width:50, align:'center', sortable:false, xmlmap:'subscriberName'},
                        {name:'Department', index:'department', width:50, align:'center', sortable:false, xmlmap:'department'},
                        {name:'Instituttion', index:'institute', width:50, align:'center', sortable:false, xmlmap:'institution'},
                        {name:'City', index:'city', width:30, align:'center', sortable: true, sorttype: 'int',xmlmap:'city'},
                        {name:'PinCode', index:'pincode', width:20, align:'center', sortable:false, xmlmap:'pincode'},
                        {name:'Country', index:'country', width:20, align:'center', sortable:false, xmlmap:'country'},
                        {name:'Email', index:'email', width:40, align:'center',sortable:false, xmlmap:'email'}
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
                    rowNum:15,
                    rowList:[15,30, 50],
                    viewrecords: true,
                    gridview: true,
                    caption: '&nbsp;',

                    gridComplete: function() {
                        var ids = jQuery("#subscriberList").jqGrid('getDataIDs');
                        for (var i = 0; i < ids.length; i++) {
                            var cl = ids[i];
                            action = "<input type='radio' name='selectedSubscriberRadio' id='selectedSubscriberRadio'" + " value=" + "\"" + cl + "\"" + " onclick='selectedSubscriberId=this.value'" + "/>";
                            jQuery("#subscriberList").jqGrid('setRowData', ids[i], { Select: action });
                        }
                    }
                });
            });

            function CheckReturnValue(){
                selectedsubscriber = jQuery("#subscriberList").getRowData(selectedSubscriberId);
                window.returnValue = selectedsubscriber;
                //window.returnValue = selectedSubscriberId;
//                if(!window.returnValue){
//                    alert("Please select a subscriber");
//                    return false;
//                }
            }
        </script>

    </head>
    <body onunload="return CheckReturnValue()">
            <div class="MainDiv">
                <div class="subMainDiv">
                    <fieldset class="MainFieldset">
                        <legend>Search Subscriber Result</legend>
                        <fieldset class="subMainFieldSet">
                            <legend>Subscriber List</legend>
                            <table class="datatable" id="subscriberList"></table>
                            <div id="pager"></div>
                        </fieldset>

                        <fieldset class="subMainFieldSet">
                            <div class="actionBtnDiv">
                                    <input class="IASButton" TABINDEX="6" type="button" onclick="window.returnValue=selectedSubscriberId;window.close()" value="OK" name="btnOk"/>
                                    <input class="IASButton" TABINDEX="8" type="submit" value="Cancel" name="btnCancel" onclick="window.close()"/>
                            </div>
                        </fieldset>
                    </fieldset>
                </div>
            </div>
    </body>
</html>