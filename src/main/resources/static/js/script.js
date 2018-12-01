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

function filter(letter, callback) {
    $.ajax({
        type : "GET",
        url : "/goodsFilter?letter=" + letter,
        success: function(data){
            callback(data);
        }
    });
}

function savePrice(shopId, productId, price) {
    clearTimeout(timeout);
    timeout = setTimeout(function () {
        $.ajax({
            url: '/savePrice',
            type: 'POST',
            data: jQuery.param({ shopId: shopId, productId: productId, price : price}),
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            success: function() {
                $('#' + productId).each(function(){
                    $(this).fadeOut('fast', function(){
                        $(this).css('color', 'green');
                        $(this).fadeIn('fast');
                    });
                });
            },
            error: function () {
                $('#' + productId).each(function(){
                    $(this).css('color', 'red');
                    alert('Ошибка, сообщите разработчику код ошибки: ' + productId);
                });
            }
        });
    }, 700);
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
    name = name.replace("+", "%2B")
                .replace(' ', '+')	                
                .replace(' ', '+')
                .replace("\"", "%22")	                
                .replace("\"", "%22")
                .replace("%", "%25")
                .replace("<", "%3C")	                
                .replace("<", "%3C")
                .replace(">", "%3E")	                
                .replace(">", "%3E")
                .replace("^", "%5E")	                
                .replace("^", "%5E");
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

function getPersonalStatistic() {
    $.ajax({
        type : "GET",
        url : "/personalStatistic",
        success: function(data){
            appendPersonalStatistic(data);
        }
    });
}

function appendPersonalStatistic(personalStatisticData) {
    console.log(personalStatisticData);
    $('.modal-body').append(
        "<div class='statisticHeader'>"
            + "<div class='statisticHeaderDate'>Дата:</div>"
            + "<div class='statisticHeaderProduct'>Продукт:</div>"
            + "<div class='statisticHeaderPrice'>Цена:</div>"
            + "<div class='statisticHeaderShop'>Магазин:</div>"
        + "</div>"
    );
    if (personalStatisticData.length <=0) {
        $('.modal-body').append(
            "<p style='text-align: center'>Ты еще ничего не купил :(</p>"
        )
    }
    var purchaseDate;
    personalStatisticData.forEach(function (item) {
        var shopName = item.shopName;
        var productName = item.productName;
        var price = item.price;
        var statisticBodyId = item.id;

        if (item.purchaseDate == purchaseDate) {
            purchaseDate = item.purchaseDate;
        } else {
            statisticBodyId = 'statisticBodyBordered';
            purchaseDate = item.purchaseDate;
        }

        $('.modal-body').append(
            "<div id='" + statisticBodyId + "' class='statisticBody'>"
                + "<div class='statisticDate'>"
                    + purchaseDate
                + "</div>"
                + "<div class='statisticProduct'>"
                    + productName
                + "</div>"
                + "<div class='statisticPrice'>"
                    + price
                + "</div>"
                + "<div class='statisticShop'>"
                    + shopName
                + "</div>"
            + "</div>"
        )
    })
}

$(function() {});
