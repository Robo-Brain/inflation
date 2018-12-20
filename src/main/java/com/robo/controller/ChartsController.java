package com.robo.controller;

import com.robo.Entities.User;
import com.robo.repository.UserDetailsRepo;
import com.robo.service.InflationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/charts")
public class ChartsController {

    @Autowired
    InflationService inflationService;
    @Autowired
    UserDetailsRepo userDetailsRepo;

    private ThreadLocal<User> userSession = new ThreadLocal<>();

    @ModelAttribute
    public void models(Model model, @AuthenticationPrincipal User user) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {

            userSession.set(user);

            model.addAttribute("myPurchases", inflationService.getPurchasesByUserId(userSession.get().getId()));
            model.addAttribute("charts", inflationService.getPurchasesMap(userSession.get().getId()));
            model.addAttribute("users", userDetailsRepo.findAll());

        }
    }

    @GetMapping("/getPurchasesByUserId")
    @ResponseBody
    public Map<String, Map> getPurchasesMapByUserId(@RequestParam String userId){
        return inflationService.getPurchasesMap(userId);
    }

    @GetMapping
    public String main() {
        return "charts";
    }

}
