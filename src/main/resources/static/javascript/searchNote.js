var token = $("meta[name='_csrf']").attr("content");

var defaultOpts = {
    totalPages: 20,
    visiblePages: 7,
    onPageClick: function (event, page) {
        getRecords(page);
    }
};

$(document).ready(function () {
    checkUser();
    
    var type = $('#search-type').html();
    if(type == "all"){
        searchInAllNotes();
    } else {
        searchInRecord();
    }
});

function searchInRecord() {
    $.ajax({
        method: "POST",
        url: "/notes/search",
        data: { text: $('#keyword').html()},
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
            table.append('<tr onclick="loadPreNote(event,this,' + $(this)[0].id + ');"><td>' + note.description.slice(0, 8) + '</td>' +
                '<td>' + note.conclusion.slice(0, 8) + '</td>' +
                '<td>' + note.keywords.slice(0, 8) + '</td>' +
                '<td>' + note.subject.slice(0, 8) + '</td>' +
                '<td>' + note.subSubject.slice(0, 8) + '</td>' +
                '<td>' + note.treatment + '</td>' +
                '<td>' + note.country.slice(0, 8) + '</td>' +
                '<td>' + note.language.slice(0, 8) + '</td>' +
                '<td>' + note.status + '</td>' +
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

function searchInAllNotes() {
    $.ajax({
        method: "GET",
        url: "/notes/search",
        data: { keyword: $('#keyword').html()},
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
            table.append('<tr onclick="loadPreNote(event,this,' + $(this)[0].id + ');"><td>' + note.description.slice(0, 8) + '</td>' +
                '<td>' + note.conclusion.slice(0, 8) + '</td>' +
                '<td>' + note.keywords.slice(0, 8) + '</td>' +
                '<td>' + note.subject.slice(0, 8) + '</td>' +
                '<td>' + note.subSubject.slice(0, 8) + '</td>' +
                '<td>' + note.treatment + '</td>' +
                '<td>' + note.country.slice(0, 8) + '</td>' +
                '<td>' + note.language.slice(0, 8) + '</td>' +
                '<td>' + note.status + '</td>' +
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

var editId;
var isEdit = false;

function loadPreNote(event, note, id) {
    var e = event || window.event,
        elm = e.target || e.srcElement,
        allTDs = note.getElementsByTagName('td');

    while (elm.nodeName.toLowerCase() !== 'td' && elm !== note) {
        elm = elm.parentNode;
    }

    if (elm !== allTDs[11] && elm !== allTDs[12] && elm !== note) {
        editId = id;
        isEdit = true;
        loadSubject();
        loadSubSubject();
        loadStatus();
        loadTreatment();
        loadNote();
        $('#submitButton').hide();
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
        loadStatus();
        loadTreatment();
        loadNote();
        disableFields(false);
        edit.modal('show');
    });
}

$(document).on('hide.bs.modal', '#creteNote', function () {
    editId = null;
    isEdit = false;
    $('#titleFoNote').text("Creating new note");
    $('#submitButton').show();
    $('#descriptionNote').val("");
    $('#conclusionNote').val(null);
    $('#keywordsNote').val(null);
    $('#subjectNote').val(null);
    $('#subSubjectNote').val(null);
    $('#countryNote').val(null);
    $('#languageNote').val(null);
    $('#treatmentNote').val(null);
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
        var select = $('#statusNote').empty();
        select.append('<option value="' + data.status + '">' +
            '' + data.status + '</option>');
        if (!isEdit) {
            $.ajax({
                method: "POST",
                url: "/checkRole",
                headers: {
                    'X-CSRF-TOKEN': token
                }
            }).done(function (data) {
                console.log(data);
                if (data == "ADMIN") {
                    loadStatus();
                }
            });
        }
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
        location.reload();
    });
}

function createNote() {
    if (editId == null) {
        create();
        $('#creteNote').modal('hide');
    } else {
        update();
        $('#creteNote').modal('hide');
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
            language: $('#languageNote').val(),
            treatment: $('#treatmentNote').val()
        },
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        location.reload();
    }).fail(function (data) {
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
            language: $('#languageNote').val(),
            status: $('#statusNote').val(),
            treatment: $('#treatmentNote').val()
        },
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {

    }).fail(function (data) {
    });
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
        var select = $('#statusNote').empty();
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
    $('#descriptionNote').prop( "disabled", boolean );
    $('#conclusionNote').prop( "disabled", boolean );
    $('#keywordsNote').prop( "disabled", boolean );
    $('#subjectNote').prop( "disabled", boolean );
    $('#subSubjectNote').prop( "disabled", boolean );
    $('#countryNote').prop( "disabled", boolean );
    $('#languageNote').prop( "disabled", boolean );
    $('#treatmentNote').prop( "disabled", boolean );
    $('#statusNote').prop("disabled", boolean);
}