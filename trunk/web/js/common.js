/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 *use this function to add events to the onload event
 */
function addOnloadEvent(fnc){
    if ( typeof window.addEventListener != "undefined" )
        window.addEventListener( "load", fnc, false );
    else if ( typeof window.attachEvent != "undefined" ) {
        window.attachEvent( "onload", fnc );
    } else {
        if ( window.onload != null ) {
            var oldOnload = window.onload;
            window.onload = function ( e ) {
                oldOnload( e );
                window[fnc]();
            };
        } else
            window.onload = fnc;
    }
}
/*
 * addOnloadEvent(myFunctionName);//
 *  Or to pass argumentsaddOnloadEvent(function(){ myFunctionName('myArgument') });
 */

function checkMandatoryFields(){

    var tagsToValidate = new Array('input', 'select','textarea');
    var isMandatoryFilledFlag = true;

    for (var i=0;i<tagsToValidate.length;i++){
        var allFields = document.getElementsByTagName(tagsToValidate[i]);

        for (var j=0;j<allFields.length;j++){
            if(allFields[j].className == 'IASTextBoxMandatory' ||
                allFields[j].className == 'IASTextAreaMandatory' ||
                allFields[j].className == 'IASComboBoxMandatory' ){

                if (allFields[j].value.length == 0) {
                    var elementID = allFields[j].id;
                    document.getElementById(elementID).style.backgroundColor = 'pink';
                    document.getElementById(elementID).focus();
                    isMandatoryFilledFlag = false;
                //break;
                } else {
                    continue;
                }
            }
        }
    }
    if (isMandatoryFilledFlag == false){
        alert("Please fill in all Mandatory fields. The mandatory fields are marked in RED");
    }
    else{
        return true;
    }
    return false;
}


function openModalPopUp(url){
    newwindow=window.showModalDialog(url,'title',"dialogHeight: 500px; dialogWidth: 1000px;center: yes;resizeable: no;status: no;menubar: no;scrollbars: yes;toolbar: no;");
}

function changePwdDialog(url) {
	newwindow=window.showModalDialog(url,'title',"dialogHeight: 200px; dialogWidth: 200px;center: yes;resizeable: no;status: no;menubar: no;scrollbars: no;toolbar: no;");
}