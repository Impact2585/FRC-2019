/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.systems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Timer;
import frc.input.InputMethod;
import frc.robot.RobotMap;

import java.sql.Time;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;


/**
 * Controls the drivetrain of the robot
 */
public class WheelSystem extends RobotSystem {
  private final double DRIVE_AMT = 1;
  private final double RAMP_AMT = 0.25;
  private final double ROBOT_SPEED = 1; // robot speed at half power, ft/s
  private final double ANGLE_TOLERANCE = 5;

  private DifferentialDrive wheels;
  private double[] pastInputs;
  public ADXRS450_Gyro gyro;
  

  private NetworkTableEntry tapeDetected, tapeYaw;
    private double targetAngle;
    NetworkTableInstance instance;
    NetworkTable visionCode;
  /**
   * Creates a new wheelSystem
   * 
   * @param input the controller input object
   */
  public WheelSystem(InputMethod input) {
    super(input);
  }

  @Override
  public void init() {
    instance = NetworkTableInstance.getDefault();
    visionCode = instance.getTable("visionCode");

    tapeDetected = visionCode.getEntry("tapeDetected");
    tapeYaw = visionCode.getEntry("tapeYaw");

    Spark leftMotor = new Spark(RobotMap.LEFT_DRIVE_MOTOR);
    Spark rightMotor = new Spark(RobotMap.RIGHT_DRIVE_MOTOR);
    wheels = new DifferentialDrive(leftMotor, rightMotor);
    pastInputs = new double[2];

    gyro = new ADXRS450_Gyro();
    gyro.calibrate();
  }

  @Override
  public void run() {
    double[] power;
    if(input.targetTape()){
      power = pointToTape();
    } else {
      power = new double[]{input.leftSidePower() * DRIVE_AMT, input.rightSidePower() * DRIVE_AMT};
      power = applyRamp(power);
      wheels.tankDrive(power[0], power[1], false);
    }
    if(input.arcadeDrive() != 0){
      power[0] += input.arcadeDrive() * 0.3;
      power[1] += input.arcadeDrive() * 0.3;
    }
    if(power[0] > 100) // try changing to 1
      power[0] = 100;
    if(power[1] > 100)
      power[1] = 100;
    wheels.tankDrive(power[0], power[1]);
  }

  public double[] applyRamp (double[] inputs){
    /* Adjust Constants.kDriveLowPassFilter to make drive accel/decel at desired rate */
    double[] adjustedInputs = new double[inputs.length];

    for (int i = 0; i < inputs.length; i++){
      adjustedInputs[i] = (inputs[i] * (1-RAMP_AMT)) + (RAMP_AMT * pastInputs[i]);
      pastInputs[i] = adjustedInputs[i];
    }
    
    return adjustedInputs;
  }

  public double[] pointToTape(){
    double[] power = new double[2];
    if(tapeYaw.getBoolean(false)){
      if(tapeYaw.getDouble(0) > 5){
        power[0] = 30;
        power[1] = -30;
      }
      if(tapeYaw.getDouble(0) < -5){
        power[0] = -30;
        power[1] = 30;
      }
      else {
        power[0] = 0;
        power[1] = 0;
      }
    } else {
      power[0] = 0;
      power[1] = 0;
    }
    return power;
  }

  public void driveStraight(int distance){ // distance in feet
    double time = distance / ROBOT_SPEED;
    Timer t = new Timer();
    t.start();
    while(t.get() < time){
      wheels.tankDrive(.5, .5);
    }
    t.stop();
    wheels.tankDrive(0,0);
  }

  public void turn(int angle){ // default is (counter)clockwise?
    gyro.reset();
    double diff = angle - gyro.getAngle();
    while(Math.abs(diff) > ANGLE_TOLERANCE){
      if(diff > 0){
        wheels.tankDrive(.5, -.5);
      } else if(diff < 0){
        wheels.tankDrive(-.5, .5);
      }
      diff = angle - gyro.getAngle();
    }
    gyro.reset();

  }
}
