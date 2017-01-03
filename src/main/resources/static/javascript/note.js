/**
 * Created by jdroidcoder on 03.01.2017.
 */

var token = $("meta[name='_csrf']").attr("content");

$(document).ready(function () {
    loadNote();
});

function showNote() {
    // var note_control = $('#note');
    // $('#titleRecord').val($('#titleNote').val());
    // $('#typeRecord').val($('#typeNote').val());
    // loadNote($(record).find('td')[0].innerText);
    // note_control.modal('show');
}

function loadNote() {
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
            var record = $(this)[0];
            console.log(record);
            table.append('<tr><td>' + record.description + '</td>'+
                '<td>'+ record.conclusion + '</td>'+
                '<td>'+ record.keywords + '</td>'+
                '<td>'+ record.subject + '</td>'+
                '<td>'+ record.subSubject + '</td>'+
                '<td>'+ record.country + '</td>'+
                '<td>'+ record.language + '</td>'+
                '<td>'+ record.status + '</td>'+
                '</td><td></td><td class="text-right"><span id=' + record.title + ' data-singleton="true" data-toggle="confirmation" class="glyphicon glyphicon-remove-circle records-control" aria-hidden="true"></span></td></tr>');
        });
    });
}

function createNote() {
    $.ajax({
        method: "POST",
        url: "/records/note/add",
        data: {
            titleRecord: $('#titleNote').val(),
            description: $('#descriptionNote').val(),
            conclusion:$('#conclusionNote').val(),
            keywords:$('#keywordsNote').val(),
            subject:$('#subjectNote').val(),
            subSubject:$('#subSubjectNote').val(),
            country:$('#countryNote').val(),
            language:$('#languageNote').val()
        },
        headers: {
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