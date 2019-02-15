//Conner Taylor
//Gurny/Climbing System


package frc.robot;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import frc.ecommons.Constants;
import frc.ecommons.RobotMap;
import frc.robot.NavX;

public class Gurny_josh  {

  Joystick m_joy;


  WPI_TalonSRX gBack;
  
  WPI_TalonSRX gFront;

  WPI_VictorSPX gDrive;
  double dashFrontSpeed, dashBackSpeed, dashDriveSpeed;
  
  NavX m_navX;
  ShuffleboardTab gurneyTab = Shuffleboard.getTab("Gurney");
  NetworkTableEntry gurneyPitchEntry = gurneyTab.add("Gurney Pitch", 0)
                                      .withWidget(BuiltInWidgets.kTextView)
                                      .getEntry();
  NetworkTableEntry gurneyFrontSpeedEntry = gurneyTab.add("Gurney Front Motor Speed", 0)
  .withWidget(BuiltInWidgets.kTextView)
  .getEntry();
  NetworkTableEntry gurneyBackSpeedEntry = gurneyTab.add("Gurney Back Motor Speed", 0)
  .withWidget(BuiltInWidgets.kTextView)
  .getEntry();
  NetworkTableEntry gurneyDriveSpeedEntry = gurneyTab.add("Gurney Drive Motor Speed", 0)
  .withWidget(BuiltInWidgets.kTextView)
  .getEntry();
 
  SendableChooser<Boolean> manual_or_hold_chooser = new SendableChooser<Boolean>();
  int current_postion;

  // ShuffleboardTab MaxSpeedTab = Shuffleboard.getTab("Max Speed");
  // NetworkTableEntry frontGurneyEntry;
  // NetworkTableEntry backGurneyEntry;
  // NetworkTableEntry driveGurneyEntry;


  public void robotInit(Joystick j) {

    m_joy = j;

    gBack = new WPI_TalonSRX(RobotMap.gBack);

    gFront = new WPI_TalonSRX(RobotMap.gFront);

    gDrive = new WPI_VictorSPX(RobotMap.gDrive);

    gDrive.configFactoryDefault();
    gBack.configFactoryDefault();
    gFront.configFactoryDefault();

    gBack.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
    gBack.setSelectedSensorPosition(0);
    current_postion = gBack.getSelectedSensorPosition();
    gBack.configNominalOutputForward(0);
    gBack.configNominalOutputReverse(0);
    gBack.configPeakOutputForward(1);
    gBack.configPeakOutputReverse(-1);
    setUpPID();

    gFront.configNominalOutputForward(0);
    gFront.configNominalOutputReverse(0);
    gFront.configPeakOutputForward(1);
    gFront.configPeakOutputReverse(-1);
    gFront.configPeakCurrentLimit(8);
    gFront.configContinuousCurrentLimit(6);

    gFront.configAllowableClosedloopError(0, 0, 0);
    gFront.config_kF(0, 0);
    gFront.config_kP(0, 0.5);
    gFront.config_kI(0, 0);
    gFront.config_kD(0, 0);
    gFront.config_IntegralZone(0, 1);

    m_navX = new NavX();
    double filtered_error = m_navX.getPitch();
    manual_or_hold_chooser.setDefaultOption("Manual", true);
    manual_or_hold_chooser.addOption("Hold Height", false);
    // gurneyTab.add("Mode", manual_or_hold_chooser).withWidget(BuiltInWidgets.kSplitButtonChooser);
  }

  private void setUpPID() {
    gBack.config_kP(0, 1);
    gBack.config_kI(0, 0);
    gBack.config_kD(0, 0);
    gBack.config_kF(0, 0);
    gBack.config_IntegralZone(0, 500);
    gBack.configMotionAcceleration(700);
    gBack.configMotionCruiseVelocity(1000);
  }
  
  private void setDownPID() {
    gBack.config_kP(0, 0.1);
    gBack.config_kI(0, 0);
    gBack.config_kD(0, 0);
    gBack.config_IntegralZone(0, 500);
    gBack.configMotionAcceleration(700);
    gBack.configMotionCruiseVelocity(1200);
  }

  private void setHoldPID() {
    gBack.config_kP(0, 2.7);
    gBack.config_kI(0, 0);
    gBack.config_kD(0, 10);
    gBack.config_kF(0, 0);
    gBack.config_IntegralZone(0, 500);
    gBack.configMotionAcceleration(700);
    gBack.configMotionCruiseVelocity(1000);
  }

