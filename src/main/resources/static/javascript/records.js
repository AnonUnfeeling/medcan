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
$(document).ready(function () {
   /* $pagination =$('#pagination');
    $pagination.twbsPagination(defaultOpts);*/
});

//Get data for table
function getRecords(page) {
    $.ajax({
        method: "GET",
        url: "/records",
        data: {page: page},
        dataType: "json",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        console.log(data);
        var arr = data.content;
        var totalPages = data.totalPages;
        var currentPage = $pagination.twbsPagination('getCurrentPage');
        $pagination.twbsPagination('destroy');
        $pagination.twbsPagination($.extend({}, defaultOpts, {
            startPage: currentPage,
            totalPages: totalPages,
            initiateStartPageClick: false
        }));

        var table = $('#table-body');
        table.find('tr').remove();
        $(arr).each(function () {
            var record = $(this)[0];
            table.append('<tr><td>' + record.name + '</td><td class="text-right"><span id=' + record.name + ' data-singleton="true" data-toggle="confirmation" class="glyphicon glyphicon-remove-circle records-control" aria-hidden="true"></span></td></tr>');
        });
        manageCompany();
    }).fail(function (data) {
        console.log(data);
    });
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
        url: "/removeRecord",
        data: { companyName: control.attr('id')},
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
        url: "/makeCompany",
        data: { companyName: $('#companyName').val()},
        dataType: "json",
        headers:{
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
