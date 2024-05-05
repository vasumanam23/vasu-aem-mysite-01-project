$(document).ready(function() {
    $("#submitButton").click(function() {
        $.ajax({
            type: "GET",
            url: "/bin/gorest/data",
            success: function(data) {
                if (typeof data === 'object') {
                    data = JSON.stringify(data);
                }
                $("#responseData").text(data);
            },
            error: function(xhr, status, error) {
                console.error("Error occurred:", error);
            }
        });
    });
});
