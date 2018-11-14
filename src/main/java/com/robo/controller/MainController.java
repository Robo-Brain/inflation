package com.robo.controller;

import com.robo.Entities.*;
import com.robo.repository.GoodsRepo;
import com.robo.repository.PurchasesRepo;
import com.robo.repository.ShopsRepo;
import com.robo.repository.UserSettingsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
            model.addAttribute("goods", goodsRepo.findAllOrOrderByNameAsc());
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
        userSettingsRepo.saveAndFlush(userSettings);
    }

    @PostMapping("/addShop")
    @ResponseBody
    public void addShop(@RequestParam("name") String name) {
        shopsRepo.save(new Shops(name));
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

        Purchases purchases = purchasesRepo.findByUserIdAndShopIdAndProductIdAndDate(
                userId,
                shopId,
                productId,
                date)
                .orElseGet(() -> purchasesRepo.saveAndFlush(new Purchases(
                        userId,
                        shopId,
                        productId,
                        price,
                        date
                )));
        purchases.setPrice(price);
        purchasesRepo.saveAndFlush(purchases);
    }

}
