package __Y2_S1_MTR_02.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SchedulePageController {

    @GetMapping("/guest/schedule")
    public String guestSchedulePage() {
        return "redirect:/templetes/IT24102460/HTML/guest_shedule.html";
    }

    @GetMapping("/member/schedule")
    public String memberSchedulePage() {
        return "redirect:/templetes/IT24102460/HTML/mem_shedule.html";
    }

    @GetMapping("/admin/schedule")
    public String adminSchedulePage() {
        return "redirect:/templetes/IT24102460/HTML/admin_shedule.html";
    }
}


