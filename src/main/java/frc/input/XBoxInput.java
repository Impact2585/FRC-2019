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

  public XBoxInput() {
    // 0 is the port # of the driver station the joystick is plugged into
    controller = new XboxController(0);
  }

  public double forwardAmount() {
    return controller.getY(Hand.kLeft);
  }

  public double turnAmount() {
    return controller.getX(Hand.kRight);
  }
}
