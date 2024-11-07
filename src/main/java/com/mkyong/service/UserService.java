package com.mkyong.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mkyong.configs.WebConfig;
import com.mkyong.helpers.Patcher;
import com.mkyong.model.TransactionEntity;
import com.mkyong.model.UserEntity;
import com.mkyong.model.VerificationCodeEntity;
import com.mkyong.model.dtos.RegisterUserDto;
import com.mkyong.model.dtos.VerifyEmailDto;
import com.mkyong.model.dtos.Transaction.MakePaymentDto;
import com.mkyong.model.dtos.Transaction.PaymentResponseDto;
import com.mkyong.model.enums.TxStatus;
import com.mkyong.repository.TransactionRepository;
import com.mkyong.repository.UserRepository;
import com.mkyong.repository.VerificationCodeRepository;
import com.mkyong.responses.VerifyEmailResponse;

import reactor.core.publisher.Mono;

import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.ViewBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    // @Autowired
    // private VerificationCodeRepository verificationCodeRepository;

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> findById(String id) {
        return userRepository.findById(id);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    public List<UserEntity> findByName(String title) {
        return userRepository.findByName(title);
    }

    public Optional<String> getNameById(String id) {
        return userRepository.getNameById(id);
    }

    // WebClient client = WebClient.create("http://localhost:3002");
    // public void handleVerificationCode(VerifyEmailDto dto) {
    // Random random = new Random();

    // Optional<VerificationCodeEntity> verificationCodeEntity =
    // verificationCodeRepository
    // .findByEmail(dto.getRecepientEmail());
    // if (verificationCodeEntity.isEmpty()) {
    // System.out.println("No verification code found in server");
    // int randomNumber = random.nextInt(1000, 9999);
    // verificationCodeRepository.save(new VerificationCodeEntity(randomNumber,
    // dto.getRecepientEmail()));
    // dto.setCode(randomNumber);
    // } else if
    // (verificationCodeEntity.get().getExpiresIn().isBefore(LocalDateTime.now())) {
    // System.out.println("verification code found in server but expired!");
    // int randomNumber = random.nextInt(1000, 9999);
    // verificationCodeEntity.get().setCode(randomNumber);
    // verificationCodeEntity.get().setExpiresIn(LocalDateTime.now().plusMinutes(30));
    // //
    // verificationCodeRepository.verificationCodeRepository.deleteById(verificationCodeEntity.get().getId());
    // verificationCodeRepository.save(verificationCodeEntity.get());
    // dto.setCode(randomNumber);
    // } else {
    // System.out.println("An existing verification code was found in the server");
    // dto.setCode(verificationCodeEntity.get().getCode());
    // }
    // }

    // public boolean sendVerificationEmail(VerifyEmailDto dto) {
    // handleVerificationCode(dto);
    // try {
    // Mono<ResponseEntity<VerifyEmailResponse>> request =
    // webConfig.webClient().post()
    // .uri("/send_mail").accept(MediaType.APPLICATION_JSON).body(Mono.just(dto),
    // VerifyEmailDto.class)
    // .retrieve().toEntity(VerifyEmailResponse.class);
    // ResponseEntity<VerifyEmailResponse> response = request.block();
    // if (response.getStatusCode().value() < 300) {
    // // System.out.println("mail sent succesfully");
    // return true;
    // } else {
    // // System.out.println("error sending the mail oo");
    // return false;
    // }
    // } catch (Exception e) {
    // System.err.println(e);
    // return false;
    // }
    // }

    // public boolean validateVerificationCode(String email, int codeInputByUser) {
    // Optional<VerificationCodeEntity> verificationCodeEntity =
    // verificationCodeRepository
    // .findByEmail(email);
    // if (verificationCodeEntity.isEmpty())
    // return false;
    // else if (verificationCodeEntity.get().getCode() == codeInputByUser) {
    // verificationCodeRepository.deleteById(verificationCodeEntity.get().getId());
    // return true;
    // } else {
    // return false;
    // }
    // }

    public static BufferedImage generateQRCodeImage(String userId) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(userId, BarcodeFormat.QR_CODE, 500, 500);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public static BufferedImage getQRCode(String data, int width,
            int height) {
        try {
            Hashtable<EncodeHintType, Object> hintMap = new Hashtable<EncodeHintType, Object>();

            hintMap.put(EncodeHintType.ERROR_CORRECTION,
                    ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(data,
                    BarcodeFormat.QR_CODE, width, height, hintMap);
            int CrunchifyWidth = byteMatrix.getWidth();

            BufferedImage image = new BufferedImage(CrunchifyWidth,
                    CrunchifyWidth, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
            graphics.setColor(Color.BLACK);
            for (int i = 0; i < CrunchifyWidth; i++) {
                for (int j = 0; j < CrunchifyWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            return image;
        } catch (WriterException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting QR Code");
        }

    }

    public static String getQRCodeSvg(String data, int width,
            int height, boolean withViewBox) {
        try {
            SVGGraphics2D g2 = new SVGGraphics2D(width, height);
            BufferedImage qrCodeImage = getQRCode(data, 300, 300);
            g2.drawImage(qrCodeImage, 0, 0, width, height, null);

            ViewBox viewBox = null;
            if (withViewBox) {
                viewBox = new ViewBox(0, 0, width, height);
            }
            return g2.getSVGElement(null, true, viewBox, null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserEntity update(RegisterUserDto dto) {
        UserEntity userData = userRepository.findByEmail(dto.getEmail()).orElseThrow();
        UserEntity newData = dto.toUserr();
        System.out.println(" user data update is  " + newData.getName());
        try {
            Patcher.userPatcher(userData, newData);
            System.out.println(" updated data update is  " + userData.getName());

        } catch (Exception e) {
            System.err.println("error patching data");
            e.printStackTrace();
        }
        return userRepository.save(userData);
    }

    // public PaymentResponseDto fundAccount(MakePaymentDto data) {

    // Optional<UserEntity> user = userRepository.findById(data.getSenderId());
    // BigDecimal userBalance = user.get().getBalance();
    // String txId = TransactionEntity.createId(data.getSenderId(),
    // data.getReceiverId());
    // if (user.isEmpty()) {
    // return new PaymentResponseDto(txId, TxStatus.FAILED, "User does not exist!");
    // }
    // if(userBalance.compareTo(userBalance))

    // }

    // public fundAccountViaCashier(MakePaymentDto data) {

    // Optional<UserEntity> poolAccount =
    // userRepository.findById(data.getSenderId());
    // BigDecimal poolBalance = poolAccount.get().getBalance();

    // Optional<UserEntity> user = userRepository.findById(data.getSenderId());
    // BigDecimal userBalance = user.get().getBalance();

    // try {
    // poolAccount.get().setBalance(userBalance);
    // user.get().setBalance(userBalance.add(data.getAmount()));

    // } catch (Exception e) {
    // // TODO: handle exception
    // }
    // }

    //
    // public List<UserEntity> findByCreatedAfterDate(LocalDate date) {
    // return userRepository.findByCreatedAfterDate(date);
    // }

}
