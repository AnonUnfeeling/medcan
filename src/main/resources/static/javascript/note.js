/**
 * Created by jdroidcoder on 03.01.2017.
 */

var token = $("meta[name='_csrf']").attr("content");

$(document).ready(function () {
    checkUser();
    loadRecord();
    loadNotes();
});

function checkUser() {
    $.ajax({
        method: "GET",
        url: "/records/getRecord",
        data: {recordName: $('#titleNote').val()},
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        if (data == false) {
            $('#addNoteButton').hide();
            $('#submitButton').hide();
        }
    });
}

function loadNotes() {
    $.ajax({
        method: "GET",
        url: "/records/notes",
        data: {title: $('#titleNote').val()},
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data;
        var table = $('#table-body');
        table.find('tr').remove();
        $(arr).each(function () {
            var note = $(this)[0];
            table.append('<tr onclick="loadPreNote(event,this,' + $(this)[0].id + ');"><td>' + note.title.slice(0, 8) + '</td><td>' + note.description.slice(0, 8) + '</td>' +
                '<td>' + note.conclusion.slice(0, 8) + '</td>' +
                '<td>' + note.keywords.slice(0, 8) + '</td>' +
                '<td>' + note.subject.slice(0, 8) + '</td>' +
                '<td>' + note.subSubject.slice(0, 8) + '</td>' +
                '<td>' + note.treatment + '</td>' +
                '<td>' + note.updateDate.dayOfMonth + ' ' + note.updateDate.month +
                ' ' + note.updateDate.year +
                '</td><td></td><td class="text-right"><span id="' +
                note.id + '" data-singleton="true"' +
                ' data-toggle="edit" class="glyphicon glyphicon glyphicon-pencil user-control" ' +
                'aria-hidden="true"></span>' +
                '<td class="text-right"><span id="' + note.id + '" ' +
                'data-singleton="true" data-toggle="confirmation" ' +
                'class="glyphicon glyphicon-remove-circle users-control" ' +
                'aria-hidden="true"></span></td></tr>');
        });
        manageCompany();
    });
}

var editId;
var isEdit = false;

function loadPreNote(event, note, id) {
    var e = event || window.event,
        elm = e.target || e.srcElement,
        allTDs = note.getElementsByTagName('td');

    while (elm.nodeName.toLowerCase() !== 'td' && elm !== note) {
        elm = elm.parentNode;
    }

    if (elm !== allTDs[8] && elm !== allTDs[9] && elm !== note) {
        editId = id;
        isEdit = true;
        loadSubject();
        loadSubSubject();
        // loadStatus();
        loadTreatment();
        loadNote();
        $('#submitButton').hide();
        $('#languageNote').show();
        document.getElementById('createCategory').style.display = 'none';
        document.getElementById('createCategory').nextElementSibling.style.display = 'none';
        document.getElementById('createCategory').nextElementSibling.nextElementSibling.style.display = 'none';
        $('#titleFoNote').text("Details");
        var edit = $('#creteNote');
        disableFields(true);
        edit.modal('show');
    }
}

function manageCompany() {
    var record_control = $('.users-control');
    $('[data-toggle=confirmation]').confirmation({
        rootSelector: '[data-toggle=confirmation]'
    });
    record_control.click(function () {
        var control = $(this);
        control.confirmation('show');
        $('#table-body').on('confirmed.bs.confirmation', deleteNote(control));
    });

    $('[data-toggle=confirmation]').confirmation({
        rootSelector: '[data-toggle=edit]'
    });

    var edit = $('.user-control');
    edit.click(function () {
        var edit = $('#creteNote');
        var control = $(this);
        editId = control.attr('id');
        $('#titleFoNote').text("Edit note");
        loadSubject();
        loadSubSubject();
        $('#languageNote').hide();
        // loadStatus();
        loadTreatment();
        loadNote();
        disableFields(false);
        edit.modal('show');
    });
}

$(document).on('hide.bs.modal', '#creteNote', function () {
    editId = null;
    isEdit = false;
    document.getElementById('createCategory').style.display = 'initial';
    document.getElementById('createCategory').nextElementSibling.style.display = 'initial';
    document.getElementById('createCategory').nextElementSibling.nextElementSibling.style.display = 'initial';
    $('#titleFoNote').text("Creating new note");
    $('#submitButton').show();
    $('#descriptionNote').val("");
    $('#conclusionNote').val(null);
    $('#keywordsNote').val(null);
    $('#subjectNote').val(null);
    $('#subSubjectNote').val(null);
    $('#countryNote').val(null);
    $('#treatmentNote').val(null);
    $('#titleForNote').val(null);
    disableFields(false);
});

function loadNote() {
    $.ajax({
        method: "POST",
        url: "/records/note/get",
        data: {id: editId},
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        $('#descriptionNote').val(data.description);
        $('#conclusionNote').val(data.conclusion);
        $('#keywordsNote').val(data.keywords);
        $('#subjectNote').val(data.subject);
        $('#subSubjectNote').val(data.subSubject);
        $('#countryNote').val(data.country);
        $('#languageNote').val(data.language);
        $('#treatmentNote').val(data.treatment);
        $('#titleForNote').val(data.title);
    });
}

//Deleting record
function deleteNote(control) {
    $(control).parent().parent().remove();
    $.ajax({
        method: "POST",
        url: "/records/note/remove",
        data: {id: control.attr('id').toString()},
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        location.reload();
    });
}

function createNote() {
    if (editId == null) {
        create();
    } else {
        update();
    }
}

