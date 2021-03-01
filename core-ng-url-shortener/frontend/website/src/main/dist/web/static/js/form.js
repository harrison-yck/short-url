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
                $("div#output").append("<div>Original Url: " + $("#originalUrl") + "<br/>Shorten Url: " + response.result + "</div>");
            } else {
                alert("An error occurred, please try again");
            }
        })
    });
})
