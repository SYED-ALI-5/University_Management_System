package Classes;

public class Faculty extends Person {
    private double salary;
    private String department;
    private String specialization;
    Staff_Status status;
    private String scale;
    private Staff_Type type;
//    ArrayList<Course> courses;

    public Faculty(String id, String name, String fatherName, String CNIC, int age, String gender, String specialization,
                   String department, String address, String DOB,String PhnNum, String scale,double salary, Staff_Type type, String image) {
        super(name, gender, age, id, fatherName, CNIC,address,DOB,PhnNum, image);
        setSalary(salary);
        setScale(scale);
        setDepartment(department);
        setSpecialization(specialization);
        status = Staff_Status.On_Duty;
        setType(type);
//        this.courses = new ArrayList<>();
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double Salary) {
        salary = Salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String Specialization) {
        specialization = Specialization;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public Staff_Status getStatus() {
        return status;
    }

    public void setStatus(Staff_Status status) {
        this.status = status;
    }

    public Staff_Type getType() {
        return type;
    }

    public void setType(Staff_Type type) {
        this.type = type;
    }

    // METHODS
//    public void addCourse(Course c) {
//        courses.add(c);
//    }
//
//    public void removeCourse(Course c) {
//        courses.remove(c);
//    }
}
