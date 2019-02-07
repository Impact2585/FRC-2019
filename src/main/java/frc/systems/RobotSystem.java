/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.systems;

import frc.input.XBoxInput;

/**
 * Parent class of the systems that run on the robot
 */
public abstract class RobotSystem {
    protected XBoxInput input;

    /**
     * Initializes the system
     * @param input the object that gives the controller input from the user
     */
    public void init(XBoxInput input) {
		this.input = input;
    }
    
    /**
     * Runs the system. Intended to be called periodically and rapidly
     */
    public abstract void run();
}