function create() {
    var message = $('#message-container');
    if ($('#titleForNote').val().trim().length !== 0) {
        $.ajax({
            method: "POST",
            url: "/records/note/add",
            data: {
                titleRecord: $('#titleNote').val(),
                description: $('#descriptionNote').val(),
                conclusion: $('#conclusionNote').val(),
                keywords: $('#keywordsNote').val(),
                subject: $('#subjectNote').val(),
                subSubject: $('#subSubjectNote').val(),
                country: $('#countryNote').val(),
                treatment: $('#treatmentNote').val(),
                titleForNote: $('#titleForNote').val()
            },
            headers: {
                'X-CSRF-TOKEN': token
            }
        }).done(function (data) {
            $(message).children().remove();
            location.reload();
        });
    } else {
        $(message).children().remove();
        message.append("<div id='error' class='alert alert-danger'><strong>Error! </strong>Title is empty</div>");
    }
}

function update() {
    var message = $('#message-container');
    if ($('#titleForNote').val().trim().length !== 0) {
        $.ajax({
            method: "POST",
            url: "/records/note/edit",
            data: {
                id: editId,
                titleRecord: $('#titleNote').val(),
                description: $('#descriptionNote').val(),
                conclusion: $('#conclusionNote').val(),
                keywords: $('#keywordsNote').val(),
                subject: $('#subjectNote').val(),
                subSubject: $('#subSubjectNote').val(),
                country: $('#countryNote').val(),
                treatment: $('#treatmentNote').val(),
                titleForNote: $('#titleForNote').val()
            },
            headers: {
                'X-CSRF-TOKEN': token
            }
        }).done(function (data) {
            location.reload();
        });
    } else {
        $(message).children().remove();
        message.append("<div id='error' class='alert alert-danger'><strong>Error! </strong>Title is empty</div>");
    }
}

function loadStatus() {
    $.ajax({
        method: "GET",
        url: "/notes/statuses",
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data;
        var select = $('#endStatus').empty();
        Object.keys(arr).forEach(function (key) {
            select.append('<option value="' + arr[key] + '">' +
                '' + arr[key] + '</option>');
        });
    });
}

function loadSubject() {
    $.ajax({
        method: "GET",
        url: "/getSubject",
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data;
        var select = $('#subjectNote').empty();
        Object.keys(arr).forEach(function (key) {
            select.append('<option value="' + arr[key].name + '">' +
                '' + arr[key].name + '</option>');
        });
    });
}

function loadSubSubject() {
    $.ajax({
        method: "GET",
        url: "/getSubSubject",
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data;
        var select = $('#subSubjectNote').empty();
        Object.keys(arr).forEach(function (key) {
            select.append('<option value="' + arr[key].name + '">' +
                '' + arr[key].name + '</option>');
        });
    });
}

function loadTreatment() {
    $.ajax({
        method: "GET",
        url: "/getTreatment",
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data;
        var select = $('#treatmentNote').empty();
        Object.keys(arr).forEach(function (key) {
            select.append('<option value="' + arr[key] + '">' +
                '' + arr[key] + '</option>');
        });
    });
}

function disableFields(boolean) {
    $('#descriptionNote').prop("disabled", boolean);
    $('#conclusionNote').prop("disabled", boolean);
    $('#keywordsNote').prop("disabled", boolean);
    $('#subjectNote').prop("disabled", boolean);
    $('#subSubjectNote').prop("disabled", boolean);
    $('#countryNote').prop("disabled", boolean);
    $('#languageNote').prop("disabled", boolean);
    $('#treatmentNote').prop("disabled", boolean);
    $('#titleForNote').prop("disabled", boolean);
}

function createEndReview() {
    var message = $('#message-container');
    if ($('#endDescription').val().trim().length !== 0 && $('#endConclusion').val().trim().length !== 0) {
        $.ajax({
            method: "POST",
            url: "/records/edit",
            data: {
                title: $('#titleNote').val(),
                type: $('#typeNote').val(),
                endDescription: $('#endDescription').val(),
                endConclusion: $('#endConclusion').val(),
                status: $('#endStatus').val(),
                country: $('#endCountry').val()
            },
            headers: {
                'X-CSRF-TOKEN': token
            }
        }).done(function (data) {
            location.reload();
        }).fail(function (data) {
            $(message).children().remove();
            message.append("<div id='error' class='alert alert-danger'><strong>Error! </strong>" + data.responseText + "</div>");
        });
    } else {
        $(message).children().remove();
        message.append("<div id='error' class='alert alert-danger'><strong>Error! </strong>Fields cannot be empty</div>");
    }
}

function loadRecord() {
    $.ajax({
        method: "GET",
        url: "/records/getRecordDetails",
        data: {
            recordName: $('#titleNote').val()
        },
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var header = $('#noteHeader');
        try {
            if (data.endDescription.trim().length > 0)
                header.append("<h4>End description:<label>" + data.endDescription + "</label></h4>");
        } catch (err) {
        }
        try {
            if (data.endConclusion.trim().length > 0)
                header.append("<h4>End conclusion: <label>" + data.endConclusion + "</label></h4>");
        } catch (err) {
        }
        try {
            if (data.country.trim().length > 0)
                header.append("<h4>Country: <label>" + data.country + "</label></h4>");
        } catch (err) {
        }
        try {
            if (data.status.trim().length > 0)
                header.append("<h4>Status: <label>" + data.status + "</label></h4>");
        } catch (err) {
        }
    });
}

function loadRecordEndSetData() {
    $.ajax({
        method: "GET",
        url: "/records/getRecordDetails",
        data: {
            recordName: $('#titleNote').val()
        },
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        $('#endDescription').val(data.endDescription);
        $('#endConclusion').val(data.endConclusion);
        $('#endCountry').val(data.country);
        $('#endStatus').val(data.status);
    });
}