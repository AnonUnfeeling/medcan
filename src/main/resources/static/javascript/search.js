var token = $("meta[name='_csrf']").attr("content");

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
            $('#result-container').children().remove();

            $(data).each(function () {
                console.log($(this)[0]);
            })
        });

});