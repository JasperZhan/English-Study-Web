$(function () {
    $('button').click(function () {
        var isSavePw = $("input[type='checkbox']").is(':checked')
        if (isSavePw) {
            //添加cookie
            setCookie();
        } else {
            clearAllCookie();
        }
        var para = {
            tell: $("#tell").val(),
            password: $("#password").val()
        };

        $.ajax({
            url: "/user/login/check",
            type: "Post",
            data: para,
            datatype: "HTML",
            success: function (data) {
                eval(data);
            }
        })
    });

});

//设置cookie
function setCookie() {
    var date = new Date();
    // 设置cookie过期时间
    date.setTime(date.getTime() + 60 * 1000);
    $.cookie("tell", $("#tell").val(), {expires: date, path: '/'});
    $.cookie("password", $("#password").val(), {expires: date, path: '/'});


}

//清除所有cookie函数
function clearAllCookie() {
    var date = new Date();
    date.setTime(date.getTime() - 10000);
    var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
    console.log("需要删除的cookie名字：" + keys);
    if (keys) {
        for (var i = keys.length; i--;)
            document.cookie = keys[i] + "=undefined; expire=" + date.toGMTString() + "; path=/";
    }
}

//获取cookie
window.onload = function getCookie() {
    var tell = $.cookie("tell"); //获取cookie中的用户名
    var password = $.cookie("password"); //获取cookie中的登陆密码
    if (!tell || tell === "") {
        console.log("账户：" + tell);
    } else {
        $("#tell").val(tell);

    }
    if (!password || password === "") {
        console.log("密码：" + password);
    } else {
        //密码存在的话把密码填充到密码文本框
        //console.log("密码解密后："+$.base64.decode(pwd));
        // $("#mima").val($.base64.decode(pwd));
        //密码存在的话把“记住用户名和密码”复选框勾选住
        $("#password").val(password);
        $("input[type='checkbox']").attr("checked", "true");
    }


}