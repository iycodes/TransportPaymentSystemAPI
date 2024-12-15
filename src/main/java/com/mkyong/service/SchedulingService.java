package com.mkyong.service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.mkyong.model.TransactionEntity;
import com.mkyong.model.dtos.TestJsonDto;
import com.mkyong.model.dtos.UpdateTxWebhookDto;
import com.mkyong.model.dtos.VerifyEmailDto;
import com.mkyong.model.dtos.WebhookData;
import com.mkyong.repository.TransactionRepository;
import com.mkyong.responses.VerifyEmailResponse;
import java.util.Timer;
import java.util.TimerTask;
import reactor.core.publisher.Mono;

@Component
public class SchedulingService {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SchedulingService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;
    // private static final SimpleDateFormat dateFormat = new
    // SimpleDateFormat("HH:mm:ss");

    // public void reportCurrentTime() {
    // log.info("The time is now {}", dateFormat.format(new Date()));
    // }

    // @Bean
    public WebClient webClient() {

        // WebClient webClient = WebClient.builder()
        // .baseUrl("https://jsonplaceholder.typicode.com")
        // // .defaultCookie("cookie-name", "cookie-value")
        // .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE,
        // MediaType.APPLICATION_JSON_VALUE,
        // "Authorization", "Bearer
        // FLWSECK-615a03d657efe4057bd3d9dad05d47ce-193bd259578vt-X")
        // .defaultUriVariables(Collections.singletonMap("url",
        // "https://jsonplaceholder.typicode.com"))
        // .build();
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.flutterwave.com/v3/transactions")
                // .defaultCookie("cookie-name", "cookie-value")
                .defaultHeader(
                        "Authorization", "Bearer FLWSECK-615a03d657efe4057bd3d9dad05d47ce-193bd259578vt-X")
                .defaultUriVariables(Collections.singletonMap("url",
                        "https://api.flutterwave.com/v3/transactions"))
                .build();
        return webClient;
    }

    // public void afunc(int i) {
    // System.out.println("task running, index " + i);
    // Mono<ResponseEntity<TestJsonDto>> request = webClient().get()
    // .uri("/posts/" + (i + 1))
    // .accept(MediaType.APPLICATION_JSON)
    // .retrieve().toEntity(TestJsonDto.class);
    // ResponseEntity<TestJsonDto> response = request.block();
    // System.out.println("task done index is " + i);

    // };

    @Scheduled(fixedDelay = 1800000)
    // @Scheduled(cron = "0 * * * * *")
    public void checkPendingTransactions() {
        log.info("scheduled application ran");
        List<TransactionEntity> pendingTx = transactionRepository.checkPendingTx();

        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        int index = 0;
        AtomicInteger i = new AtomicInteger(0);
        exec.scheduleAtFixedRate(() -> {
            TransactionEntity tx = pendingTx.get(i.get());
            String txId = tx.getFintech_tx_id();
            Mono<ResponseEntity<UpdateTxWebhookDto>> request = webClient().get()
                    .uri("/" + txId + "/verify")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve().toEntity(UpdateTxWebhookDto.class);
            try {
                ResponseEntity<UpdateTxWebhookDto> response = request.block();
                if (i.get() == 0) {
                    System.out.println("response is " + response.getBody().toString());
                }
                transactionService.updateTxViaWebhook(response.getBody().getData());
            } catch (Exception e) {
                // TODO: handle exception
                System.err.println(e);
                System.out.println("error fetching api request in scheduler index is " + i.get());
            }

            System.out.println("i = " + i);
            i.incrementAndGet();
            if (i.get() == pendingTx.size()) {
                System.out.println("exec at fixed rate shut down");
                exec.shutdownNow();
                return;
            }
        }, 0, 4, TimeUnit.SECONDS);
        // for (int i = 0; i < pendingTx.size(); i++) {
        // final int ix = i;
        // try {
        // afunc(ix);
        // } catch (Exception e) {
        // }

        // if (i == 0) {
        // System.out.println("i = 0");
        // // timer2.schedule(timerTask2, 0);
        // continue;
        // }
        // if (i % 3 == 0) {
        // System.err.println("thread sleeping");
        // try {
        // // Thread.sleep(3000);
        // TimeUnit.SECONDS.sleep(3);
        // } catch (Exception e) {
        // // TODO: handle exception
        // System.err.println("Error putting thread to sleep");

        // }
        // ;
        // System.err.println("thread done sleeping");
        // }
        // System.out.println("i is " + i);

        // }

    }

    public List<TransactionEntity> checkPendingTransactions2() {
        log.info("scheduled application ran");
        List<TransactionEntity> pendingTx = transactionRepository.checkPendingTx();
        // System.out.println("");
        return pendingTx;

    }
}
