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
        if (data == "COMPANY") {
            $('#companyLink').hide();
            if($('#statusNote')!=null){
                $('#statusNote').hide();
            }
        }
        if(data=="USER"){
            $('#companyLink').hide();
            $('#userLink').hide();
            if($('#statusNote')!=null){
                $('#statusNote').hide();
            }
        }
    })
});
