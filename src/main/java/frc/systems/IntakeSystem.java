/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.systems;

import edu.wpi.first.wpilibj.Spark;
import frc.input.XBoxInput;
import frc.robot.RobotMap;

/**
 * Controls the drivetrain of the robot
 */
public class IntakeSystem extends RobotSystem {
  private final double pivotSpeedConst = 0.3;
  private final double intakeWheelConst = 0.8;
  private Spark pivotMotor = new Spark(RobotMap.INTAKE_PIVOT_MOTOR);
  private Spark frontMotor = new Spark(RobotMap.INTAKE_FRONT_MOTOR);
  private Spark backMotor = new Spark(RobotMap.INTAKE_BACK_MOTOR);
  /**
   * Creates a new wheelSystem
   * 
   * @param input the controller input object
   */
  public IntakeSystem(XBoxInput input) {
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
    pivotMotor.setSpeed(input.intakePivot() * pivotSpeedConst);
    
    frontMotor.setSpeed(input.runIntakeWheel() * intakeWheelConst);
    backMotor.setSpeed(input.runIntakeWheel() * intakeWheelConst);

  }
}
