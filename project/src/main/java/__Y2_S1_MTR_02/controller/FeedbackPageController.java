package __Y2_S1_MTR_02.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FeedbackPageController {

    @GetMapping("/member/feedback")
    public String memberFeedbackPage() {
        return "redirect:/templetes/IT24101009/HTML/mem-feedback.html";
    }
}


