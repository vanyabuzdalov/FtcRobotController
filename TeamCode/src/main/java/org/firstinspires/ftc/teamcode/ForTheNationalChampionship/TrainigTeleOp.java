package org.firstinspires.ftc.teamcode.ForTheNationalChampionship;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.ForTheNationalChampionship.Sensored.BackBoard;
import org.firstinspires.ftc.teamcode.ForTheNationalChampionship.Basic.Catch;
import org.firstinspires.ftc.teamcode.ForTheNationalChampionship.Basic.Hook;
import org.firstinspires.ftc.teamcode.ForTheNationalChampionship.Basic.HookMotor;
import org.firstinspires.ftc.teamcode.ForTheNationalChampionship.Basic.Elevator;
import org.firstinspires.ftc.teamcode.ForTheNationalChampionship.Basic.Capture;
import org.firstinspires.ftc.teamcode.ForTheNationalChampionship.Basic.Plane;

@TeleOp(name = "TeleTraining", group = "1alfa")
public class TrainigTeleOp extends LinearOpMode {

    DcMotor TR, TL, BR, BL;

    double x, y, r;

    @Override
    public void runOpMode() {

        TL = hardwareMap.dcMotor.get("leftFront");
        TR = hardwareMap.dcMotor.get("rightFront");
        BL = hardwareMap.dcMotor.get("leftRear");
        BR = hardwareMap.dcMotor.get("rightRear");

        TL.setDirection(DcMotorSimple.Direction.FORWARD);
        TR.setDirection(DcMotorSimple.Direction.FORWARD);
        BL.setDirection(DcMotorSimple.Direction.FORWARD);
        BR.setDirection(DcMotorSimple.Direction.FORWARD);

        TL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        TR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Ready to start");
        waitForStart();
        while (opModeIsActive()) {
            x = -gamepad1.left_stick_x;
            y = -gamepad1.left_stick_y;
            r = (gamepad1.right_trigger - gamepad1.left_trigger);

            TR.setPower(-x - y + r);
            BR.setPower(x - y + r);
            BL.setPower(x + y + r);
            TL.setPower(-x + y + r);

            telemetry.update();

        }
    }
}
