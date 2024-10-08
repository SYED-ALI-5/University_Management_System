package Classes;

public class Course {
    private String title;
    private String ID;
    private int creditHr;
    private String department;
    private String level;
    private String marks;

    public Course(String title, String ID, int creditHr, String department, String level) {
        this.title = title;
        this.ID = ID;
        this.creditHr = creditHr;
        this.department = department;
        this.level = level;
    }

    public Course(String id, String title, String cH, String Marks) {
        this.title = title;
        this.ID = id;
        this.creditHr = Integer.parseInt(cH);
        this.marks =Marks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getCreditHr() {
        return creditHr;
    }

    public void setCreditHr(int creditHr) {
        this.creditHr = creditHr;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}