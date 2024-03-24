// $(".qna-comment").on("click", ".answerWrite input[type=submit]", addAnswer);
$(".answerWrite input[type=submit]").click(addAnswer);

function addAnswer(e) {
  e.preventDefault();

  var queryString = $("form[name=answer]").serialize();

  $.ajax({
    type : 'post',
    url : '/api/qna/addAnswer',
    data : queryString,
    dataType : 'json',
    error: onError,
    success : onSuccess,
  });
}

function onSuccess(json, status){
  var answer = json.answer;
  var answerTemplate = $("#answerTemplate").html();
  var template = answerTemplate.format(answer.writer, new Date(answer.createdDate), answer.contents, answer.answerId, answer.answerId);
  $(".qna-comment-slipp-articles").prepend(template);

  var countOfAnswer = json.countOfAnswer;
  updateCountOfAnswer(countOfAnswer);

  resetInputValue();
}
function updateCountOfAnswer(count) {
    $(".qna-comment-count strong").text(count);
}

function resetInputValue() {
    $(".answerWrite input[type='text'], .answerWrite textarea").val("");
}

function onError(xhr, status) {
  alert("error");
}

String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};

$(".article-util button[name=answer]").click(deleteAnswer);

function deleteAnswer(e) {
    e.preventDefault();

    var article = $(this).closest("article");
    article.remove();

    var countOfAnswer = $(".qna-comment-count strong").text();
    $(".qna-comment-count strong").text(countOfAnswer - 1);

    var form = $(this).closest("form");
    var answerId = form.find("input[name='answerId']").val();
    var questionId = form.find("input[name='questionId']").val();
    console.log(questionId);
    $.ajax({
        type: 'post',
        url: '/api/qna/deleteAnswer',
        data: {answerId : answerId, questionId : questionId},
        dataType: 'json',
        error: function(xhr, status) {
            alert("error");
        },
        success: function(json, status) {
            console.log("article 삭제");
            console.log(json.result.status);
        }
    })
}
