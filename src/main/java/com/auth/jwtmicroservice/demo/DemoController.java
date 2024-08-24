package com.auth.jwtmicroservice.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
@RequestMapping("demo")
@Slf4j
public class DemoController {

    /**
     * TESTING PURPOSES ONLY
     * You can only access this endpoint if the user is authenticated and authorized
     * @return message
     */
    @GetMapping
    public ResponseEntity<?> sayHello(){
        log.info("logging info from demo controller");
        log.error("logging error from demo controller");
        log.warn("logging warn from demo controller");
        return ResponseEntity.ok("Hello world from secure endpoint");
    }

}
