package net.novahc.smanagement.utils;

import com.github.eduramiba.webcamcapture.drivers.NativeDriver;
import com.github.eduramiba.webcamcapture.drivers.WebcamDeviceWithBufferOperations;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDevice;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageReader;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class WebcamManager {
    public Decoder getDecoder() {
        return decoder;
    }
    private Webcam camera;
    private Decoder decoder;
    private static final Logger LOG = LoggerFactory.getLogger(WebcamManager.class);
    public static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(4);


    /**
     * @param index of selected camera
     */
    public void setCamera(int index){
        camera = Webcam.getWebcams().get(index);
    }
    public WebcamManager(){
        Webcam.setDriver(new NativeDriver());
    }

    public void init(ImageView imageView, Label decodeResult){
        decoder = new Decoder(camera, decodeResult);
        final WebcamDevice device = camera.getDevice();
        LOG.info("Found camera: {}, device = {}", camera, device);

        final int width = device.getResolution().width;
        final int height = device.getResolution().height;
        final WritableImage fxImage = new WritableImage(width, height);
        Platform.runLater(() -> {
            imageView.setImage(fxImage);
            centerImage(imageView);
        });

        camera.getLock().disable();
        camera.open();
        if (device instanceof WebcamDeviceWithBufferOperations) {
            final WebcamDeviceWithBufferOperations dev = ((WebcamDeviceWithBufferOperations) device);
            EXECUTOR.scheduleAtFixedRate(new Runnable() {
                private long lastFrameTimestamp = -1;

                @Override
                public void run() {
                    if (dev.updateFXIMage(fxImage, lastFrameTimestamp)) {
                        lastFrameTimestamp = dev.getLastFrameTimestamp();
                    }

                }
            }, 0, 16, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Centers the image in the webcamWrapper
     * @author https://stackoverflow.com/users/4880243/againpsychox
     * @param imageView
     */
    private void centerImage(ImageView imageView) {
        Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setTranslateX((imageView.getFitWidth() - w) / 2);
            imageView.setTranslateY((imageView.getFitHeight() - h) / 2);

        }
    }
    /**
     * @return ArrayList of all available webcams
     */
    public List<Webcam> getWebcams(){
        List<Webcam> meow = Webcam.getWebcams();
        return Webcam.getWebcams();
    }
    public void startDecoding(){
        decoder.start();
    }
    public void stopDecoding(){
        decoder.stop();
    }
}
