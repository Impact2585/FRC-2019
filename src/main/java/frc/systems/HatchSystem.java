/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.systems;

import edu.wpi.first.wpilibj.Spark;
import frc.input.InputMethod;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.*;


/**
 * Controls the intake system of the robot
 */
public class HatchSystem extends RobotSystem {
  private final double HATCH_MOTOR_SPEED = 0.4;

  private Spark hatchMotor;

  /**
   * Creates a new intakeSystem
   * 
   * @param input the controller input object
   */
  public HatchSystem(InputMethod input) {
    super(input);
  }

  @Override
  public void init() {
    hatchMotor = new Spark(RobotMap.HATCH_INTAKE);
  }

  @Override
  public void run() {
    SmartDashboard.putNumber("Target HatchMotor Speed", getDesiredMotorSpeed());
    setHatchSpeed(getDesiredMotorSpeed());
  }

  /**
   * Returns the desired pivot motor speed based on the input
   */
  private double getDesiredMotorSpeed() {
    if(input.shouldIntakeHatch())
      return HATCH_MOTOR_SPEED;
    return (input.shouldOuttakeHatch()) ? -HATCH_MOTOR_SPEED : 0;
  }

  protected void setHatchSpeed(double speed) {
    hatchMotor.setSpeed(speed);
  }
}
