package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;



@TeleOp(name="Test", group="Iterative OpMode")
public class TestOpMode extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    private DcMotor intake = null;
    private DcMotor arm = null;

    private Servo elbow = null;
    private Servo hook = null;
    private Servo wrist = null;
    private Servo hand = null;
    private Servo extender = null;
    private Servo twist = null;

    private DigitalChannel limit1 = null;
    private DigitalChannel teamColor = null;
    private DigitalChannel LED = null;
    private RevColorSensorV3 colorSensor = null;
    private DigitalChannel laser = null;
private DigitalChannel limitBack = null;
private DigitalChannel limitFront = null;
// Array for average color numbers to use in color sensor, arranged RGBA
    int[] redConst = {300, 88, 164, 184};
    int[] blueConst = {72, 305, 141, 172};
    int[] yellowConst = {485, 139, 577, 400};
    int[] blackConst = {58, 83, 101, 81};


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        arm = hardwareMap.get(DcMotor.class, "arm");
        intake = hardwareMap.get(DcMotor.class, "intake");
        elbow = hardwareMap.get(Servo.class, "elbow");
        wrist = hardwareMap.get(Servo.class, "wrist");
        hook = hardwareMap.get(Servo.class, "hook");
        hand = hardwareMap.get(Servo.class, "hand");
        extender = hardwareMap.get(Servo.class, "extender");
        twist = hardwareMap.get(Servo.class, "twist");
limitBack = hardwareMap.get(DigitalChannel.class, "limitBack");
limitFront = hardwareMap.get(DigitalChannel.class, "limitFront");
        limit1 = hardwareMap.get(DigitalChannel.class, "limit1");
        teamColor = hardwareMap.get(DigitalChannel.class, "teamColor");
        LED = hardwareMap.get(DigitalChannel.class, "LED");
        colorSensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");
        laser = hardwareMap.get(DigitalChannel.class, "laser");

        LED.setMode(DigitalChannel.Mode.OUTPUT);

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
        arm.setDirection(DcMotorSimple.Direction.FORWARD);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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

        double rotation = gamepad1.left_stick_y; //turning drive chassis
        double movement = -gamepad1.left_stick_x;//Forward/backward drive chassis
        double strafing = -gamepad1.right_stick_x;
        double uppies = gamepad2.left_stick_y;//vertical arm movement

        frontLeftDrive.setPower(-0.75 * signedSquare(rotation + strafing + movement));
        frontRightDrive.setPower(-0.75 * signedSquare(rotation - strafing - movement));
        backLeftDrive.setPower(-0.75 * signedSquare(rotation - strafing + movement));
        backRightDrive.setPower(-0.75 * signedSquare(rotation + strafing - movement));



        if (uppies > 0.05) {//vertical arm movement
            arm.setPower(.5);
        } else if (uppies < -0.05) {
            arm.setPower(-.5);
        } else {
           arm.setPower(0);
        }

        twist.setPosition(twist.getPosition() + (gamepad2.right_stick_x* 0.01));


        if (gamepad2.x) {
            elbow.setPosition(0.2);//closer to 0
        }
        if (gamepad2.y) {
            elbow.setPosition(0.65); //TODO: increase speed
        }
        telemetry.addData("Position", elbow.getPosition());//Shows the position of elbow on drive hub

        if (gamepad1.a) {//sets the hook to the "closed" position
            hook.setPosition(0.1);
        }
        if (gamepad1.b) {//sets the hook to the "open" position
            hook.setPosition(.6);
        }

        if (gamepad2.a) {
            wrist.setPosition(wrist.getPosition() + 0.01);
        }
        if (gamepad2.b) {
            wrist.setPosition(wrist.getPosition() - 0.01);

        } //increases speed
        telemetry.addData("PositionWrist", wrist.getPosition());
 //       hand.setPosition(hand.getPosition() + (gamepad1.right_stick_y * 0.01));

       if (gamepad1.x) {//grips blocks to take up to baskets
           hand.setPosition(0.8117);//Open
        } else if (gamepad1.y) {
            hand.setPosition(0.87);//closed position

        }

          LED.setState(laser.getState());

        telemetry.addData("BlockSensor", laser.getState());
        telemetry.addData("PositionHand", hand.getPosition());

        double extenderSpeed = 0.5 + (-gamepad2.right_stick_y * 0.5);
        if (gamepad2.right_stick_y > 0 && limitFront.getState()){ //moving forward
            extender.setPosition(extenderSpeed);
            telemetry.addData("extenderDir", 0);

        }
        else if (gamepad2.right_stick_y < 0 && limitBack.getState()){ //moving back
            extender.setPosition(extenderSpeed);
            telemetry.addData("extenderDir", 1);
        }
        else { //not moving
            extender.setPosition(0.5);
            telemetry.addData("extenderDir", 2);
            telemetry.addData("frontExtender", limitFront.getState());
            telemetry.addData("backExtender", limitBack.getState());

        }
        //     extender.setPosition(extender.getPosition() + (gamepad2.right_stick_y * 0.01));
        telemetry.addData("PositionExtender",extender.getPosition());

        telemetry.addData("red", colorSensor.red());
            telemetry.addData("blue", colorSensor.blue());
            telemetry.addData("green", colorSensor.green());
            telemetry.addData("alpha", colorSensor.alpha());


        checkLimitSwitch();
        checkColorSensor();
    }


