function addShop() {
    var name = $('#newShopName').val();

    if (!!name || name.replace(/[^a-zA-Z0-9]/g, '').length > 2 || name != '')
        $.post("/addShop?name=" + name)
            .done(function () {
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
    console.log(shops);
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
            $('.product').hide();
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

$(function() {});