package com.robo.controller;

import com.robo.Entities.*;
import com.robo.Model.PersonalStatistic;
import com.robo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    ShopsRepo shopsRepo;
    @Autowired
    GoodsRepo goodsRepo;
    @Autowired
    UserSettingsRepo userSettingsRepo;
    @Autowired
    PurchasesRepo purchasesRepo;
    @Autowired
    UserDetailsRepo userDetailsRepo;

    private ThreadLocal<User> userSession = new ThreadLocal<>();
    private ThreadLocal<UserSettings> userSettingsSession = new ThreadLocal<>();

    @ModelAttribute
    public void models(Model model, @AuthenticationPrincipal User user) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {

            userSession.set(user);
            model.addAttribute("user", userSession.get());

            userSettingsSession.set(userSettingsRepo.findByUserId(user.getId()).orElseThrow(Exception::new));
            model.addAttribute("userSettings", userSettingsSession.get());

            model.addAttribute("shops", shopsRepo.findAll());
            model.addAttribute("goods", goodsRepo.findAllOrderByNameAsc());

        }
    }

    @GetMapping
    public String main() {
        return "index";
    }

    @PostMapping("/setDefaultShop")
    @ResponseBody
    public void setDefShop(@RequestParam("id") Integer id) {
        UserSettings userSettings = userSettingsSession.get();
        userSettings.setShopId(id);
        userSettingsRepo.save(userSettings);
    }

    @PostMapping("/addShop")
    @ResponseBody
    public void addShop(@RequestParam("name") String name) {
        shopsRepo.findByName(name).orElseGet(() -> shopsRepo.save(new Shops(name)));

        UserSettings userSettings = userSettingsSession.get();
        userSettings.setShopId(shopsRepo.findByName(name).get().getId());
        userSettingsRepo.save(userSettings);
    }

    @PostMapping("/addProduct")
    @ResponseBody
    public void addProduct(@RequestParam("name") String name) { goodsRepo.save(new Goods(name)); }

    @PostMapping("/savePrice")
    @ResponseBody
    public void savePrice(@RequestParam Map<String,String> requestParams) {

        String userId = userSettingsSession.get().getUserId();
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
        purchasesRepo.saveAndFlush(purchases);

    }

    @GetMapping("/personalStatistic")
    @ResponseBody
    public List<PersonalStatistic> personalStatistic() {

        String userId = userSettingsSession.get().getUserId();

        List<Purchases> purchases = purchasesRepo.findAllByUserId(userId);

        List<PersonalStatistic> personalStatisticList = new ArrayList<>();

        purchases.forEach(purchase -> {

        Integer shopId = purchase.getShopId();
        String shopName = shopsRepo.findById(shopId).get().getName();

        Integer productId = purchase.getProductId();
        String productName = goodsRepo.findById(productId).get().getName();

        Integer price = purchase.getPrice();

        LocalDate date = purchase.getDate();

        PersonalStatistic personalStatistic = new PersonalStatistic(
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
     @RequestMapping(value="/goodsFilter", method=RequestMethod.GET)
     @ResponseBody
     public List<Goods> goodsFilter(@RequestParam String letter) {
         return goodsRepo.findByNameStartingWith(letter);
     }

}
