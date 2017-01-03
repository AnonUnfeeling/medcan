var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function () {

    $('#pagination-demo').twbsPagination({
        totalPages: 35,
        visiblePages: 7,
        onPageClick: function (event, page) {
            getUsers(page);
        }
    });
});

function getUsers(page) {
    $.ajax({
        method: "POST",
        url: "/users",
        data: {page: page},
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data;

        var table = $('#table-body');
        table.find('tr').remove();
        $(arr).each(function () {
            var user = $(this)[0];
            table.append('<tr><td>' + user.login + '</td><td>' + user.role + '</td>' +
                '<td>' + user.company + '</td>' +
                '<td class="text-right"><span id=' + user.login + ' ' +
                'data-singleton="true" data-toggle="confirmation" ' +
                'class="glyphicon glyphicon-remove-circle company-control" ' +
                'aria-hidden="true"></span></td></tr>');
        });
    });
}

function createUser() {
    var success_msg = $('#success');
    var error_msg = $('#error');
    $.ajax({
        method: "POST",
        url: "/user",
        data: {
            login: $('#userLogin').val(),
            password: $('#userPassword').val(),
            role: $('#userRole').val(),
            company: $('#userCompany').val()
        },
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        success_msg.hide();
        error_msg.hide();
        success_msg.show();
    }).fail(function (data) {
        success_msg.hide();
        error_msg.hide();
        error_msg.show();
    });
}


function loadRole() {
    $.ajax({
        method: "GET",
        url: "/getRoles",
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        console.log(data);
        var arr = data;

        var select = $('#userRole').empty();
        Object.keys(arr).forEach(function (key) {
            select.append('<option value="' + arr[key] + '">' +
                '' + arr[key] + '</option>');
        });
    });
}

function loadCompanies() {
    $.ajax({
        method: "GET",
        url: "/getAllCompany",
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        console.log(data);
        var arr = data;

        var select = $('#userCompany').empty();
        $(arr).each(function () {
            var company = $(this)[0];
            select.append('<option value="' + company.name + '">' +
                '' + company.name + '</option>');
        });
    });
}