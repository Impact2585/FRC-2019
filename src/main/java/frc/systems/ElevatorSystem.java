/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.systems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.input.InputMethod;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.*;

/**
 * Controls the elevator system of the robot
 */
public class ElevatorSystem extends RobotSystem {
  private final double LIFT_SPEED = 0.8;
  private final double TARGET_POSITION_CALIBRATOR = 0.5;
  private final double MAX_DIFF = 1;
  private final double LEVEL_ONE_HEIGHT = 1;
  private final double LEVEL_TWO_HEIGHT = 2;
  private final double LEVEL_THREE_HEIGHT = 3;
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
    upperLimit = new DigitalInput(RobotMap.ELEVATOR_LIMIT_UPPER);
    lowerLimit = new DigitalInput(RobotMap.ELEVATOR_LIMIT_LOWER);
    targetPosition = 0;
    elevatorMotor.setInverted(true);
  }

  @Override
  public void run() {
    SmartDashboard.putBoolean("UPPER ELEVATOR LIMIT", upperLimit.get());
    SmartDashboard.putBoolean("LOWER ELEVATOR LIMIT", lowerLimit.get());

    //getDesiredLiftPosition();
    //setEncoderPosition(targetPosition);
    setLiftSpeed(getDesiredLiftSpeed());

    SmartDashboard.putNumber("Elevator Position", elevatorEncoder.getPosition());
  }

  /**
   * Sets the speed of the lift motor
   */
  protected void setLiftSpeed(double speed) {
    elevatorMotor.set(speed);
  }

  private double getDesiredLiftSpeed(){
    if(input.ignoreLimitSwitches())
      return input.liftElevator() * LIFT_SPEED;
    if(!upperLimit.get())
      return (input.liftElevator() < 0) ? 0 : input.liftElevator() * LIFT_SPEED;
    if(!lowerLimit.get())
      return (input.liftElevator() > 0) ? 0 : input.liftElevator() * LIFT_SPEED;
    return input.liftElevator() * LIFT_SPEED;
  }

  private boolean setEncoderPosition(double position){ //can be used if elevatorEncoder.setPosition(double position) doesn't work
    double diff = position - elevatorEncoder.getPosition();

    if(Math.abs(diff) > MAX_DIFF){
      setLiftSpeed((diff > 0) ? LIFT_SPEED : -LIFT_SPEED);
      return false;
    }
    setLiftSpeed(0);
    return true;
  }

  /**
   * Returns the desired elevator motor speed based on the input
   */
  private void getDesiredLiftPosition() {
    double liftPosition = getDesiredLiftPositionRaw();
    if(input.ignoreLimitSwitches()){
      targetPosition = liftPosition;
      return;
    }
    if(!upperLimit.get() && liftPosition > targetPosition){
      return;
    }
    if(!lowerLimit.get() && liftPosition < targetPosition){
      return;
    }
    targetPosition = liftPosition;
  }

  private double getDesiredLiftPositionRaw(){
    if(Math.abs(input.liftElevator()) > 0){
      return elevatorEncoder.getPosition() + input.liftElevator() * TARGET_POSITION_CALIBRATOR;
    }
    if(input.levelOne()){
      return LEVEL_ONE_HEIGHT; 
    }
    if(input.levelTwo()){
      return LEVEL_TWO_HEIGHT;
    }
    if(input.levelThree()){
      return LEVEL_THREE_HEIGHT;
    }
    return 0;
  }
}
