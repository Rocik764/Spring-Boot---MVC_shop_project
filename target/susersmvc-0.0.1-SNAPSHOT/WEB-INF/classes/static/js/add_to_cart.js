$(document).ready(function () {
    $("#buttonAddToCart").on("click", function (e) {
        addToCart();
    })
})


function addToCart() {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    let amount = $("#amount" + productId).val()
    let url = contextPath + "cartRest/add/" + productId + "/" + amount

    console.log(amount);
    console.log(url);
    console.log(token);
    console.log(header);
    $.ajax({
        contentType: "application/json",
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token)
        }
    }).done(function (response) {
        $("#modalTitle").text("Koszyk")
        $("#modalBody").text(response)
        $("#myModal").modal()
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log("failed: ");
        console.log(jqXHR.responseText);
        console.log(textStatus);
        console.log(errorThrown);
        $("#modalTitle").text("Koszyk")
        $("#modalBody").text("Błąd")
        $("#myModal").modal()
    });
}