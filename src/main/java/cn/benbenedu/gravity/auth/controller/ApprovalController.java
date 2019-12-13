package cn.benbenedu.gravity.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("authorizationRequest")
public class ApprovalController {

    @GetMapping("/oauth/confirm_access")
    public String getAccessConfirmation(Model model,
                                        HttpServletRequest request) {

        if (request.getAttribute("_csrf") != null) {
            model.addAttribute("_csrf", request.getAttribute("_csrf"));
        }

        model.addAttribute("scopes", request.getAttribute("scopes"));

        model.addAttribute("authProcessUrl", "/xauth/oauth/authorize");

        return "base-approval";
    }
}
