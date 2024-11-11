package com.mkyong.controller;

import com.mkyong.helpers.MyEmailValidator;
import com.mkyong.helpers.Patcher;
import com.mkyong.model.TransactionEntity;
import com.mkyong.model.UserEntity;
import com.mkyong.model.UserEntity;
import com.mkyong.model.dtos.DriverDto;
import com.mkyong.model.dtos.RegisterUserDto;
import com.mkyong.model.dtos.VerifyEmailDto;
import com.mkyong.responses.ErrorResponse;
import com.mkyong.responses.VerifyEmailResponse;
import com.mkyong.service.TransactionService;
import com.mkyong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    Patcher patcher; // custom class for updating fields
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/all")
    public List<UserEntity> findAll() {
        return userService.findAll();
    }

    @GetMapping("/me")
    public ResponseEntity<UserEntity> allUsers() {
        System.out.println("me route called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // System.out.println("authentication is " + authentication);
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        // System.out.println("current user is " + currentUser);
        return ResponseEntity.ok(currentUser);

    }

    @GetMapping("me/balance")

    public ResponseEntity<Object> getMyBalance() {
        System.out.println("user balance called");
        BigDecimal bal = userService.fetchMyBalance();
        return ResponseEntity.ok(bal);

    }
    // create a user

    // @GetMapping("/{id}")
    // public Optional<UserEntity> findById(@PathVariable Long id) {
    // return userService.findById(id);
    // }
    @GetMapping("/user/email/{email}")
    public Optional<UserEntity> findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Object> findById(@PathVariable String id) {
        System.out.println("user id in findById is " + id);
        Optional<UserEntity> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(new DriverDto(userOptional.get().getId(), userOptional.get().getName()));
    }

    // update a user
    @PatchMapping("/update")
    public ResponseEntity<UserEntity> update(@RequestBody RegisterUserDto user) {
        System.out.println("dto in controller" + user.getName());
        return ResponseEntity.ok(userService.update(user));
    }

    // delete a user
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteById(@PathVariable String id) {
        System.out.println("user id about to be deleted is :" + id);
        Optional<UserEntity> userEntity_ = userService.findById(id);
        if (userEntity_.isEmpty()) {
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        }
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        // userService.deleteById(id)
    }

    @GetMapping(value = "/{userId}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> qrCodeFile(@PathVariable("userId") String userId) throws Exception {
        return new ResponseEntity<>(UserService.generateQRCodeImage(userId), HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/qrcodeSvg")
    public ResponseEntity<String> qrCodeSvg(@PathVariable("userId") String userId) throws Exception {
        return new ResponseEntity<>(UserService.getQRCodeSvg(userId, 700, 700, false), HttpStatus.OK);
    }

    @GetMapping("/{userId}/transactions")
    public ResponseEntity<Object[]> fetchUserTransactions(@PathVariable("userId") String userId,
            Optional<Integer> limit1, Optional<Integer> limit2) {
        if (limit1.isEmpty() || limit2.isEmpty()) {
            Object[] transactions = transactionService.fetchTxByUserId(userId, 0, 100);
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        }
        Object[] transactions = transactionService.fetchTxByUserId(userId, limit1.get(), limit2.get());
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("{id}/name")
    public ResponseEntity<Object> getNameById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getNameById(id));
    }

}
