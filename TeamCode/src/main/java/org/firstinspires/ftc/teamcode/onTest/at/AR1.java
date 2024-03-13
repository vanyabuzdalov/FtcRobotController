package org.firstinspires.ftc.teamcode.onTest.at;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.EOCVSamples.PhantomSamples.Methods_for_OpenCV;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.robotModules.Basic.Catch;
import org.firstinspires.ftc.teamcode.robotModules.Basic.Lohotron;
import org.firstinspires.ftc.teamcode.robotModules.Sensored.GigaChadDriveTrain;
import org.firstinspires.ftc.teamcode.robotModules.Sensors.ColorSensorModule;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous
public class AR1 extends LinearOpMode {
    GigaChadDriveTrain base = new GigaChadDriveTrain(this);
    Catch pix = new Catch(this);
    Lohotron lohotron = new Lohotron(this);

    private static int valLeft = -1;
    private static int valRight = -1;
    public OpenCvWebcam phoneCam;

    @Override
    public void runOpMode() {
        base.initGigaChad();
        pix.initCatch();
        lohotron.initLohotron();

        Methods_for_OpenCV methodsForOpenCV = new Methods_for_OpenCV();
        int rows = methodsForOpenCV.getRows();
        int cols = methodsForOpenCV.getCols();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        phoneCam.openCameraDevice();
        phoneCam.setPipeline(new org.firstinspires.ftc.robotcontroller.EOCVSamples.PhantomSamples.Methods_for_OpenCV.StageSwitchingPipeline());
        phoneCam.startStreaming(rows, cols, OpenCvCameraRotation.UPRIGHT);

        Thread thread = new Thread(() -> {
            while (opModeInInit()) {
                telemetry.addData("Values", valLeft + "  " + valRight);
                telemetry.update();
                // visionPortall.telemetryAprilTag();
                valLeft = Methods_for_OpenCV.getValLeft();
                valRight = Methods_for_OpenCV.getValRight();
            }
        });
        FtcDashboard.getInstance().startCameraStream(phoneCam, 210);
        FtcDashboard.getInstance().getTelemetry();
        thread.start();

        waitForStart();
        if (opModeIsActive()) {
            valLeft = Methods_for_OpenCV.getValLeft();
            valRight = Methods_for_OpenCV.getValRight();
            phoneCam.stopStreaming();

            //подкатываем к точке сброса фиолетового
            base.colorRun(0, 0.5, 0, ColorSensorModule.colorsField.idkWtfIsThisColor);

            if (valLeft == 255) {           //центр
                //надо ли небольшой отъезд назад? (датчик же не спереди робота идет)
                base.encoderRun(0, 0.5, 100);
                pix.grab();
            } else if (valRight == 255) {   //право
                //надо ли небольшой отъезд назад? (датчик же не спереди робота идет)
                base.imuTurn(0.7, -90);
                base.encoderRun(0, 0.5, 100);
                pix.grab();
                base.encoderRun(0, 0.5, -100);
                base.imuTurn(0.7, 90);
            } else {                        //лево
                //надо ли небольшой отъезд назад? (датчик же не спереди робота идет)
                base.imuTurn(0.7, -90);
                base.encoderRun(0, 0.5, 100);
                pix.grab();
                base.encoderRun(0, 0.5, -100);
                base.imuTurn(0.7, 90);
            }

            //подкатываем к доске
            base.encoderRun(0, -1, 1219); //(1219) мы проезжаем две плитки (4 фута == 1219мм) и выравниваемся об стенку
            base.imuTurn(0.7, -90);
            base.encoderRun(0, -1, -882); //base.colorRun(0, 1, 0, ColorSensorModule.colorsField.BLUE); //едем до разметочной линии перед доской

            //подкатываем к правильной колонке
            if (valLeft == 255) {           //центр
                base.encoderRun(0.7, 0, -915);   //полторы клетки влево
            } else if (valRight == 255) {   //право
                base.encoderRun(0.7, 0, -1006);  //чуть больше, чем полторы клетки влево
            } else {                        //лево
                base.encoderRun(0.7, 0, -823);   //чуть меньше, чем полторы клетки влево
            }

            //роняем запад
            lohotron.armRaiser();
            lohotron.openClaw();
            sleep(1000);        //ждем, пока упадет желтый пиксель
            base.encoderRun(0, 0.4, 50);
            sleep(250);
            lohotron.armMid();

            //паркуемся
            if (valLeft == 255) {           //центр
                base.encoderRun(0.7, 0, 1015);   //полторы клетки вправо
            } else if (valRight == 255) {   //право
                base.encoderRun(0.7, 0, 1106);  //чуть больше, чем полторы клетки вправо
            } else {                        //лево
                base.encoderRun(0.7, 0, 923);   //чуть меньше, чем полторы клетки вправо
            }

            base.encoderRun(0, -0.7, -250);
            //base.imuTurn(1, 90);     //hold position
        }
    }
}
