function subtypeCodeAppend(){
    var subTypeDesc = document.subscriberForm.subtypedesc.options[document.subscriberForm.subtypedesc.selectedIndex].text;
    requestURL = "/JDS/CMasterData?md=subtype_code&mdvalue=" + subTypeDesc;
    $.ajax({
        type: "GET",
        url: requestURL, // change to full path of file on server
        dataType: "xml",
        success: function(xml){
            $(xml).find("subtypecode").each(function(){
                $("#subtypecode").text(("Subscriber Code - " + $(this).text()));
            });
        },
        error: function() {
            alert("XML File could not be found");
        }
    });
}


function imposeMaxLength(Event, Object, MaxLen)
{
    return (Object.value.length <= MaxLen)||(Event.keyCode == 8 ||Event.keyCode==46||(Event.keyCode>=35&&Event.keyCode<=40))
}