package com.robo.controller;

import com.robo.Entities.User;
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

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

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

    private String adminId = "116343510121280826843";

    private ThreadLocal<User> userSession = new ThreadLocal<>();

    @ModelAttribute
    public void models(Model model, @AuthenticationPrincipal User user) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {

            userSession.set(user);

            if (user.getId().equals(adminId)){
                model.addAttribute("userId", userSession.get().getId());
                model.addAttribute("users", userDetailsRepo.findAll());
                model.addAttribute("goods", goodsRepo.findAll());
                model.addAttribute("purchases", inflationService.getPurchases());
                model.addAttribute("shops", shopsRepo.findAll());
            }

        }
    }

    @PostMapping("/delPurchase")
    @ResponseBody
    public void delPurchase(@RequestParam("id") Integer id) {
        if (userSession.get().getId().equals(adminId)) purchasesRepo.removeById(id);
    }

    @PostMapping("/delProduct")
    @ResponseBody
    public void delProduct(@RequestParam("id") Integer id){
        if (userSession.get().getId().equals(adminId)) goodsRepo.removeById(id);
    }

    @PostMapping("/editProduct")
    @ResponseBody
    public void delProduct(@RequestParam Map<String,String> requestParams){
        if (userSession.get().getId().equals(adminId)) inflationService.editProduct(requestParams);
    }

    @GetMapping("/getPurchasesByUser")
    @ResponseBody
    public List<PurchasesModel> getPurchasesByUser(@RequestParam String id){
        if (userSession.get().getId().equals(adminId)) {
            return inflationService.getPurchasesByUserId(id);
        } else return null;
    }

    @GetMapping("/getPurchasesByDate")
    @ResponseBody
    public List<PurchasesModel> getPurchasesByDate(@RequestParam String date) {
        if (userSession.get().getId().equals(adminId)) {
            return inflationService.getPurchasesByDate(date);
        } else return null;
    }

    @GetMapping
    public String main() {
        return "admin";
    }
}
