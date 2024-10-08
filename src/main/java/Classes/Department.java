package Classes;

public class Department {

    private String name;
    private String HOD;
    private String DCO;
    private String ID;
    private double fee;

    public Department(String name, String HOD, String DCO, String ID, double fee) {
        this.name = name;
        this.HOD = HOD;
        this.DCO = DCO;
        this.ID = ID;
        this.fee = fee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHOD() {
        return HOD;
    }

    public void setHOD(String HOD) {
        this.HOD = HOD;
    }

    public String getDCO() {
        return DCO;
    }

    public void setDCO(String DCO) {
        this.DCO = DCO;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}
