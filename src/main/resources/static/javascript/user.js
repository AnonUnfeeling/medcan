var token = $("meta[name='_csrf']").attr("content");

function loadAllUsers() {
    $('#pagination-demo').twbsPagination({
        totalPages: 35,
        visiblePages: 7,
        onPageClick: function (event, page) {
            getUsers(page);
        }
    });
}

function getUsersByCompany() {
    console.log($('#companyName').val() +" "+ $('#page').val());
    $.ajax({
        method: "GET",
        url: "/usersByCompany",
        data: {
            companyName: $('#companyName').val(),
            page:  $('#page').val()
        },
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
                '<td class="text-right"><span id=' +
                user.login + ' data-singleton="true"' +
                ' data-toggle="edit" class="glyphicon glyphicon glyphicon-pencil user-control" ' +
                'aria-hidden="true"></span>' +
                '<td class="text-right"><span id=' + user.login + ' ' +
                'data-singleton="true" data-toggle="confirmation" ' +
                'class="glyphicon glyphicon-remove-circle users-control" ' +
                'aria-hidden="true"></span></td>');
        });
        manageCompany();
    });
}

function getUsers(page) {
    $.ajax({
        method: "POST",
        url: "/users",
        data: {
            page: page
        },
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
                '<td class="text-right"><span id=' +
                user.login + ' data-singleton="true"' +
                ' data-toggle="edit" class="glyphicon glyphicon glyphicon-pencil user-control" ' +
                'aria-hidden="true"></span>' +
                '<td class="text-right"><span id=' + user.login + ' ' +
                'data-singleton="true" data-toggle="confirmation" ' +
                'class="glyphicon glyphicon-remove-circle users-control" ' +
                'aria-hidden="true"></span></td>');
        });
        manageCompany();
    });
}

function manageCompany() {
    var company_control = $('.users-control');

    $('[data-toggle=confirmation]').confirmation({
        rootSelector: '[data-toggle=confirmation]'
    });

    company_control.click(function () {
        var control = $(this);
        control.confirmation('show');
        $('#table-body').on('confirmed.bs.confirmation', deleteUser(control));
    });

    $('[data-toggle=confirmation]').confirmation({
        rootSelector: '[data-toggle=edit]'
    });

    var edit = $('.user-control');
    edit.click(function () {
        var edit = $('#addCompanyModal');
        var control = $(this);
        $('#userLogin').val(control.attr('id'));
        loadRole();
        loadCompanies();
        edit.modal('show');
    })
}

function deleteUser(control) {
    $(control).parent().parent().remove();
    $.ajax({
        method: "POST",
        url: "/removeUser",
        data: {userLogin: control.attr('id')},
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        console.log(data);
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
        select.append('<option value="Non Company">Non Company</option>');
        $(arr).each(function () {
            var company = $(this)[0];
            select.append('<option value="' + company.name + '">' +
                '' + company.name + '</option>');
        });
    });
}