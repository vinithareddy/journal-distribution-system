/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function makeReadOnly(){


    var formFields = document.getElementsByTagName("input");

    for(var i=0;i<formFields.length;i++){
        var formField = formFields[i];
        if( formField.className == "IASTextBox"
            || formField.className == "IASEmailTextBox"
            || formField.className == "IASCheckBox"
            || formField.className == "IASDateTextBox"
        ){
            formField.disabled = true;
            //formField.style.backgroundColor = "#D3D3D3";
        }
    }

    formFields = document.getElementsByTagName("select");

    for(i=0;i<formFields.length;i++){
        formField = formFields[i];
        if(formField.className == "IASComboBox"){
            formField.disabled = true;
            //formField.style.backgroundColor = "#D3D3D3";
        }
    }

    formFields = document.getElementsByTagName("textarea");

    for(i=0;i<formFields.length;i++){
        formField = formFields[i];
        if(formField.className == "IASTextArea"){
            formField.disabled = true;
            //formField.style.backgroundColor = "#D3D3D3";
        }
    }

    document.getElementById("btnSave").disabled = true;
    document.getElementById("btnSearchSubscriber").disabled = true;

    if(document.getElementById("checkReturn").checked){
        document.getElementById("btnSendReturn").disabled = false;
    }else{
        document.getElementById("btnSendReturn").disabled = true;
    }


}