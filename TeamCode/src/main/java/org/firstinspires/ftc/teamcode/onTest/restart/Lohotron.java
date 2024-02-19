package org.firstinspires.ftc.teamcode.onTest.restart;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Lohotron {
    private Servo perevorot, main, claw;
    private LinearOpMode lohotronOpMode = null; //объект описывающий опмод, в котором будет использоваться наш лохотрон
    private boolean down = false, mid = false, clawClosed = false;
    public Lohotron(LinearOpMode opMode){
        lohotronOpMode = opMode;
    }

    public void initLohotron(HardwareMap hwMap){
        perevorot = hwMap.servo.get("lohotron");
        main =      hwMap.servo.get("lohotronMain");
        claw =      hwMap.servo.get("zahvat");

        openClaw();
        armMid();

        lohotronOpMode.telemetry.addLine("Lohotron ready!");
    }

    public void lohotronTelemetry(){
        //lohotronOpMode.telemetry.addData("lohotron ", down ? "opushen" : "podniat");
        lohotronOpMode.telemetry.addData("lohorton ", down ? "opushen" : mid ? "mid" : "podniat");
        lohotronOpMode.telemetry.addData("lohotron position: ", perevorot.getPosition());
        lohotronOpMode.telemetry.addData("lohotronMain position: ", main.getPosition());
        lohotronOpMode.telemetry.addData("zahvat position: ", claw.getPosition());
        lohotronOpMode.telemetry.addData("zahvacheno: ", isClawClosed());
    }

    public void armRaiser(){
        main.setPosition(0.66);     //поменять на 0.5 чтобы было параллельно заднику         //0.64 - касается распорки-ограничителя
        lohotronOpMode.sleep(150);
        perevorot.setPosition(0);       //а вот тут конструкцию надо менять... или серву на 270 поставить!

        lohotronOpMode.sleep(75);
        down = false;
        mid = false;
    }
    public void armLowerer(){
        perevorot.setPosition(0.905);            ////Тестить это
        lohotronOpMode.sleep(150);
        main.setPosition(0.03);

        lohotronOpMode.sleep(75);
        down = true;
        mid = false;
    }
    public void armMid(){
        main.setPosition(0.1);
        perevorot.setPosition(0.95);

        lohotronOpMode.sleep(225);
        down = false;
        mid = true;
    }

    boolean armRaised = false;
    boolean armMid = false;

    public void armLogicalRaise_Lower(){
        if(!armRaised) armRaiser(); else armLowerer();
        armRaised = !armRaised;
        armMid = false;                                     //вот это попросила Юля. оно делает мид, когда нажимаешь на "y"
    }

    public void armLogicalMid_Lower(){
        if(!armMid) armMid(); else armLowerer();
        armMid = !armMid;
        armRaised = false;                                  //вот это попросила Юля. оно делает вверх, когда нажимаешь на "a"
    }

    /**
     * держать пиксель
     */
    public void closeClaw(){
        claw.setPosition(0.1);
        clawClosed = true;
    }

    /**
     * отпустить пиксель
     */
    public void openClaw(){
        claw.setPosition(0);
        clawClosed = false;
    }
    public boolean isClawClosed(){
        return clawClosed;
    }
}
