package org.majorProject.UserService.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.majorProject.UserService.Model.User;
import org.majorProject.UserService.Request.UserRequest;
import org.majorProject.UserService.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/create")
    public User createUser(@RequestBody  @Valid  UserRequest userRequest) throws JsonProcessingException {
       return  userService.createUser(userRequest);
    }

    @GetMapping("/get")
    public User getUser(@RequestParam("contact") String contact)
    {
        return userService.getUser(contact);
    }


}
