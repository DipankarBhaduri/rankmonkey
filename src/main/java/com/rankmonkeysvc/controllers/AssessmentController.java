package com.rankmonkeysvc.controllers;

import com.rankmonkeysvc.auth.JwtSvc;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/assessment")
public class AssessmentController {

    private final JwtSvc jwtSvc;

    @Autowired
    public AssessmentController(
            JwtSvc jwtSvc
    ) {
        this.jwtSvc = jwtSvc;
    }

    @PostMapping("/question")
    public String register(
            HttpServletRequest request
    ) {
        return jwtSvc.getUser(request);
    }
}
