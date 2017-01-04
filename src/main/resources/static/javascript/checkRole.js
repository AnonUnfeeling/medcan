/**
 * Created by jdroidcoder on 04.01.2017.
 */
$(document).ready(function () {
    $.ajax({
        method: "POST",
        url: "/checkRole",
        headers: {
            'X-CSRF-TOKEN': token
        }
    }).done(function (data) {
        console.log(data);
        if (data == "COMPANY") {
            $('#companyLink').hide();
        }
        if(data=="USER"){
            $('#companyLink').hide();
            $('#userLink').hide();
        }
    })
});
