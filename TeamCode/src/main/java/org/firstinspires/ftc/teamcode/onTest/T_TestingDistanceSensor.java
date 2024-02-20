package org.firstinspires.ftc.teamcode.onTest;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * А работает ли он вообще, в принципе?
 * подключили, написали его в Configure Robot, запустили этот опмод
 */

@TeleOp(name = "testing Distance Sensor", group = "alfa")
public class T_TestingDistanceSensor extends LinearOpMode {
    DistanceSensorModule dsens = new DistanceSensorModule(this);

    @Override
    public void runOpMode(){
        dsens.initDistanceSensor();

        waitForStart();
        while (opModeIsActive()){
            dsens.telemetryDistance();
            telemetry.update();
        }
    }
}
