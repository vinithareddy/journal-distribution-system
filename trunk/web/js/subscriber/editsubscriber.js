function makeEditSubscriberReadOnly(){
    document.getElementById("btnEditSubscriber").disabled = true;
    document.getElementById('btnDisplaySubscriber').disabled = true;
    document.getElementById("subscriberAgent").options.length=0;
    document.getElementById("subscriberAgent").disabled = true;
    document.getElementById("isDeactived").disabled = false;
}

// called when the deactive user check box is clicked
function deActivateSubscriber(){

    if(document.getElementById("isDeactived").checked){
        jdsConfirm("Do you really want to deactivate the subscriber?","",function(ret){
            if(!ret){
                $("#isDeactived").attr("checked",false);
            }else{
                $("#btnViewSubscription").attr("disabled",true);
                $("#btnAddSubscription").attr("disabled",true);
            }
        });
    }else{
        $("#btnViewSubscription").attr("disabled",false);
        $("#btnAddSubscription").attr("disabled",false);
    }
}