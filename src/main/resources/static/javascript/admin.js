var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function () {

    getCompanies(0);

    $('#pagination-demo').twbsPagination({
        totalPages: 35,
        visiblePages: 7,
        onPageClick: function (event, page) {
           getCompanies(page);
        }
    });

});

function getCompanies(page) {
    $.ajax({
        method: "GET",
        url: "/companies",
        data: {page: page},
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data.content;
        var table = $('#table-body');
        table.find('tr').remove();
        $(arr).each(function () {
            var company = $(this)[0];
            table.append('<tr><td>' + company.name + '</td><td class="text-right"><span id=' + company.name + ' data-singleton="true" data-toggle="confirmation" class="glyphicon glyphicon-remove-circle company-control" aria-hidden="true"></span></td></tr>');
        });
        manageCompany();
    });
}

function manageCompany() {
    var company_control = $('.company-control');

    $('[data-toggle=confirmation]').confirmation({
        rootSelector: '[data-toggle=confirmation]'
    });

    company_control.click(function () {
        var control = $(this);
        control.confirmation('show');
        $('#table-body').on('confirmed.bs.confirmation', deleteCompany(control));
    });
}

function deleteCompany(control) {
    $(control).parent().parent().remove();
    $.ajax({
        method: "POST",
        url: "/removeCompany",
        data: { companyName: control.attr('id')},
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
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
        dataType: "json",
        headers:{
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
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