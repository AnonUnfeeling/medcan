/**
 * Created by jdroidcoder on 03.01.2017.
 */

var token = $("meta[name='_csrf']").attr("content");

$(document).ready(function () {
    checkUser();
    loadRecord();
    loadNotes();
    searchFilter();
});

function searchFilter() {
    setFilterSettingsToDefault();
    $('#dropdown-toggle').on('click', function () {
        $('#dropdown').toggle();
    });
    $('#cancel').on('click', function () {
        $('#dropdown').hide();
        setFilterSettingsToDefault();
    });
    $('#submit').on('click', function () {
        saveFilterSettings();
        $('#search-form').submit();
    });

    $('#conc-filter').on('change', function () {
        if ($(this).is(':checked') == true) {
            $(this).val('true');
        } else $(this).val('false');
    });

    $('#desc-filter').on('change', function () {
        if ($(this).is(':checked') == true) {
            $(this).val('true');
        } else $(this).val('false');
    });

    loadSubjects();
    loadTreatments();

    $('#category').on('change', function () {
        var category = $(this).val();
        console.log(category);
        if (category == 'All') {
            var select = $('#subCategory').empty();
            select.append("<option value=All>All</option>");
        } else {
            $.ajax({
                method: "GET",
                url: "/subjects/" + $("#category").val(),
                dataType: "json",
                headers: {
                    'X-CSRF-TOKEN': token
                }
            }).done(function (data) {
                var arr = data;
                loadTreatment();
                var select = $('#subCategory').empty();
                select.append("<option value=All>All</option>");
                Object.keys(arr).forEach(function (key) {
                    select.append('<option value="' + arr[key].name + '">' +
                        '' + arr[key].name + '</option>');
                });
            });
        }
    });
}

function loadSubjects() {
    $.ajax({
        method: "GET",
        url: "/subjects",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var categories = data;
        //loadSubSubject(categories[0].name);
        var select = $('#category').empty();
        select.append("<option value=All>All</option>");
        Object.keys(categories).forEach(function (key) {
            select.append('<option class="added-option" value="' + categories[key].name + '">' +
                '' + categories[key].name + '</option>');
        });
    });
}

function loadTreatments() {
    $.ajax({
        method: "GET",
        url: "/subjects/treatments/get",
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data;
        var select = $('#treatments').empty();
        select.append("<option value=All>All</option>");
        Object.keys(arr).forEach(function (key) {
            select.append('<option value="' + arr[key].name + '">' +
                '' + arr[key].name + '</option>');
        });
    });
}

