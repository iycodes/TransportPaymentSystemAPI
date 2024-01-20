package com.mkyong.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mkyong.model.Userr;
import com.mkyong.repository.UserRepository;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.ViewBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.time.LocalDate;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<Userr> findAll() {
        return userRepository.findAll();
    }

    public Optional<Userr> findById(Long id) {
        return userRepository.findById(id);
    }

    public Userr save(Userr book) {
        return userRepository.save(book);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public List<Userr> findByName(String title) {
        return userRepository.findByName(title);
    }

    public static BufferedImage generateQRCodeImage(String userId) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(userId, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
    public static BufferedImage getQRCode(String data, int width,
                                          int height) {
        try {
            Hashtable<EncodeHintType, Object> hintMap = new Hashtable<EncodeHintType, Object>();

            hintMap.put(EncodeHintType.ERROR_CORRECTION,
                    ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode (data,
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
        }
        catch (WriterException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting QR Code");
        }

    }
    public static String getQRCodeSvg(String data, int width,
                                      int height, boolean withViewBox) {
        try {
            SVGGraphics2D g2 = new SVGGraphics2D(width, height);
            BufferedImage qrCodeImage = getQRCode(data);
            g2.drawImage(qrCodeImage, 0,0, width, height, null);

            ViewBox viewBox = null;
            if ( withViewBox ){
                viewBox = new ViewBox(0,0,width,height);
            }
            return g2.getSVGElement(null, true, viewBox, null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//    public List<Userr> findByCreatedAfterDate(LocalDate date) {
//        return userRepository.findByCreatedAfterDate(date);
//    }



}
