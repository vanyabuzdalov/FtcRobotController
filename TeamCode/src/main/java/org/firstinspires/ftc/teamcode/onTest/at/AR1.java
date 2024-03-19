package org.firstinspires.ftc.teamcode.onTest.at;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.EOCVSamples.PhantomSamples.Methods_for_OpenCV;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.onTest.ForTheNationalChampionship.Basic.Catch;
import org.firstinspires.ftc.teamcode.onTest.ForTheNationalChampionship.Basic.Capture;
import org.firstinspires.ftc.teamcode.onTest.ForTheNationalChampionship.Sensored.MegaDriveTrain;
import org.firstinspires.ftc.teamcode.onTest.ForTheNationalChampionship.Sensors.ColorSensorModule;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous
public class AR1 extends LinearOpMode {
    MegaDriveTrain base = new MegaDriveTrain(this);
    Catch pix = new Catch(this);
    Capture lohotron = new Capture(this);

    private static int valLeft = -1;
    private static int valRight = -1;
    public OpenCvWebcam phoneCam;
    ElapsedTime timer = new ElapsedTime();

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
            base.setMaximumSpeed(0.5);                                  //MAX SPEED для того, чтобы энкодер и цвет считывались +- нормально (хз, сработает ли)
            valLeft = Methods_for_OpenCV.getValLeft();
            valRight = Methods_for_OpenCV.getValRight();
            phoneCam.stopStreaming();

            //timer.reset();
            //подкатываем к точке сброса фиолетового

            base.encoderRun(0, -0.35, -950);


            if (valLeft == 255) {           //центр
                //надо ли небольшой отъезд назад? (датчик же не спереди робота идет)
                base.encoderRun(0, 0.5, 100);
                pix.grab();
                base.encoderRun(0, -1, 200); //(1219) мы проезжаем две плитки (4 фута == 1219мм) и выравниваемся об стенку
                base.imuTurn(0.7, -90);
            } else if (valRight == 255) {   //право
                //надо ли небольшой отъезд назад? (датчик же не спереди робота идет)base.encoderRun(0, 0.7, 100);
                /*base.imuTurn(0.7, -90);
                sleep(1000);
                base.encoderRun(0, -0.5, -115);
                sleep(1000);
                pix.grab();
                sleep(1000);

                base.encoderRun(0, 0.5, 200);//уходим от пикселя
                sleep(1000);
                base.imuTurn(0.7, 0);
                sleep(1000);
                base.encoderRun(0, 0.5, 400);//проезд, чтобы объехать пиксель
                sleep(1000);
                base.imuTurn(0.7, -90);//поворачиваемся на доску
                */sleep(1000);


            } else {                        //лево
                //надо ли небольшой отъезд назад? (датчик же не спереди робота идет)
                base.encoderRun(0,0.7, 200);
                base.imuTurn(0.7, 90);
                base.encoderRun(0, -0.5, -150);
                pix.grab();
                base.encoderRun(0, 0.5, 100);
                base.imuTurn(0.7, 0);
                base.encoderRun(0, -1, 200); //(1219) мы проезжаем две плитки (4 фута == 1219мм) и выравниваемся об стенку
                base.imuTurn(0.7, -90);
            }

            //подкатываем к доске
            //base.encoderRun(0, -1, 200); //(1219) мы проезжаем две плитки (4 фута == 1219мм) и выравниваемся об стенку
            //base.imuTurn(0.7, -90);
            base.encoderRun(0, -1, -1050); //base.colorRun(0, 1, 0, ColorSensorModule.colorsField.BLUE); //едем до разметочной линии перед доской

            //подкатываем к правильной колонке
            if (valLeft == 255) {           //центр
                //base.imuSteerEncoder(0.7, 0, -915);   //полторы клетки влево
            } else if (valRight == 255) {   //право
                base.imuSteerEncoder(0.5, 0, 0, -90, -500);  //чуть больше, чем полторы клетки влево
            } else {                        //лево
                base.imuSteerEncoder(0.5, 0, 0, -90, -200);   //чуть меньше, чем полторы клетки влево
            }

            //роняем запад
            lohotron.armRaiser();
            sleep(500);
            lohotron.closeClaw();
            sleep(1000);        //ждем, пока упадет желтый пиксель
            base.encoderRun(0, 0.4, 50);
            sleep(250);
            lohotron.armMid();

            //паркуемся
            if (valLeft == 255) {           //центр
                base.imuSteerEncoder(0.5, 0, 0, -90, 1015);   //полторы клетки вправо
            } else if (valRight == 255) {   //право
                base.imuSteerEncoder(0.5, 0, 0, -90, 600);  //чуть больше, чем полторы клетки вправо
            } else {                        //лево
                base.imuSteerEncoder(0.5, 0, 0, -90, 600);   //чуть меньше, чем полторы клетки вправо
            }

            base.encoderRun(0, -0.7, -250);
            //base.imuTurn(1, 90);     //hold position
        }
    }
}
