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
 * Add your docs here.
 */
public class XBoxInput {
  private XboxController controller;
  private final double AXIS_DEAD_ZONE = 0.05;


  public XBoxInput() {
    // 0 is the port # of the driver station the joystick is plugged into
    controller = new XboxController(0);
  }

  public double forwardAmount() {
    double fwd = controller.getY(Hand.kLeft);
    return (Math.abs(fwd) < AXIS_DEAD_ZONE) ? 0 : fwd;
  }

  public double turnAmount() {
    double turn = controller.getX(Hand.kRight);
    return (Math.abs(turn) < AXIS_DEAD_ZONE) ? 0 : turn;
  }

  public double intakePivot(){
    if(controller.getXButton() && !controller.getYButton())
      return 1;
    else if(controller.getYButton() && !controller.getXButton()) 
      return -1;
    return 0;
  }

  public double runIntakeWheel(){
    if(controller.getAButton() && !controller.getBButton())
      return 1;
    else if(controller.getBButton() && !controller.getAButton()) 
      return -1;
    return 0;
  }
}
