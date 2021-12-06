$(function () {
    $('button').click(function () {

        var para={
            tell:$("#tell").val(),
            password:$("#password").val()
        };

        $.ajax({
            url:"/user/login/check",
            type:"Post",
            data: para,
            datatype:"HTML",
            success: function (data) {
                eval(data);
            }
        })
    });

});