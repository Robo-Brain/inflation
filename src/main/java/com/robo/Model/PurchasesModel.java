package com.robo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
public class PurchasesModel {

    @Getter
    @Setter
    Integer id;
    @Getter
    @Setter
    LocalDate purchaseDate;
    @Getter
    @Setter
    String userId;
    @Getter
    @Setter
    String userName;
    @Getter
    @Setter
    Integer productId;
    @Getter
    @Setter
    String productName;
    @Getter
    @Setter
    Integer price;
    @Getter
    @Setter
    Integer shopId;
    @Getter
    @Setter
    String shopName;

    public PurchasesModel(Integer id, LocalDate purchaseDate, Integer productId, String productName, Integer price, Integer shopId, String shopName) {
        this.id = id;
        this.purchaseDate = purchaseDate;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.shopId = shopId;
        this.shopName = shopName;
    }

    public PurchasesModel(Integer id, LocalDate purchaseDate, String userName, String productName, Integer price, String shopName) {
        this.id = id;
        this.purchaseDate = purchaseDate;
        this.userName = userName;
        this.productName = productName;
        this.price = price;
        this.shopName = shopName;
    }

    public PurchasesModel(Integer id, LocalDate purchaseDate, String userName, Integer productId, String productName, Integer price, String shopName) {
        this.id = id;
        this.purchaseDate = purchaseDate;
        this.userName = userName;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.shopName = shopName;
    }

}
