package com.se1858.group4.Land_Auction_SWP391.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff")
public class DashboardController {
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboardTemplates/dashboard";
    }

    @GetMapping("/icons")
    public String icons() {
        return "dashboardTemplates/icons";
    }

    @GetMapping("/map")
    public String map() {
        return "dashboardTemplates/map";
    }

    @GetMapping("/notifications")
    public String notifications() {
        return "dashboardTemplates/notifications";
    }

    @GetMapping("/tables")
    public String tables() {
        return "dashboardTemplates/tables";
    }

    @GetMapping("/typography")
    public String typography() {
        return "dashboardTemplates/typography";
    }

    @GetMapping("/upgrade")
    public String upgrade() {
        return "dashboardTemplates/upgrade";
    }

    @GetMapping("/user")
    public String user() {
        return "dashboardTemplates/user";
    }
}
