package net.novahc.smanagement.utils;

import java.awt.image.BufferedImage;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import com.github.sarxos.webcam.Webcam;
import javafx.scene.control.Label;

public class Decoder {
    public Decoder(Webcam webcam, Label decodeResult) {
        this.webcam = webcam;
        this.decodeResult = decodeResult;
    }

    private Webcam webcam;
    private Label decodeResult;

    public boolean isScanning() {
        return scanning;
    }

    public void setScanning(boolean scanning) {
        this.scanning = scanning;
    }

    private boolean scanning = false;

    public void start() {

        do {
            System.out.println("hi");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {

                if ((image = webcam.getImage()) == null) {
                    continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                    // fall thru, it means there is no QR code in image
                }
            }

            if (result != null) {
                decodeResult.setText(result.getText());
            }

        } while (true);
    }

    public void stop() {
        setScanning(false);
    }
    public void invokeSingleScan(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Result result = null;
        BufferedImage image = null;

        if (webcam.isOpen()) {

            if ((image = webcam.getImage()) == null) {
                return;
            }

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                result = new MultiFormatReader().decode(bitmap);
            } catch (NotFoundException e) {
                // fall thru, it means there is no QR code in image
            }
        }

        if (result != null) {
            decodeResult.setText(result.getText());
        }
    }
}
