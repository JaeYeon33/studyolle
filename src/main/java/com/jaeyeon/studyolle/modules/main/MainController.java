package com.jaeyeon.studyolle.modules.main;

import com.jaeyeon.studyolle.modules.account.Account;
import com.jaeyeon.studyolle.modules.account.CurrentUser;
import com.jaeyeon.studyolle.modules.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model){
        if (account != null){
            model.addAttribute(account);
        }

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
