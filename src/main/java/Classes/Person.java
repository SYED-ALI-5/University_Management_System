package Classes;

import java.io.Serializable;

abstract class Person implements Serializable {
    // ATTRIBUTES
    protected String Name;
    protected String address;
    protected String DOB;
    protected String img;
    protected String PhnNum;
    protected String Gender;
    protected String Id;
    protected String fatherName;
    protected String CNIC;
    protected int age;

    // CONSTRUCTORS
    public Person(String name, String gender, int age, String id, String fatherName, String CNIC,String address,String DOB,String PhnNum, String image) {
        setName(name);
        setGender(gender);
        setAge(age);
        setId(id);
        setAddress(address);
        setDOB(DOB);
        setPhnNum(PhnNum);
        setfatherName(fatherName);
        setCNIC(CNIC);
        setImg(image);
    }

    // SETTERS
    public void setName(String name) {
        this.Name = name;
    }
    public void setImg(String image) {
        this.img = image;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public void setfatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getPhnNum() {
        return PhnNum;
    }

    public void setPhnNum(String phnNum) {
        PhnNum = phnNum;
    }

    // GETTERS
    public String getName() {
        return Name;
    }

    public String getGender() {
        return Gender;
    }

    public int getAge() {
        return age;
    }

    public String getId() {
        return Id;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getCNIC() {
        return CNIC;
    }

    public String getImg() {
        return img;
    }
}
