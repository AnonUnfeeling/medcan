/**
 * Created by jdroidcoder on 04.01.2017.
 */

var token = $("meta[name='_csrf']").attr("content");

$(document).ready(function () {
    $.ajax({
        method: "POST",
        url: "/checkRole",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        if (data == "COMPANY") {
            $('#companyLink').hide();
            if ($('#endReview') != null) {
                $('#endReview').hide();
            }
        }
        if (data == "USER") {
            $('#companyLink').hide();
            $('#userLink').hide();
            if ($('#endReview') != null) {
                $('#endReview').hide();
            }
        }
    })
});
