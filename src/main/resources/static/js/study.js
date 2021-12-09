$('#collection').bind('click', function () {
    $.ajax({
        url: 'collection/add/',
        type: 'post',
        dataType: 'text',
        data: {
            wordId: $('#word_id').text()
        },
        cache: false,
        async: true,
        success: function (data) {
            if (data == "已收藏") {
                $("#collection").addClass("disabled");
                $("#collection").empty();
                $("#collection").append("已收藏");
            } else {
                alert("收藏失败");
            }
        }
    })
})
$('#btn_know').bind('click', function () {
    $.ajax({
        url: '/study/know',
        type: 'post',
        dataType: 'text',
        data: {

        },
        cache: false,
        async: true,
        success: function (data) {
            modifyData(data)
        }
    });
});

$('#btn_vague').bind('click', function () {
    $.ajax({
        url: '/study/vague',
        type: 'post',
        dataType: 'text',
        data: {

        },
        cache: false,
        async: true,
        success: function (data) {
            modifyData(data);
        }
    });
});

$('#btn_forget').bind('click', function () {
    $.ajax({
        url: '/study/forget',
        type: 'post',
        dataType: 'text',
        data: {

        },
        cache: false,
        async: true,
        success: function (data) {
            modifyData(data);
        }
    });
});

function modifyData(data) {
    try {
        var jsonObject = JSON.parse(data);
    } catch (e) {
        $("#word_english").empty();
        $("#word_english").append("All words have been learned!");
        $("#word_chinese").empty();
        $("#word_chinese").append("当前所有单词已学习!"+" ");
        $("#word_total").empty();
        $("#word_total").append("剩余数量："+"0");
    }

    $("#word_chinese").empty();
    $("#word_chinese").append(jsonObject.word_chinese);
    $("#word_english").empty();
    $("#word_english").append(jsonObject.word_english);
    $("#word_total").empty();
    $("#word_total").append("剩余数量："+jsonObject.word_total);
    $("#word_id").empty();
    $("#word_id").append("当前学习单词：No."+jsonObject.word_id);
    if (jsonObject.word_collect == "已收藏") {
        $("#collection").addClass("disabled");
    } else {
        $("#collection").removeClass("disabled");
    }
    $("#collection").empty();
    $("#collection").append(jsonObject.word_collect);

    console.log(jsonObject.word_status)
    switch (jsonObject.word_status) {
        // case 1: $("#progress").attr({"style": "width: 33%;"})
        // case 2: $("#progress").attr({"style": "width: 66%;"})
        // case 3: $("#progress").attr({"style": "width: 100%;"})
        case 1: $("#progress").width("33%"); break;
        case 2: $("#progress").width("66%"); break;
        case 3: $("#progress").width("100%"); break;
    }


}
// if (jsonObject.word_collect == "已收藏") {
//     if (!$("#collection").hasClass("disabled")) {
//         $("#collection").removeClass("disabled");
//         $("#collection").addClass("disabled");
//         $("#collection").empty();
//         $("#collection").append(jsonObject.word_collect);
//     }
// } else {
//     if ($("#collection").hasClass("disabled")) {
//         $("#collection").removeClass("disabled");
//         $("#collection").empty();
//         $("#collection").append(jsonObject.word_collect);
//     }
// }
//
//
// $("#collection").attr("href", "/collection/add/"+jsonObject.word_id);