var InterValObj; //timer变量，控制时间
var count = 60; //间隔函数，1秒执行
var curCount;//当前剩余秒数
// var tellStatus = 0;
// var passwordStatus = 0;
// var passwordConfirmStatus = 0;
// var verificationCodeStatus = 0;
$('#get_verification_code').on('click', function () {
    const data = {
        tell: $("#tell").val(),
        // password: $("#password").text(),
        // password_confirm:$("#password_confirm").text(),
    };
    console.log(data.tell)
    if (data.tell == null || data.tell === "") {
        alert("请先输入手机号噢");
        return;
    }
    $.ajax({
        //跳转 url
        url: "/user/register/code",
        type: "Post",
        data: data,
        datatype: "HTML",
        success: function (data) {
            alert(data)
        }
    })
    curCount = count;
    //设置button效果，开始计时
    $("#get_verification_code").attr("disabled", "disabled");
    $("#get_verification_code").addClass('disable');
    InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
});

function SetRemainTime() {
    if (curCount == 0) {
        window.clearInterval(InterValObj);//停止计时器
        $("#get_verification_code").removeAttr("disabled");//启用按钮
        $("#get_verification_code").removeClass('disable');
        $("#get_verification_code").text("重新获取");
    } else {
        curCount--;
        $("#get_verification_code").text("" + curCount + "s后获取");
    }
}

$('#register').on('click', function () {

    if (enableRegister() === false) {
        return
    }

    const data = {
        tell: $("#tell").val(),
        password: $("#password").val(),
        code: $("#verification_code").val()
    };

    $.ajax({
        //跳转 url
        url: "/user/register/check",
        type: "Post",
        data: data,
        datatype: "HTML",
        success: function (data) {
            eval(data)
        }
    })

});

function enableRegister() {
    // if (tellStatus === 1 &&
    //     passwordStatus === 1 &&
    //     passwordConfirmStatus === 1 &&
    //     verificationCodeStatus === 1) {
    //     $("#register").removeAttr("disabled");//启用按钮
    //     $("#register").removeClass('disable');
    // } else {
    //     $("#register").attr("disabled", "disabled");
    //     $("#register").addClass('disable');
    // }
    var tellReg=/^[1][3,4,5,7,8][0-9]{9}$/;
    var passwordReg1 = /^\w{8,20}$/
    var passwordReg2 = /.*\d+.*/
    var passwordReg3 = /.*[a-zA-Z]+.*/
    var codeReg = /^\d{6}$/

    if (!tellReg.test($("#tell").val())) {
        alert("手机号码格式不正确")
        return
    } else if (!passwordReg1.test($("#password").val())) {
        alert("密码长度需在8-20位之间")
    }else if (!passwordReg2.test($("#password").val())) {
        alert("密码需包含数字")
    }else if (!passwordReg3.test($("#password").val())) {
        alert("密码需包含字母")
    }else if (!($("#password").val() === $("#password_confirm").val())) {
        alert("两次输入密码不一致")
    }else if (!codeReg.test($("#verification_code").val())) {
        alert("验证码为6位数字")
    }else {
        return true;
    }
    return false;
}

// $("#tell").blur(function (){
//
//     if (reg.test($(this).val())) {
//         tellStatus = 1;
//         enableRegister()
//     } else {
//         alert("手机号码格式不正确")
//         return
//     }
// })
//
// $("#password").blur(function (){
//     var reg1 =
//     var reg2 = /.*\d+.*/
//     var reg3 = /.*[a-zA-Z]+.*/
//     if (!reg1.test($(this).val())) {
//         // $(this).attr('data-bs-container', 'body')
//         // $(this).attr('data-bs-toggle', 'popover')
//         // $(this).attr('data-bs-placement', 'bottom')
//         // $(this).attr('data-bs-content', '密码长度需在8-20位之间')
//         alert("密码长度需在8-20位之间")
//         return
//     } else if (!reg2.test($(this).val())){
//         // this.attr('data-bs-container', 'body')
//         // this.attr('data-bs-toggle', 'popover')
//         // this.attr('data-bs-placement', 'bottom')
//         // this.attr('data-bs-content', '密码需包含数字')
//         alert("密码需包含数字")
//         return;
//     } else if (!reg3.test(this.val())) {
//         // this.attr('data-bs-container', 'body')
//         // this.attr('data-bs-toggle', 'popover')
//         // this.attr('data-bs-placement', 'bottom')
//         // this.attr('data-bs-content', '密码需包含字母')
//         alert("密码需包含字母")
//         return;
//     }
//
//     passwordStatus = 1
//     enableRegister()
// })
//
// $("#password_confirm").blur(function () {
//     if ($(this).val() === $("#password").val()) {
//         passwordConfirmStatus = 1
//         enableRegister()
//     } else {
//         alert("两次输入密码不一致")
//     }
// })
//
// $("#verification_code").blur(function () {
//     var reg = /^\d{6}$/
//     if (reg.test(this.val())) {
//         verificationCodeStatus = 1
//         enableRegister()
//     } else {
//         alert("验证码胃6位数字")
//     }
// })

