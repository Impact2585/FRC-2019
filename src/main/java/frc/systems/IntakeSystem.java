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

/**
 * Controls the intake system of the robot
 */
public class IntakeSystem extends RobotSystem {
  private final double PIVOT_SPEED = 0.3;
  private final double WHEEL_SPEED = 0.8;
  
  private Spark pivotMotor;
  private Spark frontMotor;
  private Spark backMotor;

  /**
   * Creates a new intakeSystem
   * 
   * @param input the controller input object
   */
  public IntakeSystem(InputMethod input) {
    super(input);
  }

  @Override
  public void init() {
    pivotMotor = new Spark(RobotMap.INTAKE_PIVOT_MOTOR);
    frontMotor = new Spark(RobotMap.INTAKE_FRONT_MOTOR);
    backMotor = new Spark(RobotMap.INTAKE_BACK_MOTOR);
    backMotor.setInverted(true); // set back motor to opposite direction
  }

  @Override
  public void run() {
    setPivotSpeed(getDesiredPivotSpeed());
    setIntakeSpeed(getDesiredWheelSpeed());
  }

  /**
   * Returns the desired pivot motor speed based on the input
   */
  private double getDesiredPivotSpeed() {
    if (input.shouldPivotUp() && input.shouldPivotDown())
      return 0;

    if (input.shouldPivotUp()) return PIVOT_SPEED;
    if (input.shouldPivotDown()) return -PIVOT_SPEED;

    return 0;
  }

  /**
   * Returns the desired wheel motor speed based on the input
   */
  private double getDesiredWheelSpeed() {
    if (input.shouldIntake() && input.shouldOuttake())
      return 0;

    if (input.shouldIntake()) return WHEEL_SPEED;
    if (input.shouldOuttake()) return -WHEEL_SPEED;

    return 0;
  }

  /**
   * Sets the speed of the pivot motor
   */
  protected void setPivotSpeed(double speed) {
    pivotMotor.setSpeed(speed);
  }

  /**
   * Sets the speed of the intake motor
   */
  protected void setIntakeSpeed(double speed) {
    frontMotor.setSpeed(speed);
    backMotor.setSpeed(speed);
  }
}
