$(function () {
    $("#cet-4").click(function () {

        var para={
            book: 4
        };

        $.ajax({
            url:"/book/set",
            type:"Post",
            data: para,
            datatype:"HTML",
            success: function (data) {
                $("#exampleModal").modal('show')
            }
        })
    });

});

$(function () {
    $("#cet-6").click(function () {

        var para={
            book: 6
        };

        $.ajax({
            url:"/book/set",
            type:"Post",
            data: para,
            datatype:"HTML",
            success: function (data) {
                $("#exampleModal").modal('show')
            }
        })
    });

});