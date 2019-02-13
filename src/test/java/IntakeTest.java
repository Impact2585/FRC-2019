
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

import org.junit.Test;

import frc.input.InputMethod;
import frc.systems.IntakeSystem;

import org.junit.Assert;
import org.junit.Before;

/**
 * Tests for the intake system
 */
public class IntakeTest {
  private TestInput input;
  private TestIntakeSystem intake;

  @Before
  public void setUp() {
    input = new TestInput();
    intake = new TestIntakeSystem(input);
  }

  @Test
  public void testDefaultsZero() {
    Assert.assertTrue(true);
  }

  /**
	 * Tests that the speed of the motor starts as zero when the 
	 * user gives no input
	 */
	@Test
	public void defaultsToZero() {
		intake.run();
		Assert.assertTrue(intake.intakeSpeedOut == 0);
	}
	
	/**
	 * Tests that the speed of the motor is positive when right trigger is pressed 
	 */
	@Test
	public void robotIntakes() {
		input.shouldIntake = true;
		intake.run();
		Assert.assertTrue(intake.intakeSpeedOut > 0);
	}
	
	/**
	 * Tests that the speed of the motor is negative when the left trigger is pressed
	 */
	@Test
	public void robotOuttakes() {
		input.shouldOuttake = true;
		intake.run();
		Assert.assertTrue(intake.intakeSpeedOut < 0);
	}
	
	/**
	 * Tests that the motor doesn't move when both triggers are pressed
	 */
	@Test
	public void bothPressed() {
		input.shouldIntake = true;
		input.shouldOuttake = true;
		intake.run();
		Assert.assertTrue(intake.intakeSpeedOut == 0);
	}
	
	/**
	 * Tests that the motor speed returns to zero when right trigger is released
	 */
	@Test
	public void returnToZeroAfterIntake() {
		input.shouldIntake = true;
		intake.run();
		input.shouldIntake = false;
		intake.run();
		Assert.assertTrue(intake.intakeSpeedOut == 0);
	}
	
	/**
	 * Tests that the motor speed returns to zero when left trigger is released
	 */
	@Test
	public void returnToZeroAfterOuttake() {
		input.shouldOuttake = true;
		intake.run();
		input.shouldOuttake = false;
		intake.run();
		Assert.assertTrue(intake.intakeSpeedOut == 0);
	}
	
	/**
	 * Tests that the motor speed returns to zero after both triggers are released
	 */
	@Test
	public void returnToZeroAfterBoth() {
		input.shouldIntake = true;
		input.shouldOuttake = true;
		intake.run();
		input.shouldIntake = false;
		input.shouldOuttake = false;
		intake.run();
		Assert.assertTrue(intake.intakeSpeedOut == 0);
  }

  private class TestInput extends InputMethod {
    public boolean shouldIntake;
    public boolean shouldOuttake;
    public boolean shouldPivotUp;
    public boolean shouldPivotDown;

    @Override
    public boolean shouldIntake() {
      return shouldIntake;
    }

    @Override
    public boolean shouldOuttake() {
      return shouldOuttake;
    }

    @Override
    public boolean shouldPivotUp() {
      return shouldPivotUp;
    }

    @Override
    public boolean shouldPivotDown() {
      return shouldPivotDown;
    }
  }

  private class TestIntakeSystem extends IntakeSystem {
    public double pivotSpeedOut;
    public double intakeSpeedOut;

    public TestIntakeSystem(InputMethod input) {
      super(input);
    }

    @Override
    protected void setPivotSpeed(double speed) {
      pivotSpeedOut = speed;
    }

    @Override
    protected void setIntakeSpeed(double speed) {
      intakeSpeedOut = speed;
    }
  }
}
