$(document).ready(function () {
    $(".minus").on("click", function (evt) {
        evt.preventDefault()
        decreaseAmount($(this), false)
        console.log("minus")
    })

    $(".plus").on("click", function (evt) {
        evt.preventDefault()
        increaseAmount($(this))
        console.log("plus")
    })

    $(".link-remove").on("click", function (evt) {
        evt.preventDefault()
        removeFromCart($(this))
    })

    updateTotal()
})

function updateTotal() {
    console.log("updateTotal")
    let total = 0.0;
    $(".productSubtotal").each(function (index, element) {
        total += parseFloat(element.innerHTML)
    })

    $("#totalAmount").text(total + " zł")
}

function decreaseAmount(link, error) {
    console.log("decreaseAmount")
    productId = link.attr("pid")
    let amountInput = $("#amount" + productId)
    let newAmount = parseInt(amountInput.val()) - 1
    if(error) {
        amountInput.val(newAmount)
        return
    }
    if(newAmount > 0) {
        amountInput.val(newAmount)
        updateAmount(productId, newAmount)
    }
}

function increaseAmount(link) {
    console.log("increaseAmount")
    productId = link.attr("pid")
    let amountInput = $("#amount" + productId)
    let newAmount = parseInt(amountInput.val()) + 1
    if(newAmount > 0) {
        amountInput.val(newAmount)
        updateAmount(productId, newAmount, link)
    }
}

function updateSubtotal(newSubtotal, productId) {
    console.log("updateSubtotal");
    $("#subtotal" + productId).text(newSubtotal)
}

function updateAmount(productId, amount, link) {
    console.log("updateAmount")
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    let url = contextPath + "cartRest/update/" + productId + "/" + amount

    $.ajax({
        contentType: "application/json",
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token)
        }
    }).done(function (newSubtotal) {
        if(isNaN(newSubtotal)) {
            $("#modalTitle").text("Cart")
            $("#modalBody").text(newSubtotal)
            $("#myModal").modal()
            decreaseAmount(link, true)
        } else {
            updateSubtotal(newSubtotal, productId)
            updateTotal()
            console.log("done", newSubtotal, " ", productId)
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log("failed: ");
        console.log(jqXHR.responseText);
        console.log(textStatus);
        console.log(errorThrown);
        $("#modalTitle").text("Cart")
        $("#modalBody").text("Something went wrong")
        $("#myModal").modal()
    });
}

function removeFromCart(link) {
    console.log("removeFromCart")
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    let url = link.attr("href")
    $.ajax({
        contentType: "application/json",
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token)
        }
    }).done(function (response) {
        $("#modalTitle").text("Cart")
        if(response.includes("usunięty")) {
            $("#myModal").on("hide.bs.modal", function (e) {
                let rowNumber = link.attr("rowNumber")
                removeProduct(rowNumber)
                updateTotal()
            })
        }
        $("#modalBody").text(response)
        $("#myModal").modal()
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log("failed: ");
        console.log(jqXHR.responseText);
        console.log(textStatus);
        console.log(errorThrown);
        $("#modalTitle").text("Cart")
        $("#modalBody").text("Something went wrong")
        $("#myModal").modal()
    });
}

function removeProduct(rowNumber) {
    let rowId = "row" + rowNumber
    $("#" + rowId).remove()
}