import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Blockchain {
    private SecretKey key;

    IvParameterSpec ivParameterSpec = Encryption.generateIv();
    private ArrayList<Block> blockchain = new ArrayList<Block>();

    private ArrayList<String> docIDs = new ArrayList<String>();
    private ArrayList<Doctor> doctors = new ArrayList<Doctor>();

    @Override
    public String toString() {
        return "Blockchain{" +
                "key=" + key +
                ", blockchain=" + blockchain +
                '}';
    }

    public void addRecord(HashedRecord record) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        if(this.blockchain.size()==0){
            Block newBlock = new Block(0);
            newBlock.addRecord(this.key, this.ivParameterSpec, record);
            this.blockchain.add(newBlock);
            System.out.println("Record added1");
        }else {
            Block block = this.blockchain.get(this.blockchain.size()-1);
            if (block.addRecord(this.key, this.ivParameterSpec, record)){
                //this.blockchain.add(block);
                this.blockchain.set(this.blockchain.size()-1,block);
                System.out.println("Record added");
            }
            else {
                Block newBlock = new Block(block.getBlockHash());
                newBlock.addRecord(this.key, this.ivParameterSpec, record);
                this.blockchain.add(newBlock);
                System.out.println("Record added2");
            }
        }
    }



    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
//        try (FileOutputStream fos = new FileOutputStream("secret.key")) {
//            fos.write(key.getEncoded());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
       // System.out.println(key);
        return key;
    }

    public Blockchain() throws NoSuchAlgorithmException {
        this.key = generateKey(128);
    }
    public static void main(String[] args) throws Exception {
        Blockchain newBlockchain = new Blockchain();
        while (true){
            Scanner sc= new Scanner(System.in); //System.in is a standard input stream
            System.out.println("If you would like to add new doctor write 'New doctor', " +
                    "if you would like to choose existing doctor write 'Existing doctor':");
            String str= sc.nextLine();

            if(str.equals("New doctor")){
                Doctor newDoc = new Doctor();
                newBlockchain.docIDs.add(newDoc.getDoctorID());
                newBlockchain.doctors.add(newDoc);
                System.out.println("New doctor created, id = " + newDoc.getDoctorID());
                System.out.println("Would you like to add patient?");
                String str1= sc.nextLine();
                if(str1.equals("yes")){
                    System.out.println("Please add name");
                    String name= sc.nextLine();
                    System.out.println("Please add age");
                    int age;
                    while(true){
                        try{
                            age= sc.nextInt();
                            break;
                        }catch(InputMismatchException e){
                            System.out.println("Please add age in numbers");
                            sc.next();
                        }
                    }

                    System.out.println("Please add patient id");
                    sc.nextLine();
                    String id= sc.nextLine();
                    System.out.println(id);
                    Record newRec = new Record(name,age);
                    HashedRecord rec = newDoc.addRecord(id,newRec);
                    System.out.println("patient was added");
                    newDoc.readRecord(rec.getRecord());
                }
                while(true){
                    System.out.println("If you would like to add a new patient write 'add patient',\nif you would like to add new visit to existing patient write 'add visit',\nif you would like to view patient write 'view'\nwrite 'e' to exit");
                    String str2= sc.nextLine();
                    if (str2.equals("add patient")){
                        System.out.println("Please add name");
                        String name= sc.nextLine();
                        System.out.println("Please add age");
                        int age;
                        while(true){
                            try{
                                age= sc.nextInt();
                                break;
                            }catch(InputMismatchException e){
                                System.out.println("Please add age in numbers");
                                sc.next();
                            }
                        }
                        System.out.println("Please add patient id");
                        sc.nextLine();
                        String id= sc.nextLine();
                        System.out.println(id);
                        Record newRec = new Record(name,age);
                        HashedRecord rec = newDoc.addRecord(id,newRec);
                        newBlockchain.addRecord(rec);
                        System.out.println("patient was added");
                        newDoc.readRecord(rec.getRecord());
                    }
                    if(str2.equals("add visit")){
                        System.out.println("Please add readings");
                        String reading = sc.nextLine();
                        System.out.println("Please add reason");
                        String reason = sc.nextLine();
                        System.out.println("Please enter patient id");
                        String pId= sc.nextLine();
                        Record newRec = new Record(reading,reason);
                        HashedRecord rec = newDoc.addRecord(pId,newRec);
                        newBlockchain.addRecord(rec);
                        System.out.println("Patient was added");
                        try {
                            newDoc.readRecord(rec.getRecord());
                        }
                        catch (NullPointerException e){

                        }
                    }
                    if(str2.equals("view")){
                        System.out.println("Please enter patient id");
                        String pId= sc.nextLine();
                        boolean pExists=false;
                        for (int i = 0; i < newDoc.getPatients().size(); i++) {
                            if (newDoc.getPatients().get(i).equals(pId)){
                                newDoc.readRecord(newDoc.getLastRecord().get(i).getRecord());
                                pExists=true;

                            }
                        }
                        if (!pExists){
                            System.out.println("Sorry, this is not your patient");
                        }
                    }
                    if(str2.equals("e")){
                        break;
                    }
                }
            }
            if(str.equals("Existing doctor")){
                System.out.println("Please enter doctor's id");
                String str1= sc.nextLine();
                Doctor newDoc = null;
                boolean check=false;
                for (int i = 0; i < newBlockchain.docIDs.size(); i++) {
                    if (newBlockchain.docIDs.get(i).equals(str1)){
                        newDoc = newBlockchain.doctors.get(i);
                        check=true;
                    }
                }
                if(check){
                    while(true){
                        System.out.println("If you would like to add a new patient write 'add patient',\nif you would like to add new visit to existing patient write 'add visit',\nif you would like to view patient write 'view'\nwrite 'e' to exit");
                        String str2= sc.nextLine();
                        if (str2.equals("add patient")){
                            System.out.println("Please add name");
                            String name= sc.nextLine();
                            System.out.println("Please add age");
                            int age= sc.nextInt();
                            System.out.println("Please add patient id");
                            sc.nextLine();
                            String id= sc.nextLine();
                            System.out.println(id);
                            Record newRec = new Record(name,age);
                            HashedRecord rec = newDoc.addRecord(id,newRec);
                            newBlockchain.addRecord(rec);
                            System.out.println("patient was added");
                            newDoc.readRecord(rec.getRecord());
                        }
                        if(str2.equals("add visit")){
                            System.out.println("Please add readings");
                            String reading = sc.nextLine();
                            System.out.println("Please add reason");
                            String reason = sc.nextLine();
                            System.out.println("Please enter patient id");
                            String pId= sc.nextLine();
                            Record newRec = new Record(reading,reason);
                            HashedRecord rec = newDoc.addRecord(pId,newRec);
                            newBlockchain.addRecord(rec);
                            System.out.println("Patient was added");
                            try {
                                newDoc.readRecord(rec.getRecord());
                            }
                            catch (NullPointerException e){

                            }
                        }
                        if(str2.equals("view")){
                            System.out.println("Please enter patient id");
                            String pId= sc.nextLine();
                            boolean pExists=false;
                            for (int i = 0; i < newDoc.getPatients().size(); i++) {
                                if (newDoc.getPatients().get(i).equals(pId)){
                                    newDoc.readRecord(newDoc.getLastRecord().get(i).getRecord());
                                    pExists=true;

                                }
                            }
                            if (!pExists){
                                System.out.println("Sorry, this is not your patient");
                            }
                        }
                        if(str2.equals("e")){
                            break;
                        }
                    }

                }
                else {
                    System.out.println("Sorry doctor does not exist :( :(");
                }

            }
            for (int i = 0; i < newBlockchain.docIDs.size(); i++) {
                System.out.println(newBlockchain.docIDs.get(i));
            }
        }


        //wrong key
//        Doctor newDoc = new Doctor();
//        Doctor newDoc1 = new Doctor();
//        Record newRec = new Record("Name0", 18);
//        Record newRec5 = new Record("Name11", 18);
//        HashedRecord hashedRecord = newDoc.addRecord( "1", newRec);
//        newBlockchain.addRecord(hashedRecord);
//        newDoc1.readRecord(hashedRecord.getRecord());



//        Doctor newDoc = new Doctor();
//        Doctor newDoc1 = new Doctor();
//        Record newRec = new Record("Name0", 18);
//        Record newRec1 = new Record("Name1", 18);
//        Visit newVis = new Visit("B+", "bayraga3");
//        Record newRec2 = new Record("Name2", 18);
//        Record newRec3 = new Record("Name3", 18);
//        Record newRec4 = new Record("Name4", 18);
//        LabTest newLab = new LabTest();
//        Record newRec5 = new Record("Name11", 18);
//        Record newRec6 = new Record("Name21", 18);
//        Record newRec7 = new Record("Name31", 18);
//        HashedRecord hashedRecord = newDoc.addRecord( "1", newRec, newBlockchain.ivParameterSpec);
//        HashedRecord hashedRecord1 = newDoc.addRecord( "1", newRec1, newBlockchain.ivParameterSpec);
//        HashedRecord hashedRecord8 = newDoc.addVisit( newVis , newBlockchain.ivParameterSpec);
//        HashedRecord hashedRecord9 = newDoc.addTest( newLab, newBlockchain.ivParameterSpec);
//        HashedRecord hashedRecord2 = newDoc.addRecord( "1", newRec2, newBlockchain.ivParameterSpec);
//        HashedRecord hashedRecord3 = newDoc.addRecord("1",  newRec3, newBlockchain.ivParameterSpec);
//        HashedRecord hashedRecord4 = newDoc.addRecord("1",  newRec4, newBlockchain.ivParameterSpec);
//        HashedRecord hashedRecord5 = newDoc1.addRecord("2", newRec5, newBlockchain.ivParameterSpec);
//        HashedRecord hashedRecord6 = newDoc1.addRecord("2", newRec6, newBlockchain.ivParameterSpec);
//        HashedRecord hashedRecord7 = newDoc1.addRecord( "2",newRec7, newBlockchain.ivParameterSpec);
//        newBlockchain.addRecord(hashedRecord);
//        newBlockchain.addRecord(hashedRecord1);
//        newBlockchain.addRecord(hashedRecord2);
//        newBlockchain.addRecord(hashedRecord3);
//        newBlockchain.addRecord(hashedRecord4);
//        newBlockchain.addRecord(hashedRecord5);
//        newBlockchain.addRecord(hashedRecord6);
//        newBlockchain.addRecord(hashedRecord7);
//        newBlockchain.addRecord(hashedRecord8);
//        newBlockchain.addRecord(hashedRecord9);
//        System.out.println(newBlockchain.toString());
//        newDoc.readRecord(hashedRecord.getRecord(), newBlockchain.ivParameterSpec);
//        newDoc.readVisit(hashedRecord8.getRecord(),newBlockchain.ivParameterSpec);
//        HashedRecord [] r =  newBlockchain.blockchain.get(0).readRecord(newBlockchain.key,newBlockchain.ivParameterSpec);
//        System.out.println(newBlockchain.blockchain.get(0));
//        newDoc.readRecord(r[r.length-1].getRecord(),newBlockchain.ivParameterSpec);

//        String statement1  ="My world is perfect.";
//        int hashValue1 = statement1.hashCode();
//        System.out.println("Statement 1 = "+ statement1+" and it's hash is  "+ hashValue1);
//        String [] list1 = {"alex", "becky", "cyril"};
//        String [] list2 = {"alex", "becky", "Cyril"};
//        int hash1= Arrays.hashCode(list1);
//        int hash2= Arrays.hashCode(list2);
//        System.out.println("List 1 hash is  "+ hash1);
//        System.out.println("List 2 hash is  "+ hash2);
//
//        ArrayList<Block> blockChain= new ArrayList<Block>();
//        String [] initValues ={"Shad has $700", "Miguel has $550"};
//        Block firstBlock = new Block(initValues,0);
//        blockChain.add(firstBlock);
//        System.out.println("First block is  "+ firstBlock.toString());
//        System.out.println("Blockchain is  "+ blockChain.toString());
//        String [] values ={"Shad gives Miguel $200"};
//        Block secondBlock = new Block(values,firstBlock.getBlockHash());
//        blockChain.add(secondBlock);
//        System.out.println("Second block is  "+ secondBlock.toString());
//        System.out.println("Blockchain is  "+ blockChain.toString());




    }
}
