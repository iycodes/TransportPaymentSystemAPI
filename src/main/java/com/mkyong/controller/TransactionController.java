package com.mkyong.controller;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mkyong.model.TransactionEntity;
import com.mkyong.model.UserEntity;
import com.mkyong.model.dtos.FundAccountDto;
import com.mkyong.model.dtos.NewTxDto;
import com.mkyong.model.dtos.UpdateTxDto;
import com.mkyong.model.dtos.UpdateTxWebhookDto;
import com.mkyong.model.dtos.Transaction.MakePaymentDto;
import com.mkyong.model.dtos.Transaction.PaymentResponseDto;
import com.mkyong.model.dtos.Transaction.PaymentSuccessDto;
import com.mkyong.model.dtos.Transaction.TxDto;
import com.mkyong.model.enums.TxStatus;
import com.mkyong.responses.FundAccountResponse;
import com.mkyong.responses.NewTxResponse;
import com.mkyong.service.SchedulingService;
import com.mkyong.service.TransactionService;
import com.mkyong.service.UserService;

import jakarta.transaction.UserTransaction;

@RestController
@RequestMapping("/tx")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @Autowired
    private SchedulingService schedulingService;

    // @Autowired
    // private TransactionService
    // @PostMapping("/paydriver")
    // public ResponseEntity payDriver(@RequestBody MakePaymentDto dto) {
    // Optional<UserEntity> recepient = userService.findById(dto.getReceiverId());
    // Optional<UserEntity> sender = userService.findById(dto.getSenderId());
    // if (recepient.isEmpty() || sender.isEmpty()) {
    // System.out.println("user not found!");
    // return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    // }
    // PaymentResponseDto res = transactionService.payDriver(dto);
    // if (res.getTxStatus() == TxStatus.failed) {
    // return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // return new ResponseEntity<PaymentResponseDto>(res, HttpStatus.OK);
    // }

    @PostMapping("/makePayment")
    public ResponseEntity<PaymentResponseDto> makePayment(@RequestBody MakePaymentDto dto) {

        // Optional<UserEntity> recepient = userService.findById(dto.getReceiverId());
        // Optional<UserEntity> sender = userService.findById(dto.getSenderId());
        // if (recepient.isEmpty()) {
        // return new ResponseEntity<PaymentResponseDto>(HttpStatus.BAD_REQUEST);
        // }
        PaymentResponseDto res = transactionService.makePayment(dto);
        if (res.getTxStatus() == TxStatus.success) {
            return new ResponseEntity<PaymentResponseDto>(res, HttpStatus.OK);
        } else {
            System.out.println("error is " + res.getErrorMsg());
            return new ResponseEntity<PaymentResponseDto>(res, res.getErrorCode());
        }

    }

    @PostMapping("/testingPay")
    public PaymentResponseDto testingPay(@RequestBody MakePaymentDto dto) {
        PaymentResponseDto res = transactionService.testingPay(dto);
        return res;
    }

    // @PostMapping("/check_duplicate_tx")
    // public ResponseEntity<Object> testingCheckDuplicate() {

    // boolean val = transactionService.checkDuplicateTransaction();
    // MakePaymentDto makePaymentDto = new MakePaymentDto();
    // makePaymentDto.setSenderId(null);
    // if (val == true) {
    // return new ResponseEntity<>(HttpStatus.CONFLICT);
    // } else {
    // return ResponseEntity.ok("");
    // }
    // }

    @GetMapping("/fetchTx/{id}")
    public ResponseEntity fetchTransaction(@PathVariable("id") Optional<String> txid) {
        System.out.println("tx id is " + txid.get());
        if (txid.isEmpty())
            return new ResponseEntity<>("Invalid Params", HttpStatus.BAD_REQUEST);
        Optional<TransactionEntity> tx = transactionService.fetchTx(txid.get());
        if (tx.isEmpty())
            return new ResponseEntity<>("Transaction not found!", HttpStatus.NOT_FOUND);
        TransactionEntity tx_ = tx.get();
        TxDto txDto = new TxDto(tx_.getId(), tx_.getSenderId(), tx_.getReceiverId(), tx_.getCreatedAt(),
                tx_.getStatus(), tx_.getType(), null);
        return new ResponseEntity<TxDto>(txDto, HttpStatus.OK);

    }

    @GetMapping("/me/all")
    public ResponseEntity<Object[]> fetchTransactions(@RequestParam(defaultValue = "0") int limit1,
            @RequestParam(defaultValue = "25") int limit2) {
        System.out.println("tx/me/all api called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // System.out.println("user is " + authentication.getPrincipal());
        UserEntity user = (UserEntity) authentication.getPrincipal();
        // TransactionEntity[] transactions =
        // if (limit1.isEmpty() || limit2.isEmpty()) {
        // TransactionEntity[] transactions = transactionService.fetchTxByUserId(userId,
        // 0, 100);
        // return new ResponseEntity<>(transactions, HttpStatus.OK);
        // }
        Object[] userTransactions = transactionService.fetchTxByUserId(user.getId(), limit1, limit2);
        return ResponseEntity.ok(userTransactions);
    }

    @PostMapping("/fundUserAccount")
    public ResponseEntity<FundAccountResponse> fundUserAccount(@RequestBody FundAccountDto dto) {
        FundAccountResponse res = transactionService.fundUserAccount(dto);
        return new ResponseEntity<>(res, res.getStatusCode());

        // if(res.getError()!=null){
        // return new ResponseEntity<>(res, )
        // }
    }

    @GetMapping("/checkPendingTx")
    ResponseEntity<List<TransactionEntity>> checkPendingTransactions() {
        return new ResponseEntity<>(schedulingService.checkPendingTransactions2(), HttpStatus.OK);
    }

    @PostMapping("/fundAccountPending")
    public ResponseEntity<NewTxResponse> fundAccountPending(@RequestBody NewTxDto dto) {
        NewTxResponse newTxResponse = transactionService.newTx(dto);
        return new ResponseEntity<>(newTxResponse, newTxResponse.getStatusCode());
    }

    @PostMapping("/updateTx")
    public ResponseEntity<Object> updateTx(@RequestBody UpdateTxDto dto) {
        return transactionService.updateTx(dto);
    }

    @PostMapping("webhook/updateTx")
    public ResponseEntity<Object> updateTxViaWebook(@RequestBody UpdateTxWebhookDto dto,
            @RequestHeader("verif-hash") String webhookSecretHash) {
        // UpdateTxDto updateTxDto = new UpdateTxDto(dto.get, null, null)
        if (webhookSecretHash == "riide") {
            System.out.println("webhook from flutterwave");
        }
        return transactionService.updateTxViaWebhook(dto.getData());
    }
}
