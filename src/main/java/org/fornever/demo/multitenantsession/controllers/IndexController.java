package org.fornever.demo.multitenantsession.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class IndexController {


    @GetMapping("/api/{zoneId}")
    public Object all(@PathVariable String zoneId) {
        Map rt = new HashMap();


        return rt;
    }

}
