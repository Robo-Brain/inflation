package com.robo.service;

import com.robo.Entities.Purchases;
import com.robo.repository.PurchasesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class InflationService {

    @Autowired
    PurchasesRepo purchasesRepo;

    public void savePriceService(String userId, Map<String,String> requestParams) {
        Integer shopId = Integer.parseInt(requestParams.get("shopId"));
        Integer productId = Integer.parseInt(requestParams.get("productId"));
        Integer price = Integer.parseInt(requestParams.get("price"));
        LocalDate date = LocalDate.now();

        Purchases purchases = purchasesRepo.findAllByUserIdAndShopIdAndProductIdAndDate(
                userId,
                shopId,
                productId,
                date).orElseGet(Purchases::new);

        purchases.setUserId(userId);
        purchases.setShopId(shopId);
        purchases.setProductId(productId);
        purchases.setPrice(price);
        purchases.setDate(date);
        purchasesRepo.save(purchases);

    }

}
