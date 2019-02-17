/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.systems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.input.InputMethod;
import frc.robot.RobotMap;

/**
 * Controls the elevator system of the robot
 */
public class ElevatorSystem extends RobotSystem {
  private final double LIFT_SPEED = 0.5;
  
  //private Spark elevatorMotor;
  private CANSparkMax elevatorMotor;

  /**
   * Creates a new elevatorSystem
   * 
   * @param input the controller input object
   */
  public ElevatorSystem(InputMethod input) {
    super(input);
  }

  @Override
  public void init() {
    elevatorMotor = new CANSparkMax(RobotMap.ELEVATOR_MOTOR, MotorType.kBrushless);
    elevatorMotor.restoreFactoryDefaults();
  }

  @Override
  public void run() {
    setLiftSpeed(getDesiredLiftSpeed());
  }

  /**
   * Returns the desired elevator motor speed based on the input
   */
  private double getDesiredLiftSpeed() {
    if (input.shouldLiftElevator() && input.shouldLowerElevator())
      return 0;

    if (input.shouldLiftElevator()) return LIFT_SPEED;
    if (input.shouldLowerElevator()) return -LIFT_SPEED;

    return 0;
  }

  /**
   * Sets the speed of the lift motor
   */
  protected void setLiftSpeed(double speed) {
    elevatorMotor.set(speed);
  }

}
