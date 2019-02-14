/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.input;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * The input from the XBox remote
 */
public class XBoxInput extends InputMethod {
  private XboxController controller;
  private final double JOYSTICK_DEAD_ZONE = 0.05;
  private final double TRIGGER_DEAD_ZONE = 0.1;

  public XBoxInput() {
    // 0 is the port # of the driver station the joystick is plugged into
    controller = new XboxController(0);
  }

  @Override
  public double forwardAmount() {
    double forward = controller.getY(Hand.kLeft);
    return (Math.abs(forward) < JOYSTICK_DEAD_ZONE) ? 0 : forward;
  }

  @Override
  public double turnAmount() {
    double turn = controller.getX(Hand.kRight);

    return (Math.abs(turn) < JOYSTICK_DEAD_ZONE) ? 0 : turn;
  }

  @Override
  public boolean shouldPivotUp() {
    return controller.getYButton();
  }
  
  @Override
  public boolean shouldPivotDown() {
    return controller.getXButton();
  }
  
  @Override
  public boolean shouldIntake() {
    return controller.getTriggerAxis(Hand.kLeft) > TRIGGER_DEAD_ZONE;
  }
  
  @Override
  public boolean shouldOuttake() {
    return controller.getTriggerAxis(Hand.kRight) > TRIGGER_DEAD_ZONE;
  }

  @Override
  public boolean shouldLiftElevator(){
    return controller.getAButton();
  }

  @Override
  public boolean shouldLowerElevator(){
    return controller.getBButton();
  }
}
