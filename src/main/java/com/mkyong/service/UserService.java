package com.mkyong.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import com.mkyong.helpers.Patcher;

import com.mkyong.model.UserEntity;

import com.mkyong.model.dtos.RegisterUserDto;

import com.mkyong.repository.UserRepository;

import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.ViewBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.List;

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

    public BigDecimal fetchMyBalance() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentEntity = (UserEntity) authentication.getPrincipal();
        return currentEntity.getBalance();

    }

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
