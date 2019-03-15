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
  private XboxController controller2;
  private final double JOYSTICK_DEAD_ZONE = 0.05;
  private final double TRIGGER_DEAD_ZONE = 0.1;

  public XBoxInput() {
    // 0 is the port # of the driver station the joystick is plugged into
    controller = new XboxController(0);
    controller2 = new XboxController(1);
  }

  @Override
  public double forwardAmount() {
    double forward = (Math.abs(controller.getY(Hand.kLeft)) > Math.abs(controller2.getY(Hand.kLeft))) ? controller.getY(Hand.kLeft) : controller2.getY(Hand.kLeft);
    return (Math.abs(forward) < JOYSTICK_DEAD_ZONE) ? 0 : forward;
  }

  @Override
  public double turnAmount() {
    double turn = (Math.abs(controller.getX(Hand.kRight)) > Math.abs(controller2.getX(Hand.kRight))) ? controller.getX(Hand.kRight) : controller2.getX(Hand.kRight);
    return (Math.abs(turn) < JOYSTICK_DEAD_ZONE) ? 0 : turn;
  }

  @Override
  public boolean shouldPivotUp() {
    return controller.getYButton() || controller2.getYButton();
  }
  
  @Override
  public boolean shouldPivotDown() {
    return controller.getXButton() || controller2.getXButton();
  }
  
  @Override
  public boolean shouldIntake() {
    return Math.max(controller.getTriggerAxis(Hand.kLeft), controller2.getTriggerAxis(Hand.kLeft)) > TRIGGER_DEAD_ZONE;
  }
  
  @Override
  public boolean shouldOuttake() {
    return Math.max(controller2.getTriggerAxis(Hand.kLeft), controller.getTriggerAxis(Hand.kRight)) > TRIGGER_DEAD_ZONE;
  }

  @Override
  public boolean shouldLiftElevator(){
    return controller.getAButton() || controller2.getAButton();
  }

  @Override
  public boolean shouldLowerElevator(){
    return controller.getBButton() || controller2.getBButton();
  }
}
