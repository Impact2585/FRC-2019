/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The map of input/output pins on the RoboRIO
 */
public class RobotMap {
  public static final int LEFT_DRIVE_MOTOR = 0;
  public static final int RIGHT_DRIVE_MOTOR = 1;

  public static final int INTAKE_PIVOT_MOTOR = 2;

  public static final int INTAKE_FRONT_MOTOR = 3; // wheels in the front of the intake
  public static final int INTAKE_BACK_MOTOR = 4; // wheels in the back of the intake(nearer the elevator)

  public static final int ELEVATOR_MOTOR = 5;

  public static final int ELEVATOR_LIMIT_UPPER = 0;
  public static final int ELEVATOR_LIMIT_LOWER = 1;
  public static final int PIVOT_LIMIT_UPPER = 3;
  public static final int PIVOT_LIMIT_LOWER = 2;
}
