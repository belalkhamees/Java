
package org.route.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.route.common.StoreException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StoreException.class)
    public String handleStoreException(
            StoreException exception,
            Model model,
            HttpServletResponse response) {

        response.setStatus(exception.status());

        model.addAttribute("status", exception.status());
        model.addAttribute("message", exception.getMessage());

        return "error";
    }
}
