package com.robo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
public class PersonalStatistic {

    @Getter
    @Setter
    Integer id;
    @Getter
    @Setter
    Integer shopId;
    @Getter
    @Setter
    String shopName;

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
    LocalDate purchaseDate;

}
