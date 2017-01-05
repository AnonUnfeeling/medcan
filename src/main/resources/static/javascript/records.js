var token = $("meta[name='_csrf']").attr("content");

var $pagination;

//Options for pagination
var defaultOpts = {
    totalPages: 20,
    visiblePages: 7,
    onPageClick: function (event, page) {
        getRecords(page);
    }
};

//Load pagination on page loaded
function loadAllRecords() {
    $pagination = $('#pagination');
    $pagination.twbsPagination(defaultOpts);
}
function loadRecordByUser() {
    $pagination = $('#pagination');
    $pagination.twbsPagination($.extend({}, defaultOpts, {
        onPageClick: function (event, page) {
            gerRecordsByUser(page);
        }
    }));
}

function gerRecordsByUser(page) {
    $('#addRecordButton').hide();
    $.ajax({
        method: "POST",
        url: "/records/getRecordByUser",
        data: {
            userName: $('#userName').val(),
            page: page
        },
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data.content;
        var table = $('#table-body');
        var totalPages = data.totalPages;
        var currentPage = $pagination.twbsPagination('getCurrentPage');
        $pagination.twbsPagination('destroy');
        $pagination.twbsPagination($.extend({}, defaultOpts, {
            startPage: (currentPage!=null)?currentPage:0,
            totalPages: totalPages,
            initiateStartPageClick: false
        }));
        table.find('tr').remove();
        $(arr).each(function () {
            var record = $(this)[0];
            table.append('<tr onclick="showNote(event,this)"><td>' + record.title +
                '<td>' + record.type + '</td>' +
                '<td>' + record.author.login + '</td>' +
                '</td><td></td><td class="text-right"><span id="' + record.title + '" data-singleton="true" data-toggle="confirmation" class="glyphicon glyphicon-remove-circle records-control" aria-hidden="true"></span></td></tr>');
        });
        manageCompany();
    }).fail(function (data) {
        console.log(data);
    });
}

//Get data for table
function getRecords(page) {
    $.ajax({
        method: "GET",
        url: "/records/all",
        data: {page: page},
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        console.log("Get ALL:");
        console.log(data);
        var arr = data.content;

        var totalPages = data.totalPages;
        var currentPage = $pagination.twbsPagination('getCurrentPage');
        $pagination.twbsPagination('destroy');
        $pagination.twbsPagination($.extend({}, defaultOpts, {
            startPage: (currentPage!=null)?currentPage:1,
            totalPages: totalPages,
            initiateStartPageClick: false
        }));
        var table = $('#table-body');
        table.find('tr').remove();
        $(arr).each(function () {
            var record = $(this)[0];
            console.log(record);
            table.append('<tr onclick="showNote(event,this)"><td>' + record.title +
                '<td>' + record.type + '</td>' + 
                '<td>' + record.author.login + '</td>' +
                '</td><td></td><td class="text-right"><span id="' + record.title + '" data-singleton="true" data-toggle="confirmation" class="glyphicon glyphicon-remove-circle records-control" aria-hidden="true"></span></td></tr>');
        });
        manageCompany();
    }).fail(function (data) {

    });
}

function showNote(event, record) {
    var e = event || window.event,
        elm = e.target || e.srcElement,
        allTDs = record.getElementsByTagName('td');

    while (elm.nodeName.toLowerCase() !== 'td' && elm !== record) {
        elm = elm.parentNode;
    }

    if (elm !== allTDs[4] && elm !== record) {
        window.location.href = "/records/note?title=" + $(record).find('td')[0].innerText + "&type=" + $(record).find('td')[1].innerText;
    }
}

//Add delete button to last column in table
function manageCompany() {
    var record_control = $('.records-control');
    $('[data-toggle=confirmation]').confirmation({
        rootSelector: '[data-toggle=confirmation]'
    });
    record_control.click(function () {
        var control = $(this);
        control.confirmation('show');
        $('#table-body').on('confirmed.bs.confirmation', deleteRecord(control));
    });
}
//Deleting record
function deleteRecord(control) {
    $(control).parent().parent().remove();
    $.ajax({
        method: "POST",
        url: "/records/removeRecord",
        data: {recordTitle: control.attr('id')},
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        console.log(data);
    });
}

//Crete record
function createRecord() {
    var message = $('#message-container');
    $.ajax({
        method: "POST",
        url: "/records/add",
        data: {
            title: $('#title').val(),
            type: $('#type').val()
        },
        dataType: "json",
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


function loadType() {
    $.ajax({
        method: "GET",
        url: "/records/getTypeRecord",
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        var arr = data;

        var select = $('#type').empty();
        Object.keys(arr).forEach(function (key) {
            select.append('<option value="' + arr[key] + '">' +
                '' + arr[key] + '</option>');
        });
    });
}
