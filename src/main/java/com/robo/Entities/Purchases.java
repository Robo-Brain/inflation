package com.robo.Entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Purchases {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    Integer id;

    @Column(name = "user_id", nullable = false)
    @Getter
    @Setter
    String userId;

    @Column(name = "shop_id", nullable = false)
    @Getter
    @Setter
    Integer shopId;

    @Column(name = "product_id", nullable = false)
    @Getter
    @Setter
    Integer productId;

    @Column(name = "price", nullable = false)
    @Getter
    @Setter
    Integer price;

    @Column(name = "date", nullable = false)
    @Getter
    @Setter
    LocalDate date;

    public Purchases(String userId, Integer shopId, Integer productId, Integer price, LocalDate date) {
        this.userId = userId;
        this.shopId = shopId;
        this.productId = productId;
        this.price = price;
        this.date = date;
    }

}
