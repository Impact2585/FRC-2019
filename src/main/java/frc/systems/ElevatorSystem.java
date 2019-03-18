/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.systems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.input.InputMethod;
import frc.robot.RobotMap;

/**
 * Controls the elevator system of the robot
 */
public class ElevatorSystem extends RobotSystem {
  private final double LIFT_SPEED = 0.7;
  private Spark elevatorMotor;
  private DigitalInput upperLimit;
  private DigitalInput lowerLimit;

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
    elevatorMotor = new Spark(RobotMap.ELEVATOR_MOTOR);
    upperLimit = new DigitalInput(RobotMap.ELEVATOR_LIMIT_UPPER);
    lowerLimit = new DigitalInput(RobotMap.ELEVATOR_LIMIT_LOWER);
  }

  @Override
  public void run() {
    setLiftSpeed(getDesiredLiftSpeed());
  }

  /**
   * Returns the desired elevator motor speed based on the input
   */
  private double getDesiredLiftSpeed() {
    if(input.ignoreLimitSwitches())
      return input.liftElevator() * LIFT_SPEED;
    if(upperLimit.get())
      return (input.liftElevator() > 0) ? 0 : input.liftElevator() * LIFT_SPEED;
    if(lowerLimit.get())
      return (input.liftElevator() < 0) ? 0 : input.liftElevator() * LIFT_SPEED;
    return input.liftElevator() * LIFT_SPEED;
  }

  /**
   * Sets the speed of the lift motor
   */
  protected void setLiftSpeed(double speed) {
    elevatorMotor.setSpeed(speed);
  }

}
