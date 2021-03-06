var token = $("meta[name='_csrf']").attr("content");

$(document).ready(function () {
    getCategories();
});

function getCategories() {
    $.ajax({
        method: "GET",
        url: "/subjects",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        $('#modalButton').click(function () {
            editCategory();
        });
        var arr = data;
        var table = $('#table-body');
        table.find('tr').remove();
        Object.keys(arr).forEach(function (key) {
            table.append('<tr onclick="showSubCategoryForCategory(event,this)"><td>'
                + arr[key].name + '</td><td></td><td class="cotrol-class text-right"><span id="' +
                arr[key].name + '" data-singleton="true"' +
                ' data-toggle="edit" class="glyphicon glyphicon glyphicon-pencil category-edit-control" ' +
                'aria-hidden="true"></span><td class="cotrol-class text-right"><span id="' + arr[key].name + '" ' +
                'data-singleton="true" data-toggle="confirmation" class="glyphicon glyphicon-remove-circle category-delete-control"' +
                ' aria-hidden="true"></span></td></tr>');

        });
        manageCategory();
    }).fail(function (data) {

    });
}

var preCategoryName;

function manageCategory() {
    var company_control = $('.category-delete-control');

    $('[data-toggle=confirmation]').confirmation({
        rootSelector: '[data-toggle=confirmation]'
    });

    company_control.click(function () {
        var control = $(this);
        control.confirmation('show');
        $('#table-body').on('confirmed.bs.confirmation', deleteCategory(control));
    });


    $('[data-toggle=confirmation]').confirmation({
        rootSelector: '[data-toggle=edit]'
    });

    var edit = $('.category-edit-control');
    edit.click(function () {
        var control = $(this);
        var edit = $('#editCategoryModal');
        preCategoryName = control.attr('id');
        $('#titleFoCategory').text("Edit category");
        $('#categoryName').val(preCategoryName);
        edit.modal('show');
    });
}

function editCategory() {
    if (!isSubCategory && !isTreatment) {
        var message = $('#message-container');
        if ($('#categoryName').val().trim().length == 0) {
            $(message).children().remove();
            message.append("<div id='success' class='alert alert-success'><strong>Field is empty</strong></div>");
        } else {
            try {
                if (preCategoryName.trim().length > 0) {
                    $.ajax({
                        method: "POST",
                        url: "/subjects/editCategory",
                        data: {
                            name: $('#categoryName').val(),
                            oldName: preCategoryName
                        }, headers: {
                            'X-CSRF-TOKEN': token
                        }
                    }).done(function (data) {
                        $(message).children().remove();
                        $('#categoryName').val(null);
                        location.reload();
                    }).fail(function (data) {
                        $(message).children().remove();
                        message.append("<div id='error' class='alert alert-danger'><strong>" + data.responseText + "</strong></div>");
                    });
                }
            } catch (err) {
                $.ajax({
                    method: "POST",
                    url: "/subjects",
                    data: {
                        name: $('#categoryName').val()
                    }, headers: {
                        'X-CSRF-TOKEN': token
                    }
                }).done(function (data) {
                    $(message).children().remove();
                    $('#categoryName').val(null);
                    message.append("<div id='success' class='alert alert-success'><strong>" + data + "</strong></div>");
                    location.reload();
                }).fail(function (data) {
                    $(message).children().remove();
                    message.append("<div id='error' class='alert alert-danger'><strong>" + data.responseText + "</strong></div>");
                });
            }
        }
    }
}

