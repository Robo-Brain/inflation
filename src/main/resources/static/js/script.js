function addShop() {
    $("#add").dialog({
        classes: {
            "ui-dialog": "ui-dialogMobile"
        },
        resizable: false,
        height: "auto",
        width: 400,
        modal: true,
        buttons: {
            "Add": function() {
                $( this ).dialog('close');

                var name = $('#addName').val();

                if(!!name || name.replace(/[^a-zA-Z0-9]/g, '').length > 2 || name != '')
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
            },
            Cancel: function() {
                $(this).dialog('close');
            }
        }
    });
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
    console.log(shopId, productId, price);
    $.ajax({
        url: '/savePrice',
        type: 'POST',
        data: jQuery.param({ shopId: shopId, productId: productId, price : price}) ,
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        success: function (response) {
            //Do Something
        },
        error: function () {
            //Do Something
        }
    });
}

function addProduct() {
    $("#add").dialog({
        classes: {
            "ui-dialog": "ui-dialogMobile"
        },
        resizable: false,
        height: "auto",
        width: 400,
        modal: true,
        buttons: {
            "Add": function() {
                $( this ).dialog('close');
                var name = $('#addName').val();
                $.post("/addProduct?name=" + name)
                    .done(function () {
                        new Noty({
                            type: 'success',
                            theme: 'metroui',
                            text: 'Новый продукт добавлен.',
                            timeout: 5000
                        }).show();
                    })
                    .fail(function () {
                        new Noty({
                            type: 'error',
                            theme: 'metroui',
                            text: 'Ошибка. Невозможно добавить продукт.',
                            timeout: 5000
                        }).show();
                    })
            },
            Cancel: function() {
                $(this).dialog('close');
            }
        }
    });
}

$(function() {




    // $('#cancelNewProduct').click(function () {
    //     setTimeout(function () {
    //         $('.showAddForm').attr('class', 'addForm');
    //     }, 500);
    //     setTimeout(function () {
    //         $('#addProduct').toggleClass('clicked');
    //     }, 500);
    // });

});

$(function() {});