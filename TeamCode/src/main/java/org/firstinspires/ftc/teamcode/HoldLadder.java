package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Hold the Ladder", group = "Linear Opmode")
public class HoldLadder extends LinearOpMode {
  @Override
  public void runOpMode() {
    NinjaBot robot = new NinjaBot(hardwareMap, this);

    waitForStart();
    while (opModeIsActive()) {

      robot.ladder.setPower(-0.15);

      // optional (waits before continuing loop)
      sleep(10);
    }
  }
}
