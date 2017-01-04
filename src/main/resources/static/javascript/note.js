/**
 * Created by jdroidcoder on 03.01.2017.
 */

var token = $("meta[name='_csrf']").attr("content");

$(document).ready(function () {
    loadNotes();
});

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
            table.append('<tr><td id="desc">' + note.description + '</td>' +
                '<td id="concl">' + note.conclusion + '</td>' +
                '<td>' + note.keywords + '</td>' +
                '<td>' + note.subject + '</td>' +
                '<td>' + note.subSubject + '</td>' +
                '<td>' + note.country + '</td>' +
                '<td>' + note.language + '</td>' +
                '<td>' + note.status + '</td>' +
                '</td><td></td><td class="text-right"><span id=' +
                note.id + ' data-singleton="true"' +
                ' data-toggle="edit" class="glyphicon glyphicon glyphicon-pencil user-control" ' +
                'aria-hidden="true"></span>' +
                '<td class="text-right"><span id=' + note.id + ' ' +
                'data-singleton="true" data-toggle="confirmation" ' +
                'class="glyphicon glyphicon-remove-circle users-control" ' +
                'aria-hidden="true"></span></td>');
        });
        manageCompany();
    });
}

var editId;

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
        loadNote();
        edit.modal('show');
    });
}

$('#creteNote').on('hidden', function () {
    editId.val(null);
    console.log("yes");
    $('#descriptionNote').val("");
    $('#conclusionNote').val(null);
    $('#keywordsNote').val(null);
    $('#subjectNote').val(null);
    $('#subSubjectNote').val(null);
    $('#countryNote').val(null);
    $('#languageNote').val(null);
});

$(document.body).on('hidden.bs.modal', function () {
    $('#creteNote').removeData('bs.modal');
    console.log("yes");
    // editId.val(null);
    //
    // $('#descriptionNote').val("");
    // $('#conclusionNote').val(null);
    // $('#keywordsNote').val(null);
    // $('#subjectNote').val(null);
    // $('#subSubjectNote').val(null);
    // $('#countryNote').val(null);
    // $('#languageNote').val(null);
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
        console.log(data);
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
            language: $('#languageNote').val()
        },
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        // $(message).children().remove();
        // message.append("<div id='success' class='alert alert-success'><strong>Success!</strong>'+ data+'</div>")
    }).fail(function (data) {
        // $(message).children().remove();
        // message.append("<div id='error' class='alert alert-danger'><strong>Error!</strong>+' data +'</div>")
    });
}

function update() {
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
            language: $('#languageNote').val()
        },
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        // $(message).children().remove();
        // message.append("<div id='success' class='alert alert-success'><strong>Success!</strong>'+ data+'</div>")
    }).fail(function (data) {
        // $(message).children().remove();
        // message.append("<div id='error' class='alert alert-danger'><strong>Error!</strong>+' data +'</div>")
    });
}