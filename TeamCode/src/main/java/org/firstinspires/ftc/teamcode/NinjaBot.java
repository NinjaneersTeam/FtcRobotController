package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


public class NinjaBot {
  // Define motors
  public DcMotor leftDrive = null;
  public DcMotor rightDrive = null;
  public DcMotor ladder = null;
  public Servo claw = null;

  // define constants and other variabes

  // local OpMode members
  HardwareMap hwMap = null;
  LinearOpMode control = null;
  private ElapsedTime period = new ElapsedTime();

  public NinjaBot(HardwareMap map, LinearOpMode ctrl) {
    init(map, ctrl);
  }

  public void init(HardwareMap map, LinearOpMode ctrl) {
    // Save reference to Hardware map
    hwMap = map;
    control = ctrl;

    // link motors to their references
    leftDrive = hwMap.get(DcMotor.class, "left");
    rightDrive = hwMap.get(DcMotor.class, "right");
    ladder = hwMap.get(DcMotor.class, "ladder");

    leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);

    claw = hwMap.get(Servo.class, "claw");
  }

  private final RemoteControl.Point origin = new RemoteControl.Point(0, 0);

  static class Point {
    public double x, y;

    Point(double x, double y) {
      this.x = x;
      this.y = y;
    }

    public double angle() {
      return Math.atan2(this.x, this.y);
    }

    public RemoteControl.Point sub(RemoteControl.Point point) {
      return new RemoteControl.Point(this.x - point.x, this.y - point.y);
    }

    public RemoteControl.Point add(RemoteControl.Point point) {
      return new RemoteControl.Point(this.x + point.x, this.y + point.y);
    }

    public RemoteControl.Point divide(double x) {
      return new RemoteControl.Point(this.x / x, this.y / x);
    }
  }

  private RemoteControl.Point circleToSquare(RemoteControl.Point onSquare) {
    double length = Math.max(distance(origin, onSquare), 1.0);

    double u = onSquare.x / length;
    double v = onSquare.y / length;

    double u2 = u * u;
    double v2 = v * v;
    double twoSqrt2 = 2.0 * Math.sqrt(2.0);
    double subTermX = 2.0 + u2 - v2;
    double subTermY = 2.0 - u2 + v2;
    double termX1 = subTermX + u * twoSqrt2;
    double termX2 = subTermX - u * twoSqrt2;
    double termY1 = subTermY + v * twoSqrt2;
    double termY2 = subTermY - v * twoSqrt2;
    double x = 0.5 * Math.sqrt(termX1) - 0.5 * Math.sqrt(termX2);
    double y = 0.5 * Math.sqrt(termY1) - 0.5 * Math.sqrt(termY2);

    return new RemoteControl.Point(x, y);
  }

  private double squared(double x) {
    return x * x;
  }

  private double distance(RemoteControl.Point one, RemoteControl.Point two) {
    return Math.sqrt(squared(one.x - two.x) + squared(one.y - two.y));
  }

  // Turning is clockwise from -1 to 1
  public void move(double turn, double speed) {
    RemoteControl.Point circle = new RemoteControl.Point(turn, -speed);
    RemoteControl.Point uv = circleToSquare(circle);
    RemoteControl.Point UV = uv.add(new RemoteControl.Point(1, 1)).divide(2.0);

    double left = Math.min(distance(origin, circle) * -(1.0 - UV.x - UV.y) * 2.0, 1.0);
    double right = Math.min(distance(origin, circle) * -(UV.x - UV.y) * 2.0, 1.0);

    // set motor power
    this.leftDrive.setPower(left);
    this.rightDrive.setPower(right);
  }
  // define functions relating to the control of motors of the robot in general.
  // specific control should be in the OpMode code file/s.
  // eg: public void updateWheelTelemetry() {...}
}
