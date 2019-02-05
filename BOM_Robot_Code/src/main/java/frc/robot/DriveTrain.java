//Conner Taylor
//Drive Train of Robot


package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.ecommons.RobotMap;
import frc.ecommons.Constants;

// import edu.wpi.first.wpilibj.shuffleboard;
 
public class DriveTrain  {

<<<<<<< HEAD
  String lowGear = "Low Gear";
  String highGear = "High Gear";

  

=======
>>>>>>> 751b40738ae9ca847f277a7cb4abd6c49da4cddf
  // Joysticks/Controllers
  Joystick m_joy;

  //Talons
  WPI_TalonSRX m_rMaster;
  WPI_TalonSRX m_lMaster;
  
  //Victors
  WPI_VictorSPX m_rSlave1;
  WPI_VictorSPX m_rSlave2;
  WPI_VictorSPX m_lSlave1;
  WPI_VictorSPX m_lSlave2;

  //Solenoids
  DoubleSolenoid dogGearSolenoid;

  //Loops
  boolean dgLoop = false;


  // ShuffleboardTab tab = Shuffleboard.getTab("max motor speeds");
  // private NetworkTableEntry maxSpeed = tab.add("Drive Speed", 0)
  //                                             .withWidget(BuiltInWidgets.kNumberSlide)
  //                                             .withProperties(Map.of("min", 0, "max", 1))
  //                                             .getEntry();

  public void TalonConfig() {
    //Configs Talon to default
    m_rMaster.configFactoryDefault();
    m_lMaster.configFactoryDefault();
    m_rSlave1.configFactoryDefault();
    m_rSlave2.configFactoryDefault();
    m_lSlave1.configFactoryDefault();
    m_lSlave2.configFactoryDefault();

    //Motors go right way
    m_rMaster.setSensorPhase(false);
    m_lMaster.setSensorPhase(false);
    m_rSlave1.setSensorPhase(false);
    m_rSlave2.setSensorPhase(false);
    m_lSlave1.setSensorPhase(false);
    m_lSlave2.setSensorPhase(false);





    
  }
  public void robotInit(Joystick j) {

    
    //Joysticks
    m_joy = j;

    //Talons - IDS found in ecommons.RobotMap
    m_rMaster = new WPI_TalonSRX(RobotMap.rMaster);
    m_lMaster = new WPI_TalonSRX(RobotMap.lMaster);

    //Victors - IDS found in ecommons.RobotMap
    m_rSlave1 = new WPI_VictorSPX(RobotMap.rSlave1);
    m_rSlave2 = new WPI_VictorSPX(RobotMap.rSlave2);
    m_lSlave1 = new WPI_VictorSPX(RobotMap.lSlave1);
    m_lSlave2 = new WPI_VictorSPX(RobotMap.lSlave2);

    //DoubleSolenoid
    dogGearSolenoid = new DoubleSolenoid(RobotMap.dogGearSolenoid1, RobotMap.dogGearSolenoid2);

    TalonConfig();
    SmartDashboard.putNumber("Drive Train", 1);
  }

  


  
  public void autonomousInit() {

  }


  
  public void autonomousPeriodic() {


  }

  /**
   * This function is called periodically during operator control.
   */
  
  public void teleopInit() {
    dogGearSolenoid.set(DoubleSolenoid.Value.kForward);


  } 
  public void teleopPeriodic() {
    //Dog Gear Shift
    if (m_joy.getRawButton(Constants.gearShift) && !dgLoop) {
      dgLoop = true;
      if (dogGearSolenoid.get() == (Value.kForward)) {
        dogGearSolenoid.set(DoubleSolenoid.Value.kReverse);
        
        SmartDashboard.putString("Gear", lowGear);
      } else if (dogGearSolenoid.get() == (Value.kReverse)) {
        dogGearSolenoid.set(DoubleSolenoid.Value.kForward);
      
        SmartDashboard.putString("Gear", highGear);
      }

    }
    if (!m_joy.getRawButton(Constants.gearShift)) {
      dgLoop = false;
    }

    
    //Equation for ARCADE DRIVE
    double xAxis, yAxis;
				xAxis = m_joy.getRawAxis(Constants.xAxis);
				yAxis = m_joy.getRawAxis(Constants.yAxis);
				
				//Equation for Arcade Drive
				double leftSide, rightSide;
				rightSide = yAxis + xAxis;
        leftSide = xAxis - yAxis;
    
    //Percent drive output with slave follows
    m_rMaster.set(ControlMode.PercentOutput, rightSide * driveSpeed);
      m_rSlave1.follow(m_rMaster);
      m_rSlave2.follow(m_rMaster);

    m_lMaster.set(ControlMode.PercentOutput, leftSide * driveSpeed);
      m_lSlave1.follow(m_lMaster);
      m_lSlave2.follow(m_lMaster);

  }

  public void report() {
    // driveSpeed = SmartDashboard.getNumber("Drive Train", 1);
    // driveSpeed = maxSpeed.getDouble(0.3);
  }

  /**
   * This function is called periodically during test mode.
   */
  
  public void testPeriodic() {
  }
}
