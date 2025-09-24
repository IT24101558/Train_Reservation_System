package __Y2_S1_MTR_02.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // Redirect to the index.html page so static resources load correctly
        return "redirect:/templetes/IT24101009/HTML/index.html";
    }
    
    @GetMapping("/index")
    public String index() {
        // Alternative mapping for index
        return "redirect:/templetes/IT24101009/HTML/index.html";
    }

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "Application is running successfully!";
    }
}
