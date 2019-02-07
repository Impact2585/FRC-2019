/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.systems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.input.XBoxInput;
import frc.robot.RobotMap;

/**
 * Controls the drivetrain of the robot
 */
public class WheelSystem extends RobotSystem {
  private DifferentialDrive wheels;

  /**
   * Creates a new wheelSystem
   * 
   * @param input the controller input object
   */
  public WheelSystem(XBoxInput input) {
    super(input);
  }

  @Override
  public void init() {
    Spark leftMotor = new Spark(RobotMap.LEFT_DRIVE_MOTOR);
    Spark rightMotor = new Spark(RobotMap.RIGHT_DRIVE_MOTOR);
    wheels = new DifferentialDrive(leftMotor, rightMotor);
  }

  @Override
  public void run() {
    wheels.arcadeDrive(input.forwardAmount(), input.turnAmount());
  }
}
