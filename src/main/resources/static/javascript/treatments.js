var token = $("meta[name='_csrf']").attr("content");

$(document).ready(function () {
    getTreatment();
});

function getTreatment() {
    $.ajax({
        method: "GET",
        url: "/subjects/treatments/get",
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
                ' data-toggle="edit" class="glyphicon glyphicon glyphicon-pencil treatment-edit-control" ' +
                'aria-hidden="true"></span><td class="cotrol-class text-right"><span id="' + arr[key].name + '" ' +
                'data-singleton="true" data-toggle="confirmation" class="glyphicon glyphicon-remove-circle treatment-delete-control"' +
                ' aria-hidden="true"></span></td></tr>');

        });
        manageTreatment();
    }).fail(function (data) {

    });
}

var preTreatment;

function manageTreatment() {
    var company_control = $('.treatment-delete-control');

    $('[data-toggle=confirmation]').confirmation({
        rootSelector: '[data-toggle=confirmation]'
    });

    company_control.click(function () {
        var control = $(this);
        control.confirmation('show');
        $('#table-body').on('confirmed.bs.confirmation', deleteTreatment(control));
    });


    $('[data-toggle=confirmation]').confirmation({
        rootSelector: '[data-toggle=edit]'
    });

    var edit = $('.treatment-edit-control');
    edit.click(function () {
        var control = $(this);
        var edit = $('#editCategoryModal');
        preTreatment = control.attr('id');
        $('#titleFoCategory').text("Edit treatment");
        $('#categoryName').val(preTreatment);
        edit.modal('show');
    });
}

function deleteTreatment(control) {
    $.ajax({
        method: "DELETE",
        url: "/subjects/treatments/" + control.attr('id'),
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        location.reload();
    });
}

function editTreatment() {
    var message = $('#message-container');
    if ($('#categoryName').val().trim().length == 0) {
        $(message).children().remove();
        message.append("<div id='success' class='alert alert-success'><strong>Field is empty</strong></div>");
    } else {
        try {
            if (preTreatment.trim().length > 0) {
                console.log("preTreatment = " + preTreatment);
                $.ajax({
                    method: "POST",
                    url: "/subjects/treatments/edit",
                    data: {
                        name: $('#categoryName').val(),
                        oldName: preTreatment
                    }, headers: {
                        'X-CSRF-TOKEN': token
                    }
                }).done(function (data) {
                    $(message).children().remove();
                    $('#categoryName').val(null);
                    $('#editCategoryModal').modal('hide');
                    getTreatment();
                }).fail(function (data) {
                    $(message).children().remove();
                    message.append("<div id='error' class='alert alert-danger'><strong>" + data.responseText + "</strong></div>");
                });
            }
        } catch (err) {
            console.log("create");
            $.ajax({
                method: "POST",
                url: "/subjects/treatments",
                data: {
                    name: $('#categoryName').val()
                }, headers: {
                    'X-CSRF-TOKEN': token
                }
            }).done(function (data) {
                $(message).children().remove();
                $('#companyName').val(null);
                $('#editCategoryModal').modal('hide');
                message.append("<div id='success' class='alert alert-success'><strong>" + data + "</strong></div>");
                location.reload();
            }).fail(function (data) {
                $(message).children().remove();
                message.append("<div id='error' class='alert alert-danger'><strong>" + data.responseText + "</strong></div>");
            });
        }
    }
}
