/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.systems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.input.InputMethod;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.networktables.*;

/**
 * Controls the drivetrain of the robot
 */
public class WheelSystem extends RobotSystem {
  private final double DRIVE_AMT = 1;
  private final double RAMP_AMT = 0.25;

  private DifferentialDrive wheels;
  private double[] pastInputs;

  /**
   * Creates a new wheelSystem
   * 
   * @param input the controller input object
   */
  public WheelSystem(InputMethod input) {
    super(input);
  }

  @Override
  public void init() {
    Spark leftMotor = new Spark(RobotMap.LEFT_DRIVE_MOTOR);
    Spark rightMotor = new Spark(RobotMap.RIGHT_DRIVE_MOTOR);
    wheels = new DifferentialDrive(leftMotor, rightMotor);
    pastInputs = new double[2];
  }

  @Override
  public void run() {
    double[] power = {input.leftSidePower() * DRIVE_AMT, input.rightSidePower() * DRIVE_AMT};
    power = applyRamp(power);
    wheels.tankDrive(power[0], power[1], false);
  }

  public double[] applyRamp (double[] inputs){
    /* Adjust Constants.kDriveLowPassFilter to make drive accel/decel at desired rate */
    double[] adjustedInputs = new double[inputs.length];

    for (int i = 0; i < inputs.length; i++){
      adjustedInputs[i] = (inputs[i] * (1-RAMP_AMT)) + (RAMP_AMT * pastInputs[i]);
      pastInputs[i] = adjustedInputs[i];
    }
    
    return adjustedInputs;
  }
  /*NetworkTable visionTable = NetworkTable.getTable("vision");
  public boolean pointTo(){
   // NetworkTableInstance.getDefault().getTable("")
    if(tapeYaw > 5)
      wheels.tankDrive(30, -30)
    else if (tapeYaw < -5)
      wheels.tankDrive(-30, 30);
    return false;
  }*/
}
