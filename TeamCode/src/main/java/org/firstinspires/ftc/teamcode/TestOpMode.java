package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name="Test", group="Iterative OpMode")
public class BasicOpMode_Iterative extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    private DcMotor arm = null;

    private Servo shoulder = null;
    private Servo elbo = null;
    private Servo hook = null;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive  = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        arm = hardwareMap.get(DcMotor.class, "arm");
        shoulder = hardwareMap.get(Servo.class,"shoulder");
        elbo = hardwareMap.get(Servo.class, "elbo");
        hook = hardwareMap.get(Servo.class, "hook");


        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
        arm.setDirection(DcMotorSimple.Direction.FORWARD);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
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
        // Setup a variable for each drive wheel to save power level for telemetry



        telemetry.addData("Status", "Run Time: " + runtime.toString());


        double rotation = -gamepad1.left_stick_x;
        double movement = -gamepad1.left_stick_y;
        double strafing = -gamepad1.right_stick_x;
        double uppies = -gamepad2.left_stick_y;

        frontLeftDrive.setPower(0.75 * signedSquare(rotation + strafing + movement));
        frontRightDrive.setPower(0.75 * signedSquare(rotation - strafing - movement));
        backLeftDrive.setPower(0.75 * signedSquare(rotation - strafing + movement));
        backRightDrive.setPower(0.75 * signedSquare(rotation + strafing - movement));

        if (uppies > 0.05){
            arm.setPower(.5);
        } else if (uppies < -0.05) {
            arm.setPower(-.5);
        } else {
            arm.setPower(0);
        }

        shoulder.setPosition(shoulder.getPosition() + (gamepad2.left_stick_x * 0.01));


        if (gamepad2.x) {
            elbo.setPosition(elbo.getPosition() +0.001);
        }
        if (gamepad2.y) {
            elbo.setPosition(elbo.getPosition() -0.001); //TODO: increase speed
        }
        telemetry.addData("Position", elbo.getPosition());

        if (gamepad2.right_bumper) {
            hook.setPosition(0.5);}
        if (gamepad2.left_bumper) {
            hook.setPosition(0.5); }
        telemetry.addData("Position2", hook.getPosition());
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    //takes a value and multiplies it by its absolute value, then returns that (square function but keeps the sign)
    private double signedSquare(double x) {
        return x * Math.abs(x);
    }

}
