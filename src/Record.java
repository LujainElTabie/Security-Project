import java.io.Serializable;
import java.util.ArrayList;

public class Record  implements Serializable {


    private String name;
    private int age;
    private double height;
    private double weight;
    private String sex;
    private String id;

    private String readings ;
    private String reason ;

    private ArrayList<String> tests;


    private Record prevRecord;

    public void setPrevRecord(Record prevRecord) {
        this.prevRecord = prevRecord;
    }

    public Record(ArrayList<String> tests) {
        this.tests = tests;
        this.prevRecord=prevRecord;
    }

    public Record(String readings, String reason) {
        this.readings = readings;
        this.reason = reason;
        this.prevRecord=prevRecord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Record(String name, int age) {
        this.name = name;
        this.age = age;
        this.prevRecord=prevRecord;
    }

    public Record(String name, int age, double height, double weight, String sex, String id) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.id = id;
    }

    @Override
    public String toString() {
        String n=(name!=null)?("name='" + name + '\'') :"";
        String a=(age!=0)?("age='" + age + '\'') :"";
        String h=(height!=0.0)?("height='" + height + '\'') :"";
        String w=(weight!=0.0)?("weight='" + weight + '\'') :"";
        String s=(sex!=null)?("sex='" + sex + '\'') :"";
        String i=(id!=null)?("id='" + id + '\'') :"";
        String read=(readings!=null)?("readings='" + readings + '\'') :"";
        String reas=(reason!=null)?("reason='" + reason + '\'') :"";
        String t=(tests!=null)?("test='" + tests + '\'') :"";
        String p=(prevRecord!=null)?("previous records='" + prevRecord + '\'') :"";
        return "Record{" +
                n+
                a+
                h+
                w +
                s+
                i+
                read +
                reas +
                t+
                p+
                '}';
    }
}
