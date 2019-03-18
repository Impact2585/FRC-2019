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


/**
 * Controls the intake system of the robot
 */
public class IntakeSystem extends RobotSystem {
  private final double PIVOT_SPEED = 0.6;
  private final double WHEEL_SPEED = 0.8;
  
  private Spark pivotMotor;
  private Spark frontMotor;
  private Spark backMotor;

  private DigitalInput pivotLimitUpper;
  private DigitalInput pivotLimitLower;

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
    pivotLimitUpper = new DigitalInput(RobotMap.PIVOT_LIMIT_UPPER);
    pivotLimitLower = new DigitalInput(RobotMap.PIVOT_LIMIT_LOWER);
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
    if(input.ignoreLimitSwitches())
      return input.pivotIntake() * PIVOT_SPEED;
    if(pivotLimitUpper.get())
      return (input.pivotIntake() > 0) ? 0 : input.pivotIntake() * PIVOT_SPEED;
    if(pivotLimitLower.get())
      return (input.pivotIntake() < 0) ? 0 : input.pivotIntake() * PIVOT_SPEED;
    return input.pivotIntake() * PIVOT_SPEED;
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
    backMotor.setSpeed(-speed);
  }
}
