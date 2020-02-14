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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import java.sql.Time;
import java.util.Arrays;

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
  private ADXRS450_Gyro gyro;
  

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
      SmartDashboard.putString("LIMELIGHT DEBUG",Arrays.toString(power));
      SmartDashboard.putNumber("Gyro value:", gyro.getAngle());
    } else {
      power = new double[]{input.leftSidePower() * DRIVE_AMT, input.rightSidePower() * DRIVE_AMT};
      //power = applyRamp(power);
      wheels.tankDrive(power[0], power[1], false);
    }
    /*if(input.arcadeDrive() != 0){
      power[0] += input.arcadeDrive() * 0.3;
      power[1] += input.arcadeDrive() * 0.3;
    }*/
    if(power[0] > 1) // try changing to 1
      power[0] = 1;
    if(power[1] > 1)
      power[1] = 1;
    
    System.out.println(power[0]+" "+power[1]);
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
    Target t = getLoc();
    double Kp = -0.03; //TUNE
    double min_command = 0.05; //TUNE
    double error = -t.x;
    double adjust = 0.0;
    if(t.x > 0.0){
      adjust = Kp*error - min_command;
    }
    else if(t.x < 0.0){
      adjust = Kp*error + min_command;
    }
    SmartDashboard.putString("TARGET DEBUG",t.toString());
    return new double[]{adjust,-adjust};
  }
  public double getTargetDistance(){
    Target t = getLoc();
    double mountingAngle = 25.0; // TUNE
    double targetAngle = t.y;
    double mountHeight = 15;
    double targetHeight = 60;
    double d = (targetHeight-mountHeight)/Math.tan(mountingAngle+targetAngle);
    return d;
  }
  public void driveStraight(int distance){ // distance in feet // set distance to speed for option 2
    //OPTION 1
    wheels.setSafetyEnabled(false);
    double time = distance / ROBOT_SPEED;
    Timer t = new Timer();
    t.start();
    while(t.get() < time){
      wheels.tankDrive(.5, .5);
    }
    t.stop();
    wheels.tankDrive(0,0);
    wheels.setSafetyEnabled(true);

    //OPTION 2
    //wheels.tankDrive(speed, speed);
  }

  public void turn(int angle){ // default is (counter)clockwise?
    //OPTION 1
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

    //OPTION 2
    /*double diff = angle - gyro.getAngle();
    if(Math.abs(diff) > ANGLE_TOLERANCE){
      if(diff > 0){
        wheels.tankDrive(.5, -.5);
      } else if(diff < 0){
        wheels.tankDrive(-.5, .5);
      }
      diff = angle - gyro.getAngle();
    }*/
  }
  
  public double getGyroAngle(){
    return gyro.getAngle();
  }

  public void resetGyro(){
    gyro.reset();
  }

  public Target getLoc(){
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-impact");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double a = ta.getDouble(0.0);
    Target t = new Target(x,y,a);
    SmartDashboard.putNumber("target", tx.getDouble(0.0));
    return t;
  }
  static class Target {
    double x, y;
    double area;
    public Target(double x, double y, double area){
      this.x=x;
      this.y=y;
      this.area=area;
    }
    public String toString(){
      return "x: "+x+" y: "+y+"area: "+area;
    }
  }
}