function deleteCategory(control) {
    $(control).parent().parent().remove();
    $.ajax({
        method: "DELETE",
        url: "/subjects/" + control.attr('id'),
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {

    });
}

var isSubCategory = false;

function showSubCategoryForCategory(event, companyName) {
    var e = event || window.event,
        elm = e.target || e.srcElement,
        allTDs = companyName.getElementsByTagName('td');

    while (elm.nodeName.toLowerCase() !== 'td' && elm !== companyName) {
        elm = elm.parentNode;
    }

    if (elm == allTDs[0] || elm == allTDs[1] && elm == companyName) {
        $('#modalButton').click(function () {
            editSubCategory();
        });
        getSubCategory($(companyName).find('td').first()[0].innerText);
    }
}

var isTreatment = false;

function getSubCategory(categoryName) {
    var message = $('#message-container');
    $(message).children().remove();
    $('#titleForCategory').text("All sub-category");
    isSubCategory = true;
    $('#titleFoCategory').html("Add sub-category");
    $('#create-category').html("Add sub-category");
    $('#categoryName').attr("placeholder", "Sub-category name");
    preCategoryName = categoryName;
    $.ajax({
        method: "GET",
        url: "/subjects/" + categoryName.toString(),
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data;
        var table = $('#table-body');
        table.find('tr').remove();
        Object.keys(arr).forEach(function (key) {
            table.append('<tr><td>'
                + arr[key].name + '</td><td></td><td class="cotrol-class text-right"><span id="' +
                arr[key].name + '" data-singleton="true"' +
                ' data-toggle="edit" class="glyphicon glyphicon glyphicon-pencil subcategory-edit-control" ' +
                'aria-hidden="true"></span><td class="cotrol-class text-right"><span id="' + arr[key].name + '" ' +
                'data-singleton="true" data-toggle="confirmation" class="glyphicon glyphicon-remove-circle subcategory-delete-control"' +
                ' aria-hidden="true"></span></td></tr>');

        });
        manageSubCategory();
    }).fail(function (data) {

    });
}

var preSubCategoryName;

function manageSubCategory() {
    var company_control = $('.subcategory-delete-control');

    $('[data-toggle=confirmation]').confirmation({
        rootSelector: '[data-toggle=confirmation]'
    });

    company_control.click(function () {
        var control = $(this);
        control.confirmation('show');
        $('#table-body').on('confirmed.bs.confirmation', deleteSubCategory(control));
    });


    $('[data-toggle=confirmation]').confirmation({
        rootSelector: '[data-toggle=edit]'
    });

    var edit = $('.subcategory-edit-control');
    edit.click(function () {
        var control = $(this);
        var edit = $('#editCategoryModal');
        preSubCategoryName = control.attr('id');
        $('#titleFoCategory').text("Edit sub-category");
        $('#categoryName').val(preSubCategoryName);
        edit.modal('show');
    });
}

function editSubCategory() {
    if (!isTreatment) {
        var message = $('#message-container');
        if ($('#categoryName').val().trim().length == 0) {
            $(message).children().remove();
            message.append("<div id='success' class='alert alert-success'><strong>Field is empty</strong></div>");
        } else {
            try {
                if (preSubCategoryName.trim().length > 0) {
                    $.ajax({
                        method: "POST",
                        url: "/subjects/sub/editSubCategory",
                        data: {
                            name: $('#categoryName').val(),
                            oldName: preSubCategoryName,
                            subject: preCategoryName
                        }, headers: {
                            'X-CSRF-TOKEN': token
                        }
                    }).done(function (data) {
                        $(message).children().remove();
                        $('#categoryName').val(null);
                        $('#editCategoryModal').modal('hide');
                        getSubCategory(preCategoryName);
                    }).fail(function (data) {
                        $(message).children().remove();
                        message.append("<div id='error' class='alert alert-danger'><strong>" + data.responseText + "</strong></div>");
                    });
                }
            } catch (err) {
                $.ajax({
                    method: "POST",
                    url: "/subjects/sub",
                    data: {
                        name: $('#categoryName').val(),
                        subject: preCategoryName
                    }, headers: {
                        'X-CSRF-TOKEN': token
                    }
                }).done(function (data) {
                    $(message).children().remove();
                    $('#categoryName').val(null);
                    $('#editCategoryModal').modal('hide');
                    message.append("<div id='success' class='alert alert-success'><strong>" + data + "</strong></div>");
                    getSubCategory(preCategoryName);
                }).fail(function (data) {
                    $(message).children().remove();
                    message.append("<div id='error' class='alert alert-danger'><strong>" + data.responseText + "</strong></div>");
                });
            }
        }
    }
}

function deleteSubCategory(control) {
    $.ajax({
        method: "DELETE",
        url: "/subjects/sub/" + control.attr('id'),
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        getSubCategory(preCategoryName);
    });
}

$(document).on('hide.bs.modal', '#editCategoryModal', function () {
    var message = $('#message-container');
    $(message).children().remove();
});