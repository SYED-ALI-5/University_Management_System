package Classes;

public class Staff extends Person {
    private double salary;
    private String speciality;

    public Staff(String id, String name, String fatherName, String CNIC, int age, String gender, String address, String DOB,
                 String PhnNum,double salary, String speciality, String image) {
        super(name, gender, age, id, fatherName, CNIC,address,DOB,PhnNum, image);
            setSalary(salary);
            setSalary(salary);
            setSpeciality(speciality);
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double Salary) {
        salary = Salary;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

}