  public void autonomousInit() {
    gBack.setSelectedSensorPosition(0);
  }


  
  public void autonomousPeriodic() {
  }

  public void teleopInit() {
  }
  
  public void teleopPeriodic() {
    // reset encoder
    if (m_joy.getRawButton(Constants.gurneyEncoderReset)) {
      gBack.setSelectedSensorPosition(0);
    }

    // drive    
    dashFrontSpeed = gurneyFrontSpeedEntry.getDouble(0.5);
    dashBackSpeed = gurneyBackSpeedEntry.getDouble(0.3);
    dashDriveSpeed = gurneyDriveSpeedEntry.getDouble(0.7);
    if (m_joy.getRawAxis(Constants.gDriveBack) < 0.1) {
      gDrive.set(ControlMode.PercentOutput, m_joy.getRawAxis(Constants.gDriveForward) * dashDriveSpeed);
    } else if (m_joy.getRawAxis(Constants.gDriveForward) < 0.1) {
      gDrive.set(ControlMode.PercentOutput, m_joy.getRawAxis(Constants.gDriveBack) * -dashDriveSpeed);
    }

    /* #TODO: add low pass filtered error
    double pitch_error = m_navX.getPitch();
    filtered_error = low_pass(pitch_error, filtered_error);
    */

    // #TODO: add switch button on shuffleboard to have 'fast' / 'slow' mode
    if (m_joy.getRawButton(Constants.gurneyGoUp)) {
      /* Gurney UP on Button 4, Y
       * 
       * call motion magic with a set point of ~36000
       *
       * Robot tilts forward ==> positive pitch ==> add to front output
       * #TODO: set max height on encoder for top of gurney
       */
      setUpPID();
      gBack.set(ControlMode.MotionMagic, 36000);

      // accelerometer PID for front
      double pitch_error = m_navX.getPitch() - 2;
      double front_kF = 0.5;
      double front_kP = 0.13;
      double output = front_kF + (front_kP)*pitch_error;
      gFront.set(ControlMode.PercentOutput, output);

      /* #TODO: test adding an offset to help the hold PID
       * this would be the steady state error of the UP
       */
      int experimental_steady_state_error = 0;
      current_postion = gBack.getSelectedSensorPosition() + experimental_steady_state_error;
    }
    else if (m_joy.getRawButton(Constants.gurneyGoDown)) {
      /* Gurney DOWN on Button 3, X
       *
       * call motion magic with a target position of 0
       *
       */
      setDownPID();
      gBack.set(ControlMode.MotionMagic, 10);
      double pitch_error = m_navX.getPitch() - 2;
      double front_kF = 0.35;
      double front_kP = 0.11;
      double output = front_kF + (front_kP)*pitch_error;
      gFront.set(ControlMode.PercentOutput, output);

      current_postion = gBack.getSelectedSensorPosition();
    }
    else if (gBack.getSelectedSensorPosition() > 4096) {
      /* hold position when encoder reads a rotation or so above 0 position
       * 
       * #TODO: replace holdPID with upPID. inconjuction with adding the SS error to 'current_position'
       * #TODO: allow manual control while also holding position
       *        - move JS to adjust height, then holds that height when JS reset to 0
       *        - maybe with config kF? set to kF then set back to 0
       * #TODO: add 'unlock front' option to allow front to raise 
       *        - in the ideal case, the drive wheels are on the platform and front gurney can just raise with JS axis
       * #TODO: possible hold angle after front adjustment
       */
      setHoldPID();
      gBack.set(ControlMode.MotionMagic, current_postion);

      // accelerometer PID for front
      double pitch_error = m_navX.getPitch() - 2;
      double front_kF = 0.4;
      double front_kP = 0.12;
      double output = front_kF + (front_kP)*pitch_error;
      gFront.set(ControlMode.PercentOutput, output);
    }
    else {
      /* manual control
       *
       * joystick Y axis * -1 to make up direction output a positive number
       */
      gFront.set(ControlMode.PercentOutput, m_joy.getRawAxis(Constants.gUpFront) * (-1) * dashFrontSpeed);
      gBack.set(ControlMode.PercentOutput, m_joy.getRawAxis(Constants.gUpBack) * (-1)* dashBackSpeed);
    }
    
  }

  public void report() {

    gurneyPitchEntry.setDouble(m_navX.getPitch());
    
}

  public void testInit() {

  }
  
  public void testPeriodic() {
  }

  private double low_pass(double input, double output) {
    if (output == null) { return input; }

    output = output + 0.8 (input - output);
    return output;
  }

}
