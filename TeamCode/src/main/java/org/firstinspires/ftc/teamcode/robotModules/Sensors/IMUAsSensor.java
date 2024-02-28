package org.firstinspires.ftc.teamcode.robotModules.Sensors;

import com.qualcomm.hardware.bosch.BNO055IMUNew;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class IMUAsSensor {
    BNO055IMUNew imu = null;

    /**
     * Настроиваем ориентацию Контрол/Экспеншн хаба в пространстве
     */
    RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.RIGHT;       //направление того, как установлен Rev Hub (logo)
    RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.DOWN;   //направление того, как установлен Rev Hub (USB)
    RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

    LinearOpMode imuOpMode;

    public IMUAsSensor(LinearOpMode opMode) {
        imuOpMode = opMode;
    }

    public void initIMU() {
        imu = imuOpMode.hardwareMap.get(BNO055IMUNew.class, "imu");
        imu.initialize(new com.qualcomm.robotcore.hardware.IMU.Parameters(orientationOnRobot));
        imu.resetYaw();
    }

    public double getHeading() {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        return orientation.getYaw(AngleUnit.DEGREES);
    }
}
