function addShop() {
    var name = $('#newShopName').val();

    if (!!name || name.replace(/[^a-zA-Z0-9]/g, '').length > 2 || name != '')
        $.post("/addShop?name=" + name)
            .done(function () {
                setTimeout(function() {
                    location.reload();
                }, 4000);
                new Noty({
                    type: 'success',
                    theme: 'metroui',
                    text: 'Магазин успешно добавлен.',
                    timeout: 5000
                }).show();
            })
            .fail(function () {
                new Noty({
                    type: 'error',
                    theme: 'metroui',
                    text: 'Ошибка. Не удалось добавить магазин.',
                    timeout: 5000
                }).show();
            });
    else {
        new Noty({
            type: 'error',
            theme: 'metroui',
            text: 'Ошибка. Укажите название магазина.',
            timeout: 5000
        }).show();
    }
}

function changeShop(id) {
    $.post("/setDefaultShop?id=" + id)
        .done(function () {
            new Noty({
                type: 'success',
                theme: 'metroui',
                text: 'Магазин по умолчанию изменен.',
                timeout: 5000
            }).show();
        })
        .fail(function () {
            new Noty({
                type: 'error',
                theme: 'metroui',
                text: 'Ошибка. Невозможно изменить магазин по умолчанию.',
                timeout: 5000
            }).show();
        })
}

function appendGoodsAndFilter() {
    goods.forEach(function (value) {
        productList.push(value.name);
        var letter = value.name.substring(0, 1);
        if (jQuery.inArray(letter, productMask) < 0) {
            productMask.push(letter)
        }
    });

    productMask.forEach(function (value) {
        $('#filter').append(
            '<span title="' + value + '"> '
            + value
            + ' </span>'
        )
    });
}

function savePrice(shopId, productId, price) {
    $.ajax({
        url: '/savePrice',
        type: 'POST',
        data: jQuery.param({ shopId: shopId, productId: productId, price : price}) ,
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        error: function () {
            //Do Something
        }
    });
}

function showAddProductForm(e) {
    var cls = $(e.target).attr('class');
    if (cls == 'addProduct'){
        $('#wrap').toggle();
        $('.addProduct').toggleClass("showProductForm");
        $('.add').toggle();
    } else if(cls == 'add') {
        $('#wrap').show();
        $('.addProduct').addClass("showProductForm");
        $('.add').toggle();
    }
    $('.addShopInput').hide();
    $('#shops').show().val(userSettings.shopId);
}
function submitNewProduct() {
    var name = $('#productName').val();
    if(!!name || name != '')
        $.post("/addProduct?name=" + name)
            .done(function () {
                setTimeout(function() {
                    location.reload();
                }, 3000);
                new Noty({
                    type: 'success',
                    theme: 'metroui',
                    text: 'Новый продукт успешно добавлен.',
                    timeout: 3000
                }).show();
            })
            .fail(function () {
                new Noty({
                    type: 'error',
                    theme: 'metroui',
                    text: 'Ошибка. Не удалось добавить продукт.',
                    timeout: 5000
                }).show();
            });
    else {
        new Noty({
            type: 'error',
            theme: 'metroui',
            text: 'Ошибка. Укажите название продукта.',
            timeout: 5000
        }).show();

    }
}

function wrapClose() {
    $('#wrap').hide();
    $('.addProduct').removeClass("showProductForm");
    $('.addShop').removeClass("showShopForm");
    $('.add').show();
}

function filter(letter) {
    $.ajax({
        type : "GET",
        url : "/goodsFilter?letter=" + letter,
        success: function(data){
            $('.product').remove();
            data.forEach(function (item) {
                $('#goods').append(
                    "<div class='product'>"
                    + "<input class='price' type='number' id='" + item.id + "' />"
                    + "<label>"
                    + item.name
                    + "</label></div>"
                )
            })
        }
    });
}

function getPersonalStatistic() {
    $.ajax({
        type : "GET",
        url : "/personalStatistic",
        success: function(data){
            console.log(data);
            data.forEach(function (item) {
                $('.modal-body').append(
                    "<div>"
                    + item.shopName
                    + " / "
                    + item.productName
                    + " = "
                    + item.price
                    + "p. / "
                    + item.purchaseDate
                    + "</div>"
                )
            })
        }
    });
}

$(function() {});