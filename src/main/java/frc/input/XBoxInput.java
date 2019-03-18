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
  private final double JOYSTICK_DEAD_ZONE = 0.02;
  private final double TRIGGER_DEAD_ZONE = 0.1;
  private final double SLOW_MOVEMENT = 0.7;

  public XBoxInput() {
    // 0 is the port # of the driver station the joystick is plugged into
    controller = new XboxController(0);
    controller2 = new XboxController(1);
  }

  @Override
  public double leftSidePower() {
    double forward = (Math.abs(controller.getY(Hand.kLeft)) > Math.abs(controller2.getY(Hand.kLeft))) ? controller.getY(Hand.kLeft) : controller2.getY(Hand.kLeft);
    if(Math.abs(forward) < JOYSTICK_DEAD_ZONE)
      return 0;
    return (controller.getBumper(Hand.kRight) || controller.getBumper(Hand.kLeft) || controller2.getBumper(Hand.kRight) || controller2.getBumper(Hand.kLeft)) ? forward * SLOW_MOVEMENT : forward;
  }

  @Override
  public double rightSidePower() {
    double turn = (Math.abs(controller.getY(Hand.kRight)) > Math.abs(controller2.getY(Hand.kRight))) ? controller.getY(Hand.kRight) : controller2.getY(Hand.kRight);
    //double turn = (Math.abs(controller.getX(Hand.kRight)) > Math.abs(controller2.getX(Hand.kRight))) ? controller.getX(Hand.kRight) : controller2.getX(Hand.kRight);
    if(Math.abs(turn) < JOYSTICK_DEAD_ZONE)
      return 0;
    return (controller.getBumper(Hand.kRight) || controller.getBumper(Hand.kLeft) || controller2.getBumper(Hand.kRight) || controller2.getBumper(Hand.kLeft)) ? turn * SLOW_MOVEMENT : turn;
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
    return Math.max(controller.getTriggerAxis(Hand.kRight), controller2.getTriggerAxis(Hand.kRight)) > TRIGGER_DEAD_ZONE;
  }
  
  @Override
  public boolean shouldOuttake() {
    return Math.max(controller.getTriggerAxis(Hand.kLeft), controller2.getTriggerAxis(Hand.kLeft)) > TRIGGER_DEAD_ZONE;
  }

  @Override
  public boolean shouldLiftElevator(){
    return controller.getAButton() || controller2.getAButton();
  }

  @Override
  public boolean shouldLowerElevator(){
    return controller.getBButton() || controller2.getBButton();
  }

  @Override
  public boolean ignoreLimitSwitches(){
    return controller.getStartButton() || controller2.getStartButton();
  }
}
