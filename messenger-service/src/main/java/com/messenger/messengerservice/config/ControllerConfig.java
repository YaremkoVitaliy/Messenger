package com.messenger.messengerservice.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ControllerConfig {

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public Object handleStaticResourceNotFound(final NoHandlerFoundException ex,
                                               HttpServletRequest req,
                                               RedirectAttributes redirectAttributes) {
        if (req.getRequestURI().startsWith("/api"))
            return new ResponseEntity<>("Not Found!", HttpStatus.NOT_FOUND);
        else {
            return "redirect:/index.html";
        }
    }
}
