var token = $("meta[name='_csrf']").attr("content");

var defaultOpts = {
    totalPages: 20,
    visiblePages: 7,
    onPageClick: function (event, page) {
        getUsers(page);
    }
};

function loadAllUsers() {
    $pagination = $('#pagination');
    $pagination.twbsPagination(defaultOpts);
}

function getUsersByCompany() {
    $pagination = $('#pagination');
    $pagination.twbsPagination($.extend({}, defaultOpts, {
        onPageClick: function (event, page) {
            getUsersByCompanyName(page);
        }
    }));
}


function getUsersByCompanyName(page) {
    $.ajax({
        method: "GET",
        url: "/usersByCompany",
        data: {
            companyName: $('#companyName').val(),
            page: page
        },
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data.content;
        var totalPages = data.totalPages;
        var currentPage = $pagination.twbsPagination('getCurrentPage');
        $pagination.twbsPagination('destroy');
        $pagination.twbsPagination($.extend({}, defaultOpts, {
            startPage: (currentPage != null) ? currentPage : 0,
            totalPages: totalPages,
            initiateStartPageClick: false,
            onPageClick: function (event, page) {
                getUsersByCompanyName(page);
            }
        }));

        var table = $('#table-body');
        table.find('tr').remove();
        $(arr).each(function () {
            var user = $(this)[0];
            var roleName;
            if (user.roles[0].role == "ADMIN") {
                roleName = "Super Admin";
            } else if (user.roles[0].role == "COMPANY") {
                roleName = "Company Admin";
            }else {
                roleName = "User"
            }
            table.append('<tr onclick="showRecord(event,this)"><td>' + user.login + '</td><td>' + roleName + '</td>' +
                '<td>' + user.company.name + '</td><td>' + user.language + '</td><td></td>' +
                '<td class="cotrol-class text-right"><span id="' +
                user.login + '" data-singleton="true"' +
                ' data-toggle="edit" class="glyphicon glyphicon glyphicon-pencil user-control" ' +
                'aria-hidden="true"></span>' +
                '<td class="cotrol-class text-right"><span id="' + user.login + '" ' +
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
        $.ajax({
            method: "GET",
            url: "/countPageUsers",
            dataType: "json",
            headers: {
                'X-CSRF-TOKEN': token
            }
        }).done(function (data) {
            var currentPage = $pagination.twbsPagination('getCurrentPage');
            $pagination.twbsPagination('destroy');
            $pagination.twbsPagination($.extend({}, defaultOpts, {
                startPage: (currentPage != null) ? currentPage : 0,
                totalPages: data,
                initiateStartPageClick: false,

            }));
        }).fail(function (data) {

        });

        var table = $('#table-body');
        table.find('tr').remove();
        $(arr).each(function () {
            var user = $(this)[0];
            var company;
            var roleName;
            if (user.role == "ADMIN") {
                roleName = "Super Admin";
            } else if (user.role == "COMPANY") {
                roleName = "Company Admin";
            }else {
                roleName = "User"
            }
            if (user.company == null) company = "No company"; else company = user.company;
            table.append('<tr onclick="showRecord(event,this)"><td>' + user.login + '</td><td>' + roleName + '</td>' +
                '<td>' + company + '</td><td>' + user.language + '</td><td></td>' +
                '<td class="cotrol-class text-right"><span id="' +
                user.login + '" data-singleton="true"' +
                ' data-toggle="edit" class="glyphicon glyphicon glyphicon-pencil user-control" ' +
                'aria-hidden="true"></span>' +
                '<td class="cotrol-class text-right"><span id="' + user.login + '" ' +
                'data-singleton="true" data-toggle="confirmation" ' +
                'class="glyphicon glyphicon-remove-circle users-control" ' +
                'aria-hidden="true"></span></td>');
        });
        manageCompany();

    });
}

function showRecord(event, record) {

    var e = event || window.event,
        elm = e.target || e.srcElement,
        allTDs = record.getElementsByTagName('td');

    while (elm.nodeName.toLowerCase() !== 'td' && elm !== record) {
        elm = elm.parentNode;
    }

    if (elm !== allTDs[5] && elm !== allTDs[6] && elm !== record) {
        window.location.href = "/records/record?username=" + $(record).find('td')[0].innerText;
    }
}

var userLogin;

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
        $('#titleForUser').text("Edit user");
        userLogin = control.attr('id');
        loadRole();
        loadLanguage();
        loadCompanies(control.attr('id'));
        edit.modal('show');
    });
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
        location.reload();
    });
}

$(document).on('hide.bs.modal', '#addCompanyModal', function () {
    userLogin = null;
    $('#titleForUser').text("Add user");
    $('#userLogin').val(null);
});

function createUser() {
    if (userLogin == null) {
        create();
    } else {
        updateUser();
    }
}