function saveFilterSettings() {
    $("#cat").val($("#category").val());
    $("#subCat").val($("#subCategory").val());
    $("#treat").val($("#treatments").val());
    $('#conc').val($('#conc-filter').val());
    $('#desc').val($('#desc-filter').val());
}
function setFilterSettingsToDefault() {
    $("#cat").val('All');
    $("#subCat").val('All');
    $("#treat").val('All');
    $('#conc').val('true');
    $('#desc').val('true');
}
//SCode 
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
                '</td><td></td><td class="cotrol-class text-right"><span id="' +
                note.id + '" data-singleton="true"' +
                ' data-toggle="edit" class="glyphicon glyphicon glyphicon-pencil user-control" ' +
                'aria-hidden="true"></span>' +
                '<td class="cotrol-class text-right"><span id="' + note.id + '" ' +
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
        loadNote();
        $('#submitButton').hide();
        $('#languageNote').show();
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
        $('#languageNote').hide();
        loadSubject();
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
    $('#subjectNote').val(null);
    $('#subSubjectNote').val(null);
    $('#countryNote').val(null);
    $('#treatmentNote').val(null);
    $('#titleForNote').val(null);
    $('#keywordsNote').importTags('');
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
        $('#keywordsNote').importTags(data.keywords);
        $('#descriptionNote').val(data.description);
        $('#conclusionNote').val(data.conclusion);
        //$('#keywordsNote').val(data.keywords);
        $('#titleForNote').val(data.title);
        $('#countryNote').val(data.country);
        $('#languageNote').val(data.language);
        if (!isEdit) {
            loadSubSubject(data.subject, data.subSubject);
            loadTreatment(data.treatment);
        } else {
            var select = $('#subjectNote').empty();
            select.append('<option value="' + data.subject + '">' +
                '' + data.subject + '</option>');
            var select = $('#subSubjectNote').empty();
            select.append('<option value="' + data.subSubject + '">' +
                '' + data.subSubject + '</option>');
            var select = $('#treatmentNote').empty();
            select.append('<option value="' + data.treatment + '">' +
                '' + data.treatment + '</option>');
        }
        var sSubj;
        if (data.subSubject == "Select sub-category") {
            sSubj = "";
        } else {
            sSubj = data.subSubject;
        }
        $('#waySubCat').text(null);
        $('#waySubCat').text(sSubj);
        var subj;
        if (data.subject == "Select category") {
            subj = "";
        } else {
            subj = data.subject;
        }
        $('#wayCat').text(null);
        $('#wayCat').text(subj);
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
        var subj;
        var sSubj;
        var tr;
        if ($('#subjectNote').val() == "Select category") {
            subj = "";
        } else {
            subj = $('#subjectNote').val();
        }
        if ($('#subSubjectNote').val() == "Select sub-category") {
            sSubj = "";
        } else {
            sSubj = $('#subSubjectNote').val();
        }
        if ($('#treatmentNote').val() == "Select treatment") {
            tr = "";
        } else {
            tr = $('#treatmentNote').val();
        }
        $.ajax({
            method: "POST",
            url: "/records/note/add",
            data: {
                titleRecord: $('#titleNote').val(),
                description: $('#descriptionNote').val(),
                conclusion: $('#conclusionNote').val(),
                keywords: $('#keywordsNote').val(),
                subject: subj,
                subSubject: sSubj,
                country: $('#countryNote').val(),
                treatment: tr,
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
        var subj;
        var sSubj;
        var tr;
        if ($('#subjectNote').val() == "Select category") {
            subj = "";
        } else {
            subj = $('#subjectNote').val();
        }
        if ($('#subSubjectNote').val() == "Select sub-category") {
            sSubj = "";
        } else {
            sSubj = $('#subSubjectNote').val();
        }
        if ($('#treatmentNote').val() == "Select treatment") {
            tr = "";
        } else {
            tr = $('#treatmentNote').val();
        }
        $.ajax({
            method: "POST",
            url: "/records/note/edit",
            data: {
                id: editId,
                titleRecord: $('#titleNote').val(),
                description: $('#descriptionNote').val(),
                conclusion: $('#conclusionNote').val(),
                keywords: $('#keywordsNote').val(),
                subject: subj,
                subSubject: sSubj,
                country: $('#countryNote').val(),
                treatment: tr,
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
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var categories = data;
        loadSubSubject(categories[0].name.toString());
        $('#wayCat').text(null);
        var select = $('#subjectNote').empty();
        select.append('<option selected="selected">Select category</option>');
        Object.keys(categories).forEach(function (key) {
            select.append('<option value="' + categories[key].name + '">' +
                '' + categories[key].name + '</option>');
        });
    });
}

function loadSubSubject(categoryName, sb) {
    $.ajax({
        method: "GET",
        url: "/subjects/" + categoryName.toString(),
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data;
        var select = $('#subSubjectNote').empty();
        $('#waySubCat').text(null);
// <<<<<<< HEAD
//         select.append('<option selected="selected">Select sub-category</option>');
// =======
        // $('#waySubCat').text(arr[0].name);
        if (sb == null) {
            select.append('<option selected="selected">Select sub-category</option>');
        } else {
            select.append('<option>Select sub-category</option>');
        }
// >>>>>>> ff57576412da5cf2579832e7edf117bb930f194d
        Object.keys(arr).forEach(function (key) {
            select.append('<option value="' + arr[key].name + '">' +
                '' + arr[key].name + '</option>');
        });
        if (sb != null) {
            $('#subjectNote').find('option[value="' + categoryName + '"]').attr('selected', 'selected');
            setTimeout(function () {
                $('#subSubjectNote').find('option[value="' + sb + '"]').attr('selected', 'selected');
                var sSubj;
                if (sb == "Select sub-category") {
                    sSubj = "";
                } else {
                    sSubj = sb;
                }
                $('#waySubCat').text(null);
                $('#waySubCat').text(sSubj);
                var subj;
                if (categoryName == "Select category") {
                    subj = "";
                } else {
                    subj = categoryName;
                }
                $('#wayCat').text(null);
                $('#wayCat').text(subj);
            }, 144);
            console.log(sb);
        }
    });
}

function loadTreatment(tr) {
    $.ajax({
        method: "GET",
        url: "/subjects/treatments/get",
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data;
        var select = $('#treatmentNote').empty();
        select.append('<option selected="selected">Select treatment</option>');
        Object.keys(arr).forEach(function (key) {
            select.append('<option value="' + arr[key].name + '">' +
                '' + arr[key].name + '</option>');
        });
        if (tr != null)$('#treatmentNote').find('option[value="' + tr + '"]').attr('selected', 'selected');
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
    $('#keywordsNote_tagsinput ').toggleClass("disabledFields",boolean);
}

function createEndReview() {
    var message = $('#message-container');
    if ($('#endDescription').val().trim().length !== 0 || $('#endConclusion').val().trim().length !== 0) {
        $.ajax({
            method: "POST",
            url: "/records/edit",
            data: {
                title: $('#titleNote').val(),
                type: $('#typeNote').val(),
                endDescription: $('#endDescription').val(),
                endConclusion: $('#endConclusion').val(),
                country: $('#endCountry').val(),
                url: $('#editUrl').val()
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
                header.append("<label>End description: </label><span>" + data.endDescription + "</span><br/>");
        } catch (err) {
        }
        try {
            if (data.endConclusion.trim().length > 0)
                header.append("<label>End conclusion: </label><span>" + data.endConclusion + "</span><br/>");
        } catch (err) {
        }
        try {
            if (data.type == "Website")
                if (data.url.trim().length > 0)
                    header.append("<label>URL: </label> <span>" + data.url + "</span><br/>");
        } catch (err) {
        }
        try {
            if (data.country.trim().length > 0)
                header.append("<label>Country: </label> <span>" + data.country + "</span><br/>");
        } catch (err) {
        }
        try {
            if (data.status.trim().length > 0)
                header.append("<label>Status: </label> <span>" + data.status + "</span><br/>");
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
        $('#editTitle').val(data.title);
        $('#editCountry').val(data.country);
        $('#editEndConclusion').val(data.endConclusion);
        $('#editEndDescription').val(data.endDescription);
        $('#endConclusion').val(data.endConclusion);
        $('#endDescription').val(data.endDescription);
        $('#endStatus').val(data.status);
        if (data.type !== "Website") {
            document.getElementById('editUrl').style.display = 'none';
        } else {
            $('#editUrl').val(data.url);
        }
    });
}

$(document).ready(function () {
    $('#subjectNote').change(function () {
        var val = $("#subjectNote option:selected").text();
        var subj;
        if (val == "Select category") {
            subj = "";
        } else {
            subj = val;
        }
        $('#wayCat').text(subj);
        loadSubSubject(val);
    });
    $('#subSubjectNote').change(function () {
        var val = $("#subSubjectNote option:selected").text();
        var sSubj;
        if (val == "Select sub-category") {
            sSubj = "";
        } else {
            sSubj = val;
        }
        $('#waySubCat').text(null);
        $('#waySubCat').text(sSubj);
    });
});

function editRecord() {
    var status;
    if (document.getElementById('endStatus').style.display == 'none') {
        status = "In review";
    } else {
        status = $('#endStatus').val();
    }
    var message = $('#edit-message-container');
    if ($('#editTitle').val().trim().length !== 0) {
        $.ajax({
            method: "POST",
            url: "/records/editRecord",
            data: {
                preRecordName: $('#titleNote').val(),
                title: $('#editTitle').val(),
                type: $('#type').val(),
                status: status,
                endDescription: $('#editEndDescription').val(),
                endConclusion: $('#editEndConclusion').val(),
                country: $('#editCountry').val(),
                url: $('#editUrl').val()
            },
            headers: {
                'X-CSRF-TOKEN': token
            }
        }).done(function (data) {
            window.location.href = "/records";
        }).fail(function (data) {
            $(message).children().remove();
            message.append("<div id='error' class='alert alert-danger'><strong>Error! </strong>" + data.responseText + "</div>");
        });
    } else {
        $(message).children().remove();
        message.append("<div id='error' class='alert alert-danger'><strong>Error! </strong>Fields cannot be empty</div>");
    }
}
var countClickIntoFailed = 0;
function clickForSortNote(title) {
    if (countClickIntoFailed == 0) {
        countClickIntoFailed = 1;
        sortByNote(title, "ASC");
    } else {
        countClickIntoFailed = 0;
        sortByNote(title, "DESC");
    }
}
//sort by title
function sortByNote(title, sortDirection) {
    $.ajax({
        method: "POST",
        url: "/records/note/sort",
        data: {
            title: $('#titleNote').val(),
            sortDirection: sortDirection,
            sortField: $(title).text().toLowerCase()
        },
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
                '</td><td></td><td class="cotrol-class text-right"><span id="' +
                note.id + '" data-singleton="true"' +
                ' data-toggle="edit" class="glyphicon glyphicon glyphicon-pencil user-control" ' +
                'aria-hidden="true"></span>' +
                '<td class="cotrol-class text-right"><span id="' + note.id + '" ' +
                'data-singleton="true" data-toggle="confirmation" ' +
                'class="glyphicon glyphicon-remove-circle users-control" ' +
                'aria-hidden="true"></span></td></tr>');
            manageRecord();
        });
    });
}
