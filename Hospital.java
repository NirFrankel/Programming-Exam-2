package pe2;

import java.util.*;
/* PLEASE DO NOT MODIFY A SINGLE STATEMENT IN THE TEXT BELOW.
READ THE FOLLOWING CAREFULLY AND FILL IN THE GAPS

I hereby declare that all the work that was required to 
solve the following problem including designing the algorithms
and writing the code below, is solely my own and that I received
no help in creating this solution and I have not discussed my solution 
with anybody. I affirm that I have read and understood
the Senate Policy on Academic honesty at 
https://secretariat-policies.info.yorku.ca/policies/academic-honesty-senate-policy-on/
and I am well aware of the seriousness of the matter and the penalties that I will face as a 
result of committing plagiarism in this assignment.

BY FILLING THE GAPS,YOU ARE SIGNING THE ABOVE STATEMENTS.

Full Name: Nir Frankel
Student Number: 210610459
Course Section: M
*/
import java.util.function.BooleanSupplier;


public class Hospital {
	private  Director Director;
	private List<Patient> patientList;
	private List<Volunteer> volunteerList;
	private List<Physician> physicianList;
	private List<PhysicianAdministrator> physicianAdminList;	
	
	public Hospital(Director director) {
		this.Director = director;
		this.patientList = new ArrayList<Patient>();
		this.volunteerList = new ArrayList<Volunteer>();
		this.physicianList = new ArrayList<Physician>();
		this.physicianAdminList = new ArrayList<PhysicianAdministrator>();
	}
	/**
	 * This method returns the hospital director.
	 * @return Director object
	 */
	public Director getHospDirector() {
		return this.Director;
	}
	/**
	 * This method admits a patient to the hospital. The patient is assigned a physician on a first come
	 * first serve basis. Every patient must be assigned a physician. No duplicate patients can exist.
	 * @param patient that is being admitted
	 * @return this method returns true if the patient was successfully added and false otherwise.
	 * @throws NoSpaceException is thrown if the max capacity of patients has been reached or
	 * there are not enough physicians to handle the patients.
	 */
	public boolean admitPatient(Patient patient) throws NoSpaceException {
		if(this.patientList.size() == 500) {
			throw new NoSpaceException();
		}
		int hasMaxCounter = 0;
		for(Physician p: physicianList) {
			if(p.hasMaximumpatient())
			hasMaxCounter++;
			if(hasMaxCounter == physicianList.size()) {
				throw new NoSpaceException();
			}
		}
		for(Patient p : patientList) {
			if(p.equals(patient))
				return false;
		}
		this.patientList.add(patient);
		for(int j = 0; j < physicianList.size() ; j++) {
			if(!physicianList.get(j).hasMaximumpatient()) {
				physicianList.get(j).setAssignedPatient(patient);
				patient.setAssignedPhysician(physicianList.get(j));
				return true;
			}
		}
		return false;
	}
	/**
	 * This method removes the patient from the hospital's list of patients. 
	 * @param patient being removed
	 * @return returns true if the patient was successfully removed.
	 */
	public boolean dischargePatient(Patient patient) {
		patient.getAssignedPhysician().dischargePatient(patient);
		patientList.remove(patient);
		return true;
	}
	/**
	 * This method returns all patient information stored in the hospital record as a sorted list
	 * according to patient's full name. "firstName, lastName" 
	 * @return sorted list of patients
	 */
	public List<Patient> extractAllPatientDetails() {
		List<Patient> sortedPatientList = new ArrayList<Patient>();
		sortedPatientList.addAll(patientList);
		Collections.sort(sortedPatientList);
		return sortedPatientList;
	}
	/**
	 * This method hires a volunteer to the hospital. The volunteer is assigned a physician on a first come
	 * first serve basis. Every volunteer can be assigned a physician. No duplicate volunteers can exist. 
	 * @param volunteer being hired
	 * @return returns true if the volunteer was successfully hired, and false otherwise.
	 */
	public boolean hireVolunteer(Volunteer volunteer) {
		if(this.volunteerList.size() == 150) {
			return false;
		}
		for(Volunteer v : volunteerList) {
			if(v.equals(volunteer)) 
				return false;
		}
		volunteerList.add(volunteer);
		for(int j = 0; j < physicianList.size() ; j++) {
			if(!physicianList.get(j).hasMaxVolunteers()) {
				physicianList.get(j).setAssignedVolunteer(volunteer);
				volunteer.setAssignedPhysician(physicianList.get(j));
				return true;
			}
		}	
		return false;
	}
	/**
	 * This method removes the volunteer from the hospital's list of volunteers. 
	 * @param volunteer being removed
	 * @return returns true if the volunteer was successfully removed.
	 * @throws NoVolunteersException if there are no volunteers to be removed or a physician
	 *  will be left with no volunteers.
	 */
	public void resignVolunteer(Volunteer volunteer) throws NoVolunteersException{
		int counter = 0;
		if(volunteer.getAssignedPhysician().volunteerList.size() == 1) {
			for(Physician p : physicianList) {
				if(p.volunteerList.size() <= 1) {
					counter++;
				}
				if(counter == physicianList.size())
					throw new NoVolunteersException();
			}
			for(Physician q : physicianList) {
				if(q.equals(volunteer.getAssignedPhysician())) {
					continue;
				}
				if(q.volunteerList.size() >= 2) {
					q.getAssignedVolunteer().setAssignedPhysician(volunteer.getAssignedPhysician());
					volunteerList.remove(volunteer);
					volunteer.getAssignedPhysician().dischargeVolunteer(volunteer);
				}
			}
		}
		volunteerList.remove(volunteer);
		volunteer.getAssignedPhysician().dischargeVolunteer(volunteer);
	}
	/**
	 * This method returns all volunteer information stored in the hospital record as a sorted list
	 * according to volunteer's full name. "firstName, lastName" 
	 * @return sorted list of volunteers
	 */
	public List<Volunteer> extractAllVolunteerDetails() {
		return volunteerList;
	}
	/**
	 * This method hires a physician to the hospital, no duplicate physicians can exist. The physician is
	 * assigned to a physician administrator based on his specialty.
	 * @param physician being hired
	 * @return returns true if the physician was successfully hired and false otherwise.
	 */
	public boolean hirePhysician(Physician physician) {
		if(this.physicianList.size() == 70) {
			return false;
		}
		
		for(Physician p : physicianList) {
			if(physician.equals(p))
				return false;
		}
		physicianList.add(physician);
		for(int j = 0; j < physicianAdminList.size(); j++) {
			if(!physicianAdminList.get(j).hasMaximumPhysicians()) {
				if(physicianAdminList.get(j).getAdminSpecialtyType().equals(physician.getSpecialty())) {
					physicianAdminList.get(j).setAssignedPhysician(physician);
					physician.setAssignedPhysicianAdmin(physicianAdminList.get(j));
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * This method adds a physician administrator to the hospital, there can only be three
	 * physician administrators.
	 * @param physicianAdmin that is being added.
	 * @return return true if the physician administrator was successfully added, and false otherwise.
	 */
	public boolean addAdministrator(PhysicianAdministrator physicianAdmin) {
		if(physicianAdminList.size() == 3) {
			return false;
		}
		for(PhysicianAdministrator p : physicianAdminList) {
			if(physicianAdmin.equals(p))
				return false;
		}
		Director.assignAdministrator(physicianAdmin);
		physicianAdminList.add(physicianAdmin);
		return true;		
	}
	
	/**
	 * This method removes a physician from the hospital list and transfers his patients and volunteers to
	 * the remaining physicians.
	 * @param physician being removed
	 * @throws NoSpecialtyException is thrown if the physician leaving is the only physician representing
	 * his specialty type.
	 */
	public void resignPhysician(Physician physician) throws NoSpecialtyException {
		
		int i = physician.extractPatientDetail().size();
		
		for(Physician p : physicianList) {
			if(p.equals(physician))
				continue;
			while((!p.hasMaximumpatient() && i > 0)) {
				physician.extractPatientDetail().get(i-1).setAssignedPhysician(p);
				p.setAssignedPatient(physician.extractPatientDetail().get(i-1));
				i--;
			}
		}
		
		int specialtyCounter = 0;
		for(Physician p : physicianList) {
			if(!physician.getSpecialty().equals(p.getSpecialty()))
				specialtyCounter++;
		}
		if(physicianList.size() - specialtyCounter == 1) {
			throw new NoSpecialtyException();
		}
//		Exception in the case of not enough Physicians being able to accept the patients being reassigned 
//		if(i > 0) {
//			throw new NoSpecialtyException();
//		}
		int j = physician.extractValunterDetail().size();
		for(Physician p : physicianList) {
			if(p.equals(physician))
				continue;
			while((!p.hasMaxVolunteers() && j > 0)) {
				physician.extractValunterDetail().get(j-1).setAssignedPhysician(p);
				p.setAssignedVolunteer(physician.extractValunterDetail().get(j-1));
				j--;
			}
		}
		physicianList.remove(physician);
	}
	/**
	 * This method returns all physician information stored in the hospital record as a sorted list
	 * according to the physician's full name. "firstName, lastName" 
	 * @return sorted list of physicians
	 */
	public List<Physician> extractAllPhysicianDetails() {
		List<Physician> sortedPhysicianList = new ArrayList<Physician>();
		sortedPhysicianList.addAll(physicianList);
		Collections.sort(sortedPhysicianList);
		return sortedPhysicianList;
	}


}

abstract class Person{
	protected String firstName;
	protected String lastName;
	protected int age;
	protected String gender;
	protected String address;
	
	public Person() {
		
	}
	public Person(String firstName, String lastName, int age, String gender, String address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.gender = gender;
		this.address = address;
	}
	/**
	 * This method sets the first name for our person (patient, volunteer, physician, physician admin, or director)
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * This method sets the last name for our person (patient, volunteer, physician, physician admin, or director)
	 * @param firstName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * This method sets the age for our person (patient, volunteer, physician, physician admin, or director)
	 * @param firstName
	 */
	public void setAge(int age) {
		this.age = age;
	}
	/**
	 * This method sets the gender for our person (patient, volunteer, physician, physician admin, or director)
	 * @param firstName
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * This method sets the address for our person (patient, volunteer, physician, physician admin, or director)
	 * @param firstName
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * This method gets the full name for our person (patient, volunteer, physician, physician admin, or director)
	 * @param firstName
	 */
	public String getName() {
		return this.firstName + ", " + this.lastName;
	}
	/**
	 * This method gets the age for our person (patient, volunteer, physician, physician admin, or director)
	 * @param firstName
	 */
	public int getAge() {
		return this.age;
	}
	/**
	 * This method sets the gender for our person (patient, volunteer, physician, physician admin, or director)
	 * @param firstName
	 */
	public String getGender() {
		return this.gender;
	}
	/**
	 * This method gets the address for our person (patient, volunteer, physician, physician admin, or director)
	 * @param firstName
	 */
	public String getAddress() {
		return this.address;
	}
	/**
	 * This method prints out the relevant person information, all field instances characterizing the
	 * person.
	 */
	public String toString() {
		return "[" + this.firstName + ", " + this.lastName + ", " + this.age + ", "
		+ this.gender + ", " + this.address + "]";
	}
	/**
	 * This method checks for equality between two objects based on the
	 * field instances, and returns true if they are equal and false otherwise.
	 */
	public boolean equals(Object person) {
		if(this == person) {
			return true;
		}
		if(person == null) {
			return false;
		}
		if(this.getClass() != person.getClass()) {		
		return false;
		}
	
		Person other = (Person) person;
		if(this.firstName.equals(other.firstName)) {
			if(this.lastName.equals(other.lastName)) {
				if(this.age == other.age) {
					if(this.gender.equals(other.gender)) {
						if(this.address.equals(other.address)) {
							return true;
						}
						return false;
					}
					return false;
				}
				return false;
			}	
			return false;
		}
	return false;
	}
	/**
	 * This method sorts the object based on field instances comprising the object. 
	 * @param o is the person being compared
	 * @return an int value is returned indicating where the object will be placed in the order.
	 * if the value is equal to 0 then the order is the same, less than zero that the object precedes 
	 * the previous, and if greater than zero the object follows in the order.
	 */
	public int compareTo(Person o) {
		if(this.firstName.equals(o.firstName) && this.lastName.equals(o.lastName) && this.age == o.age
		&& this.gender.equals(o.gender) && this.address.equals(o.address)) {
			return 0;
		}
		else return this.getName().compareTo(o.getName());
	}
}
	

class Patient extends Person implements Comparable<Patient>{
	private static int patientIDCounter = 999;
	private int patientID;
	private Physician physician;
	
	public Patient() {
		super();
		this.patientID = ++patientIDCounter;
	}
	public Patient(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
		this.patientID = ++patientIDCounter;
	}
	/*
	 * This method returns the corresponding patient ID
	 * @return the patient ID.
	 */
	public int getPatientID() {
		return this.patientID;
	}
	/**
	 * This method returns the corresponding physician
	 * @return physician that is assigned to this patient.
	 */
	public Physician getAssignedPhysician() {
		return this.physician;
	}
	/**
	 * This method designates a physician to this patient.
	 * @param physician that is being assigned to the patient.
	 */
	public void setAssignedPhysician(Physician physician) {
		this.physician = physician;
	}
	/**
	 * This method informs us if the patient is designated a physician or not
	 * @return false if the patient is designated a physician and true otherwise.
	 */
	public Boolean clearPatientRecord() {
		if((this).getAssignedPhysician() != null)
			return false;
		else return true;
	}
	@Override
	public int compareTo(Patient o) {
		return super.compareTo(o);
	}
	@Override
	public String toString() {
		return "Patient [" + this.getPatientID() +", " + super.toString() + "]";
	}
}

abstract class Employee extends Person{
	protected int employeeID;
	private static int employeeIDCounter = 99;
	
	public Employee() {
		super();
		this.employeeID = ++employeeIDCounter;
	}
	public Employee(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
		this.employeeID = ++employeeIDCounter;
	}
	/**
	 * This method returns the corresponding employee ID that is unique to every employee
	 * @return integer value starting from 100
	 */
	public int getEmployeeID() {
		return this.employeeID;
	}
	
	public int compareTo(Employee o) {
		return super.compareTo(o);
	}
}

class Volunteer extends Employee{
	private Physician physician;
	
	public Volunteer() {
		super();
	}
	public Volunteer(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
	}
	/**
	 * This method returns the corresponding physician
	 * @return physician that is assigned to this volunteer.
	 */
	public Physician getAssignedPhysician() {
		return this.physician;
	}
	/**
	 * This method designates a physician to this volunteer.
	 * @param physician that is being assigned to the volunteer.
	 */
	public void setAssignedPhysician(Physician physician) {
		this.physician = physician;
	}
	@Override
	public String toString() {
		return "Volunteer [[" + this.getEmployeeID() + "," + super.toString() + "]]";
	}
}

abstract class SalariedEmployee extends Employee{
	protected double salary;
	
	public SalariedEmployee(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
	}
	public SalariedEmployee() {
		super();
	}
	/**
	 * This method sets the salary for the salaried employees
	 * @param double value representing salary.
	 */
	public void setSalary(double salary) {
		this.salary = salary;
	}
	/**
	 * This method retrieves the salary for the salaried employee
	 * @return a double value representing salary.
	 */
	public double getSalary() {
		return this.salary;
	}
}

class Physician extends SalariedEmployee implements Comparable<Physician>{
	protected String specialty;
	private List<Patient> patientList;
	protected List<Volunteer> volunteerList;
	private Patient patient;
	private Volunteer volunteer;
	private PhysicianAdministrator physicianAdmin;
	
	public Physician(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
		this.patientList = new ArrayList<Patient>();
		this.volunteerList = new ArrayList<Volunteer>();
	}
	/**
	 * This method sets the specialty for the designated physician
	 * @param specialty
	 * @throws IllegalArgumentException is thrown is the specialty is not one of the three
	 * Neurology, Dermatology, or Immunology.
	 */
	public void setSpecialty(String specialty) throws IllegalArgumentException{
		if(!specialty.equals("Neurology") && !specialty.equals("Dermatology") && !specialty.equals("Immunology")) {
			throw new IllegalArgumentException();
		}
		this.specialty = specialty;
	}
	/**
	 * This method returns the corresponding specialty of the physician
	 * @return specialty
	 */
	public String getSpecialty() {
		return this.specialty;
	}
	/**
	 * This returns a physician's particular assigned patient.
	 * @return patient
	 */
	public Patient getAssignedPatient() {
		return this.patient;
	}
	/**
	 * This method assigns a particular patient to the physician.
	 * @param patient
	 */
	public void setAssignedPatient(Patient patient) {
		patientList.add(patient);
		this.patient = patient;
	}
	/**
	 * This method removes a particular patient from the physician
	 * @param patient
	 */
	public void dischargePatient(Patient patient) {
		this.patientList.remove(patient);
	}
	/**
	 * This method returns a physician's particular assigned volunteer.
	 * @return volunteer
	 */
	public Volunteer getAssignedVolunteer() {
		return this.volunteer;
	}
	/**
	 * This method assigns a particular volunteer to a physician.
	 * @param volunteer
	 */
	public void setAssignedVolunteer(Volunteer volunteer) {
		volunteerList.add(volunteer);
		this.volunteer = volunteer;
	}
	/**
	 * This method removes the volunteer from the physician.
	 * @param volunteer
	 */
	public void dischargeVolunteer(Volunteer volunteer) {
		this.volunteerList.remove(volunteer);
	}
	/**
	 * This method returns the physician's administrator
	 * @return
	 */
	public PhysicianAdministrator getAssignedPhysicianAdmin() {
		return this.physicianAdmin;
	}
	/**
	 * This method designates a physician administrator for this physician.
	 * @param physicianAdmin
	 */
	public void setAssignedPhysicianAdmin(PhysicianAdministrator physicianAdmin) {
		this.physicianAdmin = physicianAdmin;
	}
	/**
	 * This method determines if the physician can be assigned a volunteer and then assigns.
	 * @param volunteer
	 * @return true if the volunteer was assigned and false otherwise.
	 */
	public boolean assignVolunteer(Employee volunteer) {
		if(this.hasMaxVolunteers() == true) {
			return false;
		}
		this.volunteer = (Volunteer) volunteer;
		volunteerList.add(this.volunteer);
		return true;
	}
	/**
	 * This method determines if the physician has the maximum number of volunteers 
	 * @return integer value 
	 */
	public Boolean hasMaxVolunteers() {
		return this.extractValunterDetail().size() == 5;
	}
	/**
	 * This method determines if the physician has the maximum number of patients 
	 * @return integer value 
	 */
	public Boolean hasMaximumpatient() {
		return this.extractPatientDetail().size() == 8;
	}
	/**
	 * This method returns all volunteer information associated with this physician as a sorted list
	 * according to volunteer's full name. "firstName, lastName" 
	 * @return sorted list of volunteers
	 */
	public List<Volunteer> extractValunterDetail() {
		return volunteerList;
	}
	/**
	 * This method returns all patient information associated with this physician as a sorted list
	 * according to patient's full name. "firstName, lastName" 
	 * @return sorted list of patients
	 */
	public List<Patient> extractPatientDetail() {
		Collections.sort(patientList);
		return patientList;
	}
	@Override
	public String toString() {
		return "Physician [[[" + this.getEmployeeID() + "," + super.toString() + "], "
		+ this.salary + "]]";
	}
	@Override
	public int compareTo(Physician o) {
		return super.compareTo(o);
	}
	
}
abstract class Administrator extends SalariedEmployee{
	
	public Administrator() {
		super();
	}
	
	public Administrator(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
	}
	
}

class PhysicianAdministrator extends Administrator{
	private String specialty;
	private List<Physician> physicianList;
	private Physician physician;
	
	public PhysicianAdministrator() {
		super();
	}
	
	public PhysicianAdministrator(String firstName, String lastName, int age, String gender, String address) {
		super(firstName, lastName, age, gender, address);
		this.physicianList = new ArrayList<Physician>();
	}
	/**
	 * This method assigns a physician to the administrator
	 * @param physician
	 */
	public void setAssignedPhysician(Physician physician) {
		physicianList.add(physician);
		this.physician = physician;
	}
	/**
	 * This method retrieves a particular physician assigned to the administrator
	 * @return
	 */
	public Physician getAssignedPhysician() {
		return this.physician;
	}
	/**
	 * This method sets the specialty for the designated administrator
	 * @param specialty
	 * @throws IllegalArgumentException is thrown is the specialty is not one of the three
	 * Neurology, Dermatology, or Immunology.
	 */
	public void setAdminSpecialtyType(String specialty) throws IllegalArgumentException{
		if(!specialty.equals("Neurology") && !specialty.equals("Dermatology") && !specialty.equals("Immunology")) {
			throw new IllegalArgumentException();
		}
		this.specialty = specialty;
	}
	/**
	 * This method returns the administrator's specialty
	 * @return specialty represented by string value
	 */
	public String getAdminSpecialtyType() {
		return this.specialty;
	}
	/**
	 * This method determines if the physician administrator has exceeded the number of physicians
	 * that have been assigned to them.
	 * @return true if the administrator has reached the maximum, false otherise.
	 */
	public Boolean hasMaximumPhysicians() {
		 return this.extractPhysician().size() == 25;
	}
	/**
	 * This method returns all physician information associated with this administrator as a sorted list
	 * according to physician's full name. "firstName, lastName" 
	 * @return sorted list of physicians
	 */
	public List<Physician> extractPhysician() {
		Collections.sort(physicianList);
		return physicianList;
	}
	
	@Override
	public int compareTo(Employee o) {
		return super.compareTo(o);
	}
	
	@Override
	public String toString() {
		return "PysicianAdministrator [[[" + this.getEmployeeID() + "," + super.toString() + "], "
		+ this.salary + "], " + this.specialty + "]";
	}
}

class Director extends Administrator{
	private List<PhysicianAdministrator> physicianAdminList;
	
	public Director() {
		super();
	}
	public Director(String firstName, String lastName, int age, String gender, String address)   {
		super(firstName, lastName, age, gender, address);
		this.physicianAdminList = new ArrayList<PhysicianAdministrator>();
	}
	/**
	 * This method assigns an administrator to the director 
	 * @param admin is a physician administrator
	 * @return true if the administrator was assigned
	 */
	public Boolean assignAdministrator(PhysicianAdministrator admin) {
		if(extractPhysicianAdmins().size() == 3) {
			return false;
		}
		physicianAdminList.add(admin);
		return true;
	}
	/**
	 * This method returns all administrator information associated with this director as a sorted list
	 * according to administrator's full name. "firstName, lastName" 
	 * @return sorted list of Physician Administrators
	 */
	public List<PhysicianAdministrator> extractPhysicianAdmins() {
		return physicianAdminList;
	}
	
}

class NoSpecialtyException extends Exception{
	public NoSpecialtyException() {
		super();
	}
	public NoSpecialtyException(String e) {
		super(e);
	}
}

class NoSpaceException extends Exception{
	public NoSpaceException() {
		super();
	}
	public NoSpaceException(String e) {
		super(e);
	}
}

class NoVolunteersException extends Exception{
	public NoVolunteersException() {
		super();
	}
	public NoVolunteersException(String e) {
		super(e);
	}
}
