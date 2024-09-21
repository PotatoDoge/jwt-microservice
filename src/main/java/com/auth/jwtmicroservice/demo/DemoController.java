package com.auth.jwtmicroservice.demo;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@CrossOrigin
@RequestMapping("demo")
public class DemoController {

    private static final Logger log = LogManager.getLogger(DemoController.class);

    /**
     * TESTING PURPOSES ONLY
     * You can only access this endpoint if the user is authenticated and authorized
     * @return message
     */
    @GetMapping
    public ResponseEntity<?> sayHello(){
        log.info("Secure endpoint reached");
        return ResponseEntity.ok("Hello world from secure endpoint");
    }

}

// TODO: Add logs to different classes and levels. --- Run kafka service in the same network