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
    var title = $('#search-title').html();
    var category = $('#category').html();
    var subCategory = $('#subCategory').html();
    var treatment = $('#treatments').html();

    (category=='All')?category = null:category;
    (subCategory=='All')?subCategory = null:subCategory;
    (treatment=='All')?treatment = null:treatment;
    
    $.ajax({
        method: "POST",
        url: "/search/records/"+title+"/notes/",
        data: { text: $('#keyword').html(),
                category: category,
                treatment: treatment,
                subCategory: subCategory
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

    if (elm !== allTDs[8] && elm !== allTDs[9] && elm !== note) {
        editId = id;
        isEdit = true;
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
        $('#languageNote').hide();
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
        $('#titleForNote').val(data.title);
        $('#countryNote').val(data.country);
        $('#languageNote').val(data.language);
        var select = $('#subjectNote').empty();
        select.append('<option value="' + data.subject + '">' +
            '' + data.subject + '</option>');
        var select = $('#subSubjectNote').empty();
        select.append('<option value="' + data.subSubject + '">' +
            '' + data.subSubject + '</option>');
        var select = $('#treatmentNote').empty();
        select.append('<option value="' + data.treatment + '">' +
            '' + data.treatment + '</option>');
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
        url: "/subjects",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var categories = data;
        loadSubSubject(categories[0].name);
        var select = $('#subjectNote').empty();
        Object.keys(categories).forEach(function (key) {
            select.append('<option value="' + categories[key].name + '">' +
                '' + categories[key].name + '</option>');
        });
    });
}

function loadSubSubject(categoryName) {
    $.ajax({
        method: "GET",
        url: "/subjects/" + categoryName.toString(),
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data;
        loadTreatment(arr[0].name);
        var select = $('#subSubjectNote').empty();
        Object.keys(arr).forEach(function (key) {
            select.append('<option value="' + arr[key].name + '">' +
                '' + arr[key].name + '</option>');
        });
    });
}

function loadTreatment(subcategoryName) {
    $.ajax({
        method: "GET",
        url: "/subjects/treatments/"+subcategoryName.toString(),
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data;
        var select = $('#treatmentNote').empty();
        Object.keys(arr).forEach(function (key) {
            select.append('<option value="' + arr[key].name + '">' +
                '' + arr[key].name + '</option>');
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