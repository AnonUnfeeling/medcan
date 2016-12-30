$(document).ready(function () {
    var company_control = $('.company-control');

    $('[data-toggle=confirmation]').confirmation({
        rootSelector: '[data-toggle=confirmation]'
    });

    company_control.click(function () {
        var control = $(this);
        control.confirmation('show');
        $('#table-body').on('confirmed.bs.confirmation', deleteCompany(control));
    });
});

function deleteCompany(control) {
    $(control).parent().parent().remove();
    $.ajax({
        method: "POST",
        url: "/removeCompany",
        data: { companyName: control.attr('id')},
        dataType: "json"
    }).fail(function (data) {
        console.log(data);
    });
}

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
        if (data.responseText=='CREATED'){
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