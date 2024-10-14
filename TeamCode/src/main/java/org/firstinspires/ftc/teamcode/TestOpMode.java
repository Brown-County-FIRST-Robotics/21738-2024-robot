

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;



@TeleOp(name="Emmit Test Program", group="Iterative OpMode")
public class BasicOpMode_Iterative extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    //    private DcMotor frontLeftDrive = null;
//    private DcMotor frontRightDrive = null;
//    private DcMotor backRightDrive = null;
//    private DcMotor backLeftDrive = null;
    private DcMotor arm = null;
    private Servo shoulder = null;

    @Override
    public void init() { // when you press start, this code runs once
        telemetry.addData("Status", "Initialized");

//        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
//        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
//        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
//        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        arm = hardwareMap.get(DcMotor.class, "arm");
        shoulder = hardwareMap.get(Servo.class,"shoulder");


//        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
//        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
//        backRightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
//        backLeftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        arm.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {
        double rotate = -gamepad1.left_stick_x;
        double drive = -gamepad1.left_stick_y;
        double strafe = -gamepad1.right_stick_x;
        double uppies = -gamepad2.left_stick_y;
//        double twists = -gamepad2.right_stick_x;
        // double denominator = Math.max(Math.abs(rotate) + Math.abs(strafe) + Math.abs(drive_), .75);
        double frontLeftPower = (.75 * .75 * rotate + strafe + drive);// / denominator;
        double backLeftPower = (.75 * .75 * rotate - strafe + drive);// / denominator;
        double frontRightPower = (.75 * .75 * rotate - strafe - drive);// / denominator;
        double backRightPower = (.75 * .75 * rotate + strafe - drive);// / denominator;
//            frontLeftDrive.setPower(frontLeftPower);
//            frontRightDrive.setPower(frontRightPower);
//            backLeftDrive.setPower(backLeftPower);
//            backRightDrive.setPower(backRightPower);

        if (uppies > 0.05){
            arm.setPower(.5);
        } else if (uppies < -0.05) {
            arm.setPower(-.5);
        } else {
            arm.setPower(0);
        }

        shoulder.setPosition(shoulder.getPosition() + (gamepad2.left_stick_x * 0.01));
//        if (twists > 0.05){
//            shoulder.setPosition(shoulder.getPosition()+0.1);
//        }else if (twists < -0.05){
//            shoulder.setPosition(shoulder.getPosition()-0.1);
//        }else {
//            shoulder.setPosition(0);
//        }

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {



    }

}
