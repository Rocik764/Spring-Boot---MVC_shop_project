$(document).ready(function () {
    $(".minus").on("click", function (evt) {
        evt.preventDefault()
        productId = $(this).attr("pid")
        amountInput = $("#amount" + productId)

        newAmount = parseInt(amountInput.val()) - 1
        if(newAmount > 0) amountInput.val(newAmount)
    })

    $(".plus").on("click", function (evt) {
        evt.preventDefault()
        productId = $(this).attr("pid")
        amountInput = $("#amount" + productId)

        newAmount = parseInt(amountInput.val()) + 1
        if(newAmount > 0) amountInput.val(newAmount)
    })
})