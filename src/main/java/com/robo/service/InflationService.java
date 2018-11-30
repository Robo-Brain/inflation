package com.robo.service;

import com.robo.Entities.Goods;
import com.robo.Entities.Purchases;
import com.robo.Model.PersonalStatistic;
import com.robo.Model.PurchasesModel;
import com.robo.repository.GoodsRepo;
import com.robo.repository.PurchasesRepo;
import com.robo.repository.ShopsRepo;
import com.robo.repository.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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

    public List<PersonalStatistic> getPersonalStatistic(String userId) {
        List<PersonalStatistic> personalStatisticList = new ArrayList<>();

        List<Purchases> purchases = purchasesRepo.findAllByUserId(userId);

        purchases.forEach(purchase -> {

            Integer id = purchase.getId();
            Integer shopId = purchase.getShopId();
            String shopName = shopsRepo.findById(shopId).get().getName();
            Integer productId = purchase.getProductId();
            String productName = goodsRepo.findById(productId).get().getName();
            Integer price = purchase.getPrice();
            LocalDate date = purchase.getDate();

            PersonalStatistic personalStatistic = new PersonalStatistic(
                    id,
                    shopId,
                    shopName,
                    productId,
                    productName,
                    price,
                    date
            );

            personalStatisticList.add(personalStatistic);
        });

        return personalStatisticList;
    }


    public List<PurchasesModel> getPurchases() {
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

    public List<PurchasesModel> getPurchasesByUserId(String id) {
        List<Purchases> purchasesList = purchasesRepo.findAllByUserId(id);

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

    public Map<String, Map> getPurchasesMap(String userId) {
        List<Goods> goodsList = goodsRepo.findAll();
        Map<String, Map> resultMap = new HashMap<>();

        goodsList.forEach(product -> {
            List<Purchases> productList = purchasesRepo.findAllByUserIdAndProductId(userId, product.getId());
            Map<String, Integer> tmpMap = new HashMap<>();

            productList.forEach(item -> {
                tmpMap.put(item.getDate().toString(), item.getPrice());
                resultMap.put(product.getName(), tmpMap);
            });

        });

        return resultMap;

    }

}
