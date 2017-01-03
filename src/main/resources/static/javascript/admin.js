var token = $("meta[name='_csrf']").attr("content");

var $pagination;
var defaultOpts = {
    totalPages: 20,
    visiblePages: 7,
    onPageClick: function (event, page) {
        getCompanies(page);
    }
};

$(document).ready(function () {
    $pagination =$('#pagination');
    $pagination.twbsPagination(defaultOpts);
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
        console.log(data);
        var arr = data.content;
        var totalPages = data.totalPages;
        var currentPage = $pagination.twbsPagination('getCurrentPage');
        $pagination.twbsPagination('destroy');
        $pagination.twbsPagination($.extend({}, defaultOpts, {
            startPage: currentPage,
            totalPages: totalPages,
            initiateStartPageClick: false
        }));

        var table = $('#table-body');
        table.find('tr').remove();
        $(arr).each(function () {
            var company = $(this)[0];
            table.append('<tr><td>' + company.name + '</td><td class="text-right"><span id=' + company.name + ' data-singleton="true" data-toggle="confirmation" class="glyphicon glyphicon-remove-circle company-control" aria-hidden="true"></span></td></tr>');
        });
        manageCompany();
    }).fail(function (data) {
        console.log(data);
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
    }).done(function (data) {
        console.log(data);
    });
}

function createCompany() {
    var message = $('#message-container');
    $.ajax({
        method: "POST",
        url: "/makeCompany",
        data: { companyName: $('#companyName').val()},
        dataType: "json",
        headers:{
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        $(message).children().remove();
        message.append("<div id='success' class='alert alert-success'><strong>Success!</strong>'+ data+'</div>")
    }).fail(function (data) {
        $(message).children().remove();
        message.append("<div id='error' class='alert alert-danger'><strong>Error!</strong>+' data +'</div>")
    });
}
