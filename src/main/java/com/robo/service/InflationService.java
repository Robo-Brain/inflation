package com.robo.service;

import com.robo.Entities.Goods;
import com.robo.Entities.Purchases;
import com.robo.Entities.Shops;
import com.robo.Entities.User;
import com.robo.Model.PurchasesModel;
import com.robo.repository.GoodsRepo;
import com.robo.repository.PurchasesRepo;
import com.robo.repository.ShopsRepo;
import com.robo.repository.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InflationService that = (InflationService) o;
        return Objects.equals(shopsRepo, that.shopsRepo) &&
                Objects.equals(goodsRepo, that.goodsRepo) &&
                Objects.equals(purchasesRepo, that.purchasesRepo) &&
                Objects.equals(userDetailsRepo, that.userDetailsRepo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(shopsRepo, goodsRepo, purchasesRepo, userDetailsRepo);
    }

    public void savePriceService(String userId, Map<String,String> requestParams) {

        HashMap<Integer, Integer> map = new HashMap<>();
        HashMap<Integer, Integer> reverseSortedMap = new HashMap<>();
        IntStream.range(0, 100).forEach(num -> map.put(num, num));
        map.entrySet().stream().sorted(Map.Entry.<Integer, Integer>comparingByKey().reversed()).forEach(item -> reverseSortedMap.put(item.getKey(), item.getValue()));



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

    public List<PurchasesModel> getPersonalStatistic(String userId) {
        List<PurchasesModel> personalStatisticList = new ArrayList<>();

        String dateInString = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue();
        LocalDate startDate = LocalDate.parse(dateInString + "-01");
        LocalDate endDate = LocalDate.parse(dateInString + "-31");

        List<Purchases> purchasesList = purchasesRepo.findAllByUserIdAndDateBetween(userId, startDate, endDate);

        while (purchasesList.isEmpty() && startDate.getYear() > 2017){
            if (startDate.getMonthValue() > 1) {
                startDate = startDate.minusMonths(1);
                purchasesList = purchasesRepo.findAllByUserIdAndDateBetween(userId, startDate, endDate);
            } else {
                Calendar cal = Calendar.getInstance();
                startDate = LocalDate.of(cal.get(Calendar.YEAR)-1,12, 1);
                purchasesList = purchasesRepo.findAllByUserIdAndDateBetween(userId, startDate, endDate);
            }
        }

        purchasesList.forEach(purchase -> {

            Integer shopId = purchase.getShopId();
            Integer productId = purchase.getProductId();

            PurchasesModel purchaseModel = new PurchasesModel(
                    purchase.getId(),
                    purchase.getDate(),
                    productId,
                    goodsRepo.findById(productId).get().getName(),
                    purchase.getPrice(),
                    shopId,
                    shopsRepo.findById(shopId).get().getName()
            );

            personalStatisticList.add(purchaseModel);
        });

        return personalStatisticList;
    }


    public List<PurchasesModel> getPurchases() {
        List<Purchases> purchasesList = purchasesRepo.findAll();

        List<PurchasesModel> purchasesResult = new ArrayList<>();
        purchasesList.forEach(purchase -> {
            PurchasesModel purchaseModel = new PurchasesModel();
            purchaseModel.setId(purchase.getId());
            purchaseModel.setPurchaseDate(purchase.getDate());
            purchaseModel.setUserName(userDetailsRepo.findById(purchase.getUserId()).map(User::getName).orElse(null));
            purchaseModel.setProductId(purchase.getProductId());
            purchaseModel.setProductName(goodsRepo.findById(purchase.getProductId()).map(Goods::getName).orElse(null));
            purchaseModel.setPrice(purchase.getPrice());
            purchaseModel.setShopName(shopsRepo.findById(purchase.getShopId()).map(Shops::getName).orElse(null));
            purchasesResult.add(purchaseModel);
        });
        return purchasesResult;
    }

    public List<PurchasesModel> getPurchasesByUserId(String id) {
        List<Purchases> purchasesList = purchasesRepo.findAllByUserId(id);

        List<PurchasesModel> purchasesResult = new ArrayList<>();
        purchasesList.forEach(purchase -> {
            PurchasesModel purchaseModel = new PurchasesModel();
            purchaseModel.setId(purchase.getId());
            purchaseModel.setPurchaseDate(purchase.getDate());
            purchaseModel.setUserId(purchase.getUserId());
            purchaseModel.setUserName(userDetailsRepo.findById(purchase.getUserId()).map(User::getName).orElse(null));
            purchaseModel.setProductName(goodsRepo.findById(purchase.getProductId()).map(Goods::getName).orElse(null));
            purchaseModel.setPrice(purchase.getPrice());
            purchaseModel.setShopName(shopsRepo.findById(purchase.getShopId()).map(Shops::getName).orElse(null));
            purchasesResult.add(purchaseModel);
        });
        return purchasesResult;
    }

    public Map<String, Map> getPurchasesMap(String userId) {
        List<Goods> goodsList = goodsRepo.findAll();
        LinkedHashMap<String, Map> resultMap = new LinkedHashMap<>();

        goodsList.forEach(product -> {
            LinkedHashMap<String, Integer> tmpMap = new LinkedHashMap<>();
            purchasesRepo.findAllByUserIdAndProductId(userId, product.getId())
                    .stream()
                    .sorted(Comparator.comparing(Purchases::getDate))
                    .forEachOrdered(purchase -> {
                        tmpMap.put(purchase.getDate().toString(), purchase.getPrice());
                        resultMap.put(product.getName(), tmpMap);
                    });
        });
        return resultMap;
    }

    public List<PurchasesModel> getPurchasesByDate(String date) {
        LocalDate startDate = LocalDate.parse(date);
        List<Purchases> purchasesList = purchasesRepo.findAllByDateBetween(startDate, startDate.plusDays(1));

        List<PurchasesModel> purchasesResult = new ArrayList<>();
        purchasesList.forEach(purchase -> {
            PurchasesModel purchaseModel = new PurchasesModel(
                    purchase.getId(),
                    purchase.getDate(),
                    userDetailsRepo.findById(purchase.getUserId()).map(User::getName).orElse(null),
                    goodsRepo.findById(purchase.getProductId()).map(Goods::getName).orElse(null),
                    purchase.getPrice(),
                    shopsRepo.findById(purchase.getShopId()).map(Shops::getName).orElse(null)
            );
            purchasesResult.add(purchaseModel);
        });
        return purchasesResult;
    }

    public void editProduct(Map<String, String> productParams) {
        Goods product = goodsRepo.findAllById(Integer.valueOf(productParams.get("productId")));
        product.setName(productParams.get("productName"));
        goodsRepo.save(product);
    }

}
