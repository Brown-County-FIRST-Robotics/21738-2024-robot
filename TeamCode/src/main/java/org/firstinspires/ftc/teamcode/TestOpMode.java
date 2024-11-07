package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Test", group="Iterative OpMode")
public class TestOpMode extends OpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;

    private DcMotor arm = null;

    private Servo shoulder = null;
    private Servo elbow = null;
    private Servo hook = null;
    private Servo wrist = null;
    private Servo hand = null;
    private DcMotor intake = null;
    private Servo extender = null;
    private Servo twist = null;
    private DcMotor slide2 = null;

    private DigitalChannel limit1 = null;
    private DigitalChannel teamColor = null;
    private RevColorSensorV3 colorSensor = null;

    int[] redConst = {70, 100, 58, 75};
    int[] blueConst = {64, 95, 53, 70};
    int[] yellowConst = {70, 51, 100, 74};

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
        shoulder = hardwareMap.get(Servo.class, "shoulder");
        elbow = hardwareMap.get(Servo.class, "elbow");
        wrist = hardwareMap.get(Servo.class, "wrist");
        hook = hardwareMap.get(Servo.class, "hook");
        hand = hardwareMap.get(Servo.class, "hand");
        intake = hardwareMap.get(DcMotor.class, "intake");
        extender = hardwareMap.get(Servo.class, "extender");
        twist = hardwareMap.get(Servo.class, "twist");
        slide2 = hardwareMap.get(DcMotor.class, "slide2");
//        hook.setDirection(Servo.Direction.REVERSE);
        limit1 = hardwareMap.get(DigitalChannel.class, "limit1");
        teamColor = hardwareMap.get(DigitalChannel.class, "teamColor");
        colorSensor = hardwareMap.get(RevColorSensorV3.class, "color");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
        arm.setDirection(DcMotorSimple.Direction.FORWARD);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

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
        double slide3 = -gamepad2.right_stick_y;

        frontLeftDrive.setPower(0.75 * signedSquare(rotation + strafing + movement));
        frontRightDrive.setPower(0.75 * signedSquare(rotation - strafing - movement));
        backLeftDrive.setPower(0.75 * signedSquare(rotation - strafing + movement));
        backRightDrive.setPower(0.75 * signedSquare(rotation + strafing - movement));

        DigitalChannel hi;

        if (uppies > 0.05) {
            arm.setPower(.5);
        } else if (uppies < -0.05) {
            arm.setPower(-.5);
        } else {
            arm.setPower(0);
        }

        shoulder.setPosition(shoulder.getPosition() + (gamepad2.left_stick_x * 0.01));


        if (gamepad2.x) {
            elbow.setPosition(elbow.getPosition() + 0.001);
        }
        if (gamepad2.y) {
            elbow.setPosition(elbow.getPosition() - 0.001); //TODO: increase speed
        }
        telemetry.addData("Position", elbow.getPosition());

        if (gamepad2.right_bumper) {
            hook.setPosition(.38);
        }
        if (gamepad2.left_bumper) {
            hook.setPosition(.625);
        }

        if (gamepad2.a) {
            wrist.setPosition(wrist.getPosition() + 0.001);
        }
        if (gamepad2.b) {
            wrist.setPosition(wrist.getPosition() - 0.001);
            telemetry.addData("Position2", wrist.getPosition());
        } //increases speed


        if (gamepad1.right_trigger > 0.5) {
            hand.setPosition(0);
        } else if (gamepad1.left_trigger > .5) {
            hand.setPosition(0.2094);
            //hand.setPosition(.25);
            //hand.setPosition(0.525);
        }
        telemetry.addData("Position3", hand.getPosition());
        if (Math.abs(gamepad2.left_stick_x) > .05) {

            //    intake.setPower(gamepad2.right_stick_x);

        }
        extender.setPosition(extender.getPosition() + (gamepad2.right_stick_y * 0.001));

        if (-gamepad2.right_stick_y > 0.05) {
            slide2.setPower(.5);
        } else if (-gamepad2.right_stick_y < -0.05) {
            slide2.setPower(-.5);
        } else {
            slide2.setPower(0);

            telemetry.addData("red", colorSensor.red());
            telemetry.addData("blue", colorSensor.blue());
            telemetry.addData("green", colorSensor.green());
            telemetry.addData("alpha", colorSensor.alpha());

        }

        checkLimitSwitch();
        checkColorSensor();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    public void checkLimitSwitch() {
        // double speed = 0.01; // Increase speed to 0.01 for faster movement
        if (arm.getPower() > 0) {
            if (limit1.getState()) {

                arm.setPower(0);
            }
        }

        if (gamepad1.dpad_left) {
            twist.setPosition(0.6);
        } else if (gamepad1.dpad_right) {
            twist.setPosition(0.2);

            //        if (colorSensor
        }

    }

    public void checkColorSensor() {
        int redBlockDifference = 0;
        int blueBlockDifference = 0;
        int yellowBlockDifference = 0;

        int[] currentColor = new int[4];
        currentColor[0] = colorSensor.red();
        currentColor[1] = colorSensor.green();
        currentColor[2] = colorSensor.blue();
        currentColor[3] = colorSensor.alpha();

        for (int i = 0; i < 4; i++) {
            redBlockDifference += Math.abs(redConst[i] - currentColor[i]);
            blueBlockDifference += Math.abs(blueConst[i] - currentColor[i]);
            yellowBlockDifference += Math.abs(yellowConst[i] - currentColor[i]);

        }

        int min = Math.min(Math.min(redBlockDifference, blueBlockDifference), yellowBlockDifference);
        if (teamColor.getState()) {
            if (min == yellowBlockDifference || min == redBlockDifference) {
                intake.setPower(1);
            } else if (min == blueBlockDifference) {
                intake.setPower(-1);
            }
        } else {
            if (min == yellowBlockDifference || min == blueBlockDifference) {
                intake.setPower(1);
            } else if (min == redBlockDifference) {
                intake.setPower(-1);
            }
        }
    }



    //takes a value and multiplies it by its absolute value, then returns that (square function but keeps the sign)
    private double signedSquare(double x) {
        return x * Math.abs(x);
    }

}
