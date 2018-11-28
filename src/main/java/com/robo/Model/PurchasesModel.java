package com.robo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class PurchasesModel {

    @Getter
    @Setter
    Integer purchaseId;
    @Getter
    @Setter
    String date;
    @Getter
    @Setter
    String userName;
    @Getter
    @Setter
    String productName;
    @Getter
    @Setter
    Integer price;
    @Getter
    @Setter
    String shopName;

}