function create() {
    var message = $('#message-container');
    if ($('#userLogin').val().trim().length == 0) {
        $(message).children().remove();
        message.append("<div id='success' class='alert alert-success'><strong>Login is empty</strong></div>");
    } else if ($('#userPassword').val().trim().length == 0) {
        $(message).children().remove();
        message.append("<div id='success' class='alert alert-success'><strong>Password is empty</strong></div>");
    } else {
        $.ajax({
            method: "POST",
            url: "/user",
            data: {
                login: $('#userLogin').val(),
                password: $('#userPassword').val(),
                role: $('#userRole').val(),
                company: $('#userCompany').val(),
                language: $('#usersLanguage').val()
            },
            headers: {
                'X-CSRF-TOKEN': token
            }
        }).done(function (data) {
            location.reload();
        }).fail(function (data) {
            $(message).children().remove();
            message.append("<div id='success' class='alert alert-success'><strong>" + data.responseText + "</strong></div>");
        });
    }
}

function updateUser() {
    var message = $('#message-container');
    if ($('#userLogin').val().trim().length == 0) {
        $(message).children().remove();
        message.append("<div id='success' class='alert alert-success'><strong>Login is empty</strong></div>");
    } else {
        $.ajax({
            method: "POST",
            url: "/user/edit",
            data: {
                preLogin: userLogin,
                login: $('#userLogin').val(),
                password: $('#userPassword').val(),
                role: $('#userRole').val(),
                company: $('#userCompany').val(),
                language: $('#usersLanguage').val()
            },
            headers: {
                'X-CSRF-TOKEN': token
            }
        }).done(function (data) {
            location.reload();
        }).fail(function (data) {
            $(message).children().remove();
            message.append("<div id='success' class='alert alert-success'><strong>" + data + "</strong></div>");
        });
    }
}

function loadRole() {
    $(document).ready(function () {
        $.ajax({
            method: "POST",
            url: "/checkRole",
            headers: {
                'X-CSRF-TOKEN': token
            }
        }).done(function (data) {
            var select = $('#userRole').empty();
            if (data == "COMPANY") {
                select.append('<option value="User">User</option>');
            } else {
                $.ajax({
                    method: "GET",
                    url: "/getRoles",
                    dataType: "json",
                    headers: {
                        'X-CSRF-TOKEN': token
                    }
                }).done(function (data) {
                    var arr = data;
                    Object.keys(arr).forEach(function (key) {
                        var roleName;
                        if (arr[key] == "ADMIN") {
                            roleName = "Super Admin";
                        } else if (arr[key] == "COMPANY") {
                            roleName = "Company Admin";
                        }else {
                            roleName = "User"
                        }
                        select.append('<option value="' + arr[key] + '">' +
                            '' + roleName + '</option>');
                    });
                });
            }
        })
    });
}

function loadLanguage() {
    var select = $('#usersLanguage').empty();
    $(document).ready(function () {
        $.ajax({
            method: "POST",
            url: "/getLanguage",
            headers: {
                'X-CSRF-TOKEN': token
            }
        }).done(function (data) {
            var arr = data;
            Object.keys(arr).forEach(function (key) {
                select.append('<option value="' + arr[key] + '">' +
                    '' + arr[key] + '</option>');
            });
        });
    });
}

function loadCompanies(id) {
    var select = $('#userCompany').empty();
    $(document).ready(function () {
        $.ajax({
            method: "POST",
            url: "/checkRole",
            headers: {
                'X-CSRF-TOKEN': token
            }
        }).done(function (data) {
            if (data == "COMPANY") {
                $.ajax({
                    method: "POST",
                    url: "/checkCompany",
                    headers: {
                        'X-CSRF-TOKEN': token
                    }
                }).done(function (data) {
                    select.append('<option value="' + data + '">' + data + '</option>');
                    select.hide();
                    setSelectsForUser(id);
                });
            } else {
                $.ajax({
                    method: "GET",
                    url: "/getAllCompany",
                    dataType: "json",
                    headers: {
                        'X-CSRF-TOKEN': token
                    }
                }).done(function (data) {
                    var arr = data;
                    select.append('<option value="Non Company">Non Company</option>');
                    $(arr).each(function () {
                        var company = $(this)[0];
                        select.append('<option value="' + company.name + '">' +
                            '' + company.name + '</option>');

                    });
                    setSelectsForUser(id);
                });
            }
        });
    });
}

function setSelectsForUser(id) {
    $.ajax({
        method: "POST",
        url: "/getUser",
        data: {login: id},
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        $('#userRole').find('option[value="' + data.role + '"]').attr('selected', 'selected');
        $('#userCompany').find('option[value="' + data.company + '"]').attr('selected', 'selected');
        $('#usersLanguage').find('option[value="' + data.language + '"]').attr('selected', 'selected');
    });
}