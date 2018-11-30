package com.robo.controller;

import com.robo.Entities.User;
import com.robo.service.InflationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/charts")
public class ChartsController {

    @Autowired
    InflationService inflationService;

    private ThreadLocal<User> userSession = new ThreadLocal<>();

    @ModelAttribute
    public void models(Model model, @AuthenticationPrincipal User user) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {

            userSession.set(user);

            model.addAttribute("myPurchases", inflationService.getPurchasesByUserId(userSession.get().getId()));
            model.addAttribute("charts", inflationService.getPurchasesMap(userSession.get().getId()));

        }
    }

    @GetMapping
    public String main() {
        return "charts";
    }
}
