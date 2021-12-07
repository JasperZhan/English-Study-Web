$('#btn_know').bind('click', function () {
    $.ajax({
        url: '/review/know',
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
        url: '/review/vague',
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
        url: '/review/forget',
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
        $("#word_english").append("All words have been reviewed!");
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
}