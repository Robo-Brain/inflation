function appendPurchaseUsers() {
    users.forEach(function (user) {
        $("#purchasesUsers").append(
            "<option id='" + user.id + "'>"
            + user.name
            +"</option>"
        );
    });
}

function appendPurchaseDates() {
    var datesArr = [];
    purchases.forEach(function (purchase) {
        datesArr.push(purchase.purchaseDate)
    });
    unique(datesArr).forEach(function (date) {
        $("#purchasesDates").append(
            "<option value='" + date + "'>"
            + date
            +"</option>"
        );
    });
}

function appendPurchaseShops() {
    shops.forEach(function (shop) {
        $("#purchasesShops").append(
            "<option>"
            + shop.name
            +"</option>"
        );
    });
}

function sortPurchasesByUser(id) {
    $.ajax({
        type : "GET",
        url : "/admin/getPurchasesByUser?id=" + id,
        success: function(data){
            appendSort(data);
        }
    });
}

function sortPurchasesByDate(date) {
    $.ajax({
        type : "GET",
        url : "/admin/getPurchasesByDate?date=" + date,
        success: function(data){
            appendSort(data);
        }
    });
}

function sortPurchasesByShop(shop) {
    $.ajax({
        type : "GET",
        url : "/admin/getPurchasesByShop?shop=" + shop,
        success: function(data){
            appendSort(data);
        }
    });
}

function appendSort(purchases) {
    $('.purchase').remove();
    purchases.forEach(function (purchase) {
        console.log(purchase.purchaseDate);
        $('#purchases').append(
            "<tr class='purchase' value='" + purchase.id + "'>" +
                + "<td><span>"
                    + purchase.purchaseDate
                + "</span></td>"
                + "<td><span>"
                    + purchase.purchaseDate
                + "</span></td>"
                + "<td><span>"
                    + purchase.userName
                + "</span></td>"
                + "<td><span>"
                    + purchase.productName
                + "</span></td>"
                + "<td><span>"
                    + purchase.price
                + "</span></td>"
                + "<td><span>"
                    + purchase.shopName
                + "</span></td>"
                + "<td>&nbsp;<button class='delPurchaseButton' id='" + purchase.id + "'>DEL</button></td>"
            + "</tr>"
        )
    });
}

function unique(array){
    return array.filter(function(el, index, arr) {
        return index === arr.indexOf(el);
    });
}