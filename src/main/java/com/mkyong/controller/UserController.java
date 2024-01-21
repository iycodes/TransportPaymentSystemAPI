package com.mkyong.controller;


import com.mkyong.model.Userr;
import com.mkyong.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class    UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<Userr> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Userr> findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    // create a user
    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping
    public Userr create(@RequestBody Userr user) {
        return userService.save(user);
    }

    // update a user
    @PutMapping
    public Userr update(@RequestBody Userr user) {
        return userService.save(user);
    }

    // delete a user
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @GetMapping("/find/title/{title}")
    public List<Userr> findByTitle(@PathVariable String title) {
        return userService.findByName(title);
    }

    @GetMapping(value = "/{userId}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> qrCodeFile(@PathVariable("userId") String userId) throws Exception{
        return new ResponseEntity<>(UserService.generateQRCodeImage(userId),HttpStatus.OK);

    }
    @GetMapping(value = "/{userId}/qrcodeSvg")
    public ResponseEntity<String> qrCodeSvg(@PathVariable("userId") String userId) throws Exception{
        return new ResponseEntity<>(UserService.getQRCodeSvg(userId, 700, 700, false),HttpStatus.OK);
    }






}
