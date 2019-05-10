package ru.rmamedov.app.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Rustam Mamedov
 */

@Controller
public class ErrorPageController implements ErrorController {

    @GetMapping("/error")
    public String handleError() {
        return "page_404";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
