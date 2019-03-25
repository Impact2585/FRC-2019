/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.systems;

import edu.wpi.first.wpilibj.Spark;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.input.InputMethod;
import frc.robot.RobotMap;

/**
 * Controls the elevator system of the robot
 */
public class ElevatorSystem extends RobotSystem {
  private final double LIFT_SPEED = 0.8;
  private final double TARGET_POSITION_CALIBRATOR = 0.5;
  //private Spark elevatorMotor;
  private CANSparkMax elevatorMotor;
  private CANEncoder elevatorEncoder;
  private DigitalInput upperLimit;
  private DigitalInput lowerLimit;

  private double targetPosition; //in revolutions

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
    elevatorMotor = new CANSparkMax(RobotMap.ELEVATOR_MOTOR_CAN_ID, MotorType.kBrushless);
    elevatorEncoder = new CANEncoder(elevatorMotor);
    //elevatorMotor = new Spark(RobotMap.ELEVATOR_MOTOR);
    upperLimit = new DigitalInput(RobotMap.ELEVATOR_LIMIT_UPPER);
    lowerLimit = new DigitalInput(RobotMap.ELEVATOR_LIMIT_LOWER);
    targetPosition = 0;
  }

  @Override
  public void run() {
    //setLiftSpeed(getDesiredLiftSpeed());
    getDesiredLiftSpeed();
    setEncoderPosition(targetPosition);
  }

  /**
   * Returns the desired elevator motor speed based on the input
   */
  private void getDesiredLiftSpeed() {
    if(input.ignoreLimitSwitches())
      targetPosition += input.liftElevator() * TARGET_POSITION_CALIBRATOR;
    else if(upperLimit.get())
      targetPosition += (input.liftElevator() > 0) ? 0 : input.liftElevator() * TARGET_POSITION_CALIBRATOR;
    else if(lowerLimit.get())
      targetPosition += (input.liftElevator() < 0) ? 0 : input.liftElevator() * TARGET_POSITION_CALIBRATOR;
    else
      targetPosition += input.liftElevator() * TARGET_POSITION_CALIBRATOR;
  }

  /**
   * Sets the speed of the lift motor
   */
  protected void setLiftSpeed(double speed) {
    elevatorMotor.set(speed);
  }

  private boolean setEncoderPosition(double position){ //can be used if elevatorEncoder.setPosition(double position) doesn't work
    if(position != elevatorEncoder.getPosition()){
      double diff = position - elevatorEncoder.getPosition();
      setLiftSpeed((diff > 0) ? LIFT_SPEED : -LIFT_SPEED);
      return false;
    }
    setLiftSpeed(0);
    return true;
  }
}
