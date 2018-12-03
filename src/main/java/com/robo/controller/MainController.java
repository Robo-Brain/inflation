package com.robo.controller;

import com.robo.Entities.Goods;
import com.robo.Entities.Shops;
import com.robo.Entities.User;
import com.robo.Entities.UserSettings;
import com.robo.Model.PurchasesModel;
import com.robo.repository.*;
import com.robo.service.InflationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    @Autowired
    InflationService inflationService;

    private ThreadLocal<User> userSession = new ThreadLocal<>();
    private ThreadLocal<UserSettings> userSettingsSession = new ThreadLocal<>();

    @ModelAttribute
    public void models(Model model, @AuthenticationPrincipal User user) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {

            userSession.set(user);
            model.addAttribute("user", userSession.get());

            userSettingsSession.set(userSettingsRepo.findByUserId(user.getId()).orElseGet(() -> userSettingsRepo.save(new UserSettings(user.getId()))));
            model.addAttribute("userSettings", userSettingsSession.get());

            model.addAttribute("shops", shopsRepo.findAll());
            model.addAttribute("goods", goodsRepo.findAllOrderByNameAsc());
            model.addAttribute("todaysPurchases", purchasesRepo.findAllByUserIdAndDateBetween(userSession.get().getId(), LocalDate.now(), LocalDate.now().plusDays(1)));

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
        inflationService.savePriceService(userSettingsSession.get().getUserId(), requestParams);
    }

    @GetMapping("/personalStatistic")
    @ResponseBody
    public List<PurchasesModel> personalStatistic() {
        return inflationService.getPersonalStatistic(userSettingsSession.get().getUserId());
    }

     @GetMapping("/goodsFilter")
     @ResponseBody
     public List<Goods> goodsFilter(@RequestParam String letter) {
         return goodsRepo.findByNameStartingWith(letter);
     }

}
