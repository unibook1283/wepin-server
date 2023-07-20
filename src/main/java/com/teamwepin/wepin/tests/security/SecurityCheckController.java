package com.teamwepin.wepin.tests.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityCheckController {

    @GetMapping("/secured")
    public String secured() {
        return "This api needs spring security authentication.";
    }

    @GetMapping("/unsecured")
    public String unsecured() {
        return "This api doesn't need spring security authentication.";
    }

}
