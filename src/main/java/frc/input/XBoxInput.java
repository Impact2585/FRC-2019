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
  private final double JOYSTICK_DEAD_ZONE = 0.075;
  private final double TRIGGER_DEAD_ZONE = 0.1;
  private final double SLOW_MOVEMENT = 0.7;

  public XBoxInput() {
    // 0 is the port # of the driver station the joystick is plugged into
    controller = new XboxController(0);
    controller2 = new XboxController(1);
  }

  @Override
  public double leftSidePower() {
    /*double forward = controller.getY(Hand.kLeft);
    if(Math.abs(forward) < JOYSTICK_DEAD_ZONE)
      return 0;
    return forward * SLOW_MOVEMENT;*/
    double forward = controller.getY(Hand.kLeft);
    double turn = controller.getX(Hand.kRight);
    if(Math.abs(forward + turn) < JOYSTICK_DEAD_ZONE)
      return 0;
      return (forward + turn) * SLOW_MOVEMENT;
  }

  @Override
  public double rightSidePower() {
   /* double forward = controller.getY(Hand.kRight);
    if(Math.abs(forward) < JOYSTICK_DEAD_ZONE)
      return 0;
    return forward * SLOW_MOVEMENT;*/
    double forward = controller.getY(Hand.kLeft);
    double turn = controller.getX(Hand.kRight);
    if(Math.abs(forward - turn) < JOYSTICK_DEAD_ZONE)
      return 0;
      return (forward - turn) * SLOW_MOVEMENT;
  }

  @Override
  public double pivotIntake() {
    double movement = controller2.getY(Hand.kRight);
    return (Math.abs(movement) < JOYSTICK_DEAD_ZONE) ? 0 : movement;
  }
  
  @Override
  public double liftElevator(){
    double movement = controller2.getY(Hand.kLeft);
    return (Math.abs(movement) < JOYSTICK_DEAD_ZONE) ? 0 : movement;
  }
  
  @Override
  public boolean levelOne(){
    //return controller2.getAButton();
    return false;
  }

  @Override
  public boolean levelTwo(){
    //return controller2.getXButton();
    return false;
  }

  @Override
  public boolean levelThree(){
    //return controller2.getYButton();
    return false;
  }

  @Override
  public boolean shouldIntake() {
    return controller.getTriggerAxis(Hand.kRight) > TRIGGER_DEAD_ZONE || controller2.getTriggerAxis(Hand.kRight) > TRIGGER_DEAD_ZONE;
  }
  
  @Override
  public boolean shouldOuttake() {
    return controller.getTriggerAxis(Hand.kLeft) > TRIGGER_DEAD_ZONE || controller2.getTriggerAxis(Hand.kLeft) > TRIGGER_DEAD_ZONE;
  }

  public boolean shouldIntakeHatch(){
    return false;
    //return controller2.getBumper(Hand.kRight);
  }

  public boolean shouldOuttakeHatch(){
    return false;
    //eturn controller2.getBumper(Hand.kLeft);
  }

  @Override
  public boolean ignoreLimitSwitches(){
    return controller.getStartButton() || controller2.getStartButton();
  }

  @Override
  public boolean targetTape(){
    return controller.getAButton();
    //return controller.getTriggerAxis(Hand.kRight) > TRIGGER_DEAD_ZONE || controller.getTriggerAxis(Hand.kLeft) > TRIGGER_DEAD_ZONE;
  }

  @Override
  public double arcadeDrive(){
    return 0;
    /* if(controller.getYButton())
      return 1;
    if(controller.getAButton())
      return -1;
    return 0; */
  }
}
