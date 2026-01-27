package com.example.controller;

import com.example.model.Greeting;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class GreetController {

    @GetMapping({"/", "/homePage"})
    public String homePage() {
        return "index";
    }

    @GetMapping("/greet")
    @ResponseBody
    public Greeting greet() {
        return new Greeting(1, "Hello World!!!");
    }

    @GetMapping("/greetWithPathVariable/{name}")
    @ResponseBody
    public Greeting greetWithPathVariable(@PathVariable String name) {
        return new Greeting(1, "Hello World " + name + "!!!");
    }

    @GetMapping("/greetWithQueryVariable")
    @ResponseBody
    public Greeting greetWithQueryVariable(@RequestParam String name) {
        return new Greeting(1, "Hello World " + name + "!!!");
    }

    @PostMapping("/greetWithPost")
    @ResponseBody
    public Greeting greetWithPost() {
        return new Greeting(1, "Hello World!!!");
    }

    @PostMapping("/greetWithPostAndFormData")
    @ResponseBody
    public Greeting greetWithPostAndFormData(@RequestParam String id, @RequestParam String name) {
        return new Greeting(Long.parseLong(id), "Hello World " + name + "!!!");
    }
}