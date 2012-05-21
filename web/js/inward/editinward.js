function makeInwardReadOnly(){

    $("#btnEditInward").button("disable");    
}

function enablePayment(){
    if($("#inwardPurpose").val().toLowerCase() == "payment"){
        enableSubscriptionID(true);
    }else{
        enableSubscriptionID(false);
    }
}

function loadData(){
    jdsAppend("CMasterData?md=country","country","country");
    jdsAppend("CMasterData?md=state","state","state");
    jdsAppend("CMasterData?md=district","district","district");
    jdsAppend("CMasterData?md=city","city","city");
    jdsAppend("CMasterData?md=purpose","purpose","inwardPurpose", "", enablePayment);  //call back function to enable the subscriptionid fields/buttons
    jdsAppend("CMasterData?md=payment_mode","payment_mode","paymentMode");
    jdsAppend("CMasterData?md=currency","currency","currency");
    jdsAppend("CMasterData?md=language","language","language");

}