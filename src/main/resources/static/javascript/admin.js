$(document).ready(function () {

});

function createCompany() {
    var success_msg = $('#success');
    var error_msg = $('#error');
    $.ajax({
        method: "POST",
        url: "/makeCompany",
        data: { companyName: $('#companyName').val()},
        dataType: "json"
        }).fail(function (data) {
        console.log(data);
        if (data=='CREATED'){
            success_msg.hide();
            error_msg.hide();
            success_msg.show();
        } else{
            success_msg.hide();
            error_msg.hide();
            error_msg.show();
        }
    });
}