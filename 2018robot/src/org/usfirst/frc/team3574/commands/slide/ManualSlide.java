package org.usfirst.frc.team3574.commands.slide;

import org.usfirst.frc.team3574.commands.util.L;
import org.usfirst.frc.team3574.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ManualSlide extends Command {
	
	private double deadzone = 0.05;
	
    public ManualSlide() {
        requires(Robot.slide);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
		L.ogInit(this);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(Robot.slide.getBottomStopSwitchIsPressed() && Math.abs(Robot.OperatorInput.CoPilotLeftStickY()) < deadzone ) {
//    		L.og("should be doing more");
    		if(Math.abs(Robot.OperatorInput.CoPilotLeftStickY()) < deadzone) {
//    			L.og("Stop the motor");
    			Robot.slide.setSlideSpeedPercent(0.0);
    			
    		}
    		
    	} else {

    		if (Math.abs(Robot.OperatorInput.CoPilotLeftStickY()) <= deadzone) {
    			Robot.slide.setSlideSpeedPercent(Robot.slide.brakeSpeed);
//    			L.og("brake speed");
    		} else {

    			Robot.slide.setSlideSpeedPercent(Robot.driveTrain.scalingSpeed(Robot.OperatorInput.CoPilotLeftStickY(), 0.25) + Robot.slide.brakeSpeed);	
    		
//    			L.og("normal drive");
    		}

    	}
    
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {    	
		L.ogEnd(this);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	L.ogInterrupt(this);  	
    }
}