import java.io.Serializable;

public class Record  implements Serializable {
    private String name;
    private int age;
    private double height;
    private double weight;
    private String sex;
    private String id;

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
        return "Record{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", sex='" + sex + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