//0.3689
//    .625
    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    public void checkLimitSwitch() {
        // double speed = 0.01; // Increase speed to 0.01 for faster movement
        if (arm.getPower() > 0) {
            if (limit1.getState()) {//checks if the limit switch has been triggered

                arm.setPower(0);
            }
        }
    }

    public void checkColorSensor() {
        int redBlockDifference = 0;
        int blueBlockDifference = 0;
        int yellowBlockDifference = 0;
        int blackDifference = 0;

        int[] currentColor = new int[4];
        currentColor[0] = colorSensor.red();
        currentColor[1] = colorSensor.green();
        currentColor[2] = colorSensor.blue();
        currentColor[3] = colorSensor.alpha();

        for (int i = 0; i < 4; i++) {//calculates the difference in current color sensed & block color
            redBlockDifference += Math.abs(redConst[i] - currentColor[i]);
            blueBlockDifference += Math.abs(blueConst[i] - currentColor[i]);
            yellowBlockDifference += Math.abs(yellowConst[i] - currentColor[i]);
            blackDifference += Math.abs(blackConst[i] - currentColor[i]);

        }

        int min = Math.min(Math.min(redBlockDifference, blueBlockDifference), Math.min(yellowBlockDifference, blackDifference));

        if (min == yellowBlockDifference) {
            telemetry.addData("Color", "Yellow");
            telemetry.addData("Color Difference", yellowBlockDifference);
        }
        else if (min == redBlockDifference) {
            telemetry.addData("Color", "Red");
            telemetry.addData("Color Difference", redBlockDifference);
        }
        else if (min == blueBlockDifference) {
            telemetry.addData("Color", "Blue");
            telemetry.addData("Color Difference", blueBlockDifference);
        }
        else if (min == blackDifference) {
            telemetry.addData("Color", "Black");
            telemetry.addData("Color Difference", blackDifference);
        }

        if (min != blackDifference) {
            if (teamColor.getState()) {//checks for current color, activates intake motor accordingly
                if (min == yellowBlockDifference || min == redBlockDifference) {
                    intake.setPower(-1);
                    telemetry.addData("colorSensorTest", 1);
                } else if (min == blueBlockDifference) {
                    intake.setPower(1);
                    telemetry.addData("colorSensorTest", 2);
                }
            } else {
                if (min == yellowBlockDifference || min == blueBlockDifference) {
                    intake.setPower(-1);
                    telemetry.addData("colorSensorTest", 3);
                } else if (min == redBlockDifference) {
                    intake.setPower(1);
                    telemetry.addData("colorSensorTest", 4);
                }
            }
        } else {
            intake.setPower(0);
            telemetry.addData("colorSensorTest", 5);
        }
    }

    //takes a value and multiplies it by its absolute value, then returns that (square function but keeps the sign)
    private double signedSquare(double x) {
        return x * Math.abs(x);
    }

}