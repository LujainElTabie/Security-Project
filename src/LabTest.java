import java.io.Serializable;
import java.util.ArrayList;

public class LabTest implements Serializable {
    ArrayList<String> tests;

    public LabTest() {
        this.tests = new ArrayList<String>();
    }
}
