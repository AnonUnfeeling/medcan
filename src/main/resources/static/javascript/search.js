var token = $("meta[name='_csrf']").attr("content");

var defaultOpts = {
    totalPages: 20,
    visiblePages: 7,
    onPageClick: function (event, page) {
        getRecords(page);
    }
};

$(document).ready(function () {
        $.ajax({
            method: "GET",
            url: "/records/search",
            data: { keyword: $('#keyword').html()},
            dataType: "json",
            headers: {
                'X-CSRF-TOKEN': token
            }
        }).done(function (data) {
            var table = $('#table-body');
            table.find('tr').remove();
            $(data).each(function () {
                var record = $(this)[0];
                table.append('<tr onclick="showNote(event,this)"><td>' + record.title +
                    '<td>' + record.type + '</td>' +
                    '<td>' + record.author.login + '</td>' +
                    '</td><td></td><td class="text-right"><span id=' + record.title + ' data-singleton="true" data-toggle="confirmation" class="glyphicon glyphicon-remove-circle records-control" aria-hidden="true"></span></td></tr>');
            });
            manageCompany();
        });

});

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