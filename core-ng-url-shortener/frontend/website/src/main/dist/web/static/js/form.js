$(document).ready(function () {
    $("button#encode").on("click", function () {
        $.when($.ajax({
            url: "/ajax/encode",
            type: "POST",
            contentType: 'application/json',
            data: JSON.stringify({'url': $("#originalUrl").val()})
        })).done(function (response) {
            console.log(response.success);

            if (response.success) {
                let url = location.host + '/' + response.result;
                $("#originalUrl").val(url).select();
            } else {
                alert("An error occurred, please try again");
            }
        })
    });
})
