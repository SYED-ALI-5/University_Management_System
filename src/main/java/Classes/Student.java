package Classes;

public class Student extends Person {
    // ATTRIBUTES
    protected String department;
    protected int fee;
    Student_Status s;
    private int semester;
    private String status;
//    ArrayList<Course> courses;

    // CONSTRUCTORS

    public Student(String id, String name, String fatherName, String CNIC, int age, String gender, String department,
                   String address, String DOB, String PhnNum, String image, int semester, int fee) {
        super(name, gender, age, id, fatherName, CNIC, address, DOB, PhnNum, image);
        setdepartment(department);
        s = Student_Status.Currently_Studding;
        setSemester(semester);
        setFee(fee);
//        courses = new ArrayList<>();
    }

    public Student(String id,String name,String dep,int semester,String status){
        super(name, "", 0, id, "", "", "", "", "", "");
        setDepartment(dep);
        setSemester(semester);
        setStatus(status);

    }


    private void setSemester(int semester) {
        this.semester = semester;
    }

    // SETTERS
    // GETTERS
    public void setdepartment(String department) {
        this.department = department;
    }

    public void setStudentStatus(Student_Status s) {
        this.s = s;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getFee() {
        return fee;
    }

    public int getSemester() {
        return semester;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public Student_Status getS() {
        return s;
    }

    public void setS(Student_Status s) {
        this.s = s;
    }

    public String getDepartment() {
        return department;
    }

    public Student_Status getStudentStatus() {
        return s;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
