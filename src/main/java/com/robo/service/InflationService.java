package com.robo.service;

import com.robo.Entities.Purchases;
import com.robo.Model.PurchasesModel;
import com.robo.repository.GoodsRepo;
import com.robo.repository.PurchasesRepo;
import com.robo.repository.ShopsRepo;
import com.robo.repository.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InflationService {

    @Autowired
    ShopsRepo shopsRepo;
    @Autowired
    GoodsRepo goodsRepo;
    @Autowired
    PurchasesRepo purchasesRepo;
    @Autowired
    UserDetailsRepo userDetailsRepo;

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

    public List<PurchasesModel> getPurchases() throws Exception {
        List<Purchases> purchasesList = purchasesRepo.findAll();

        List<PurchasesModel> purchasesResult = new ArrayList<>();
        purchasesList.forEach(purchase -> {
            PurchasesModel purchaseModel = new PurchasesModel(
                    purchase.getId(),
                    purchase.getDate().toString(),
                    userDetailsRepo.findById(purchase.getUserId()).map(user -> user.getName()).orElse(null),
                    goodsRepo.findById(purchase.getProductId()).map(product -> product.getName()).orElse(null),
                    purchase.getPrice(),
                    shopsRepo.findById(purchase.getShopId()).map(shop -> shop.getName()).orElse(null)
            );
            purchasesResult.add(purchaseModel);
        });
        return purchasesResult;
    }

}
