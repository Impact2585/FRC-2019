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
  private final double LIFT_SPEED = 0.5;
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
    upperLimit = new DigitalInput(RobotMap.UPPER_LIMIT_DIGITAL_INPUT);
    lowerLimit = new DigitalInput(RobotMap.LOWER_LIMIT_DIGITAL_INPUT);
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

    if (input.shouldLiftElevator()){
      return (!upperLimit.get()) ? 0 : LIFT_SPEED;
    }
    if (input.shouldLowerElevator()){ 
      return (!lowerLimit.get()) ? 0 : -LIFT_SPEED;
    }
    return 0;
  }

  /**
   * Sets the speed of the lift motor
   */
  protected void setLiftSpeed(double speed) {
    if(speed == 0)
      elevatorMotor.stopMotor();
    else
      elevatorMotor.setSpeed(speed);
  }

}
