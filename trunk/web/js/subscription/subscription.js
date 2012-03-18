var isPageLoaded = false;
function setSubmitButtonValue(button,value){
    button.value = value;
}

function listSubscription(mode){
    var lastSel;
    var editable = false;
    if (mode=="Display"){
        hrefText = "View";
        editable = false;
    }else{
        hrefText = "Edit";
        editable = true;
    }
    $(function(){
        //$("#subscriptionDetailDiv").hide();
        $("#subscriptionList").jqGrid({
            url:'/JDS/subscription?oper=getsubscription&subscriberNumber=' + $("#subscriberNumber").val(),
            //data: "subscriberNumber=" + $("#subscriberNumber").val(),
            datatype: 'xml',
            mtype: 'GET',
            height: 235,
            autowidth: true,
            forceFit: true,
            sortable: true,
            loadonce: true,
            rownumbers: true,
            sortname:'subscriptionID',
            emptyrecords: "No subscription(s) to view",
            loadtext: "Loading...",
            colNames:['Subscription Id','Inward No','Subscription Date','Subscription Value','Amount Paid', 'Balance', 'Currency','Details'],
            colModel :[
            {
                name:'subscriptionID',
                index:'id',
                width:25,
                align:'center',
                xmlmap:'id',
                sortable: true,
                key: true
            },
            {
                name:'inwardNumber',
                index:'inwardNumber',
                width:20,
                align:'center',
                xmlmap:'inwardNumber'
            },

            {
                name:'subscriptionDate',
                index:'subscriptionDate',
                width:30,
                align:'center',
                xmlmap:'subscriptionDate'
            },
            {
                name:'subscriptionValue',
                index:'subscriptionValue',
                width:30,
                align:'center',
                xmlmap:'subscriptionTotal'
            },
            {
                name:'amountPaid',
                index:'amountPaid',
                width:20,
                align:'center',
                xmlmap:'amount'
            },

            {
                name:'balance',
                index:'balance',
                width:20,
                align:'center',
                xmlmap:'balance'
            },
            {
                name:'currency',
                index:'currency',
                width:15,
                align:'center',
                xmlmap:'currency'
            },
            {
                name:'details',
                index:'details',
                width:15,
                align:'center'
            }],
            xmlReader : {
                root: "results",
                row: "row",
                repeatitems: false,
                id: "id"
            },
            pager: '#pager',
            rowNum:10,
            rowList:[10,30,50],
            viewrecords: true,
            gridview: true,
            caption: '&nbsp;',
            gridComplete: function() {

                var ids = jQuery("#subscriptionList").jqGrid('getDataIDs');
                for (var i = 0; i < ids.length; i++) {
                    action = "<a style=\"color:blue\" href=\"#\" onclick=\"getSubscriptionDetails(" + ids[i] + ")\">" + hrefText + "</a>";
                    jQuery("#subscriptionList").jqGrid('setRowData', ids[i], {
                        "details": action
                    });
                }
                isPageLoaded = true;
            }
        });


    });

}

function getSubscriptionDetails(subscriptionId){
    //$("#subscriptionDetailDiv").show();
   windowParams = "dialogHeight:500px; dialogWidth:800px; center:yes; resizeable:no; status:no; menubar:no;\n\
                    scrollbars:yes; toolbar: no;"
   openModalPopUp("jsp/subscription/subscriptiondetails.jsp?id=" + subscriptionId , windowParams);
   return false;
    /*jQuery("#subscriptionDetail").setGridParam({
        datatype: 'xml',
        url: 'subscription?oper=detail&id=' + subscriptionId
    });

    jQuery("#subscriptionDetail").trigger("reloadGrid");*/
//alert(subscriptionId);
}