
$(".answerWrite input[type=submit]").click(addAnswer);

function addAnswer(e) {
    e.preventDefault(); //submit이 자동으로 동작하는 것을 막는다.
    //form 데이터들을 자동으로 묶어준다.
    var queryString = $("form[name=answer]").serialize();

    console.log(queryString);

    $.ajax({
        type: 'post',
        url: '/api/qna/addAnswer',
        data: queryString,
        dataType: 'json',
        error: onError,
        success: onSuccess
    });
}

function onSuccess(json, status) {
    console.log(json);
    var answerTemplate = $("#answerTemplate").html();
    var template = answerTemplate.format(json.writer, new Date(json.createdDate), json.contents, json.answerId, json.answerId);
    $(".qna-comment-slipp-articles").prepend(template);
}

function onError(xhr, status) {
    alert("error");
}

// 이벤트 리스너
// qna-comment 클래스 내부에서 form-delete 폼의 submit이라는 이벤트가 발생했을 때
// deleteAnswer 함수를 호출한다.(동적으로 설정됨)
$(".qna-comment").on("submit", ".form-delete", deleteAnswer);

function deleteAnswer(e) {
    // submit 이벤트는 기본 동작이 페이지를 새로고침하거나 새로운 페이지로 이동한다.
    // 이를 막아주는 역할을 한다.
    e.preventDefault();

    // 이벤트가 발생한 요소
    var deleteBtn = $(this);
    // 가장 가까운 form 요소를 찾고, 그 폼 내부의 name=answerId를 가진 입력 필드값을 가져온다.
    var queryString = deleteBtn.closest("form").find("input[name=answerId]").val();

    // ajax요청 진행
    $.ajax({
        type: 'delete',
        url: '/api/qna/deleteAnswer',
        contentType: 'application/json',
        data: JSON.stringify({ answerId : queryString}),
        dataType: 'json',
        // error 처리 콜백 함수
        error: function(xhr, status) {
            alert("error")
        },
        // success 처리 콜백 함수
        success: function(json, status) {
            console.log(json.status);
            console.log(json);
            if (json.status) {
                deleteBtn.closest('article').remove();
            }
        }
    })
}

String.prototype.format = function() {
    var args = arguments;
    console.log("format....");
    console.log(args);
    return this.replace(/{(\d+)}/g, function(match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};