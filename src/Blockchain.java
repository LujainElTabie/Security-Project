import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Blockchain {
    private SecretKey key;
    private IvParameterSpec ivParameterSpec = Encryption.generateIv();
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

    public void addRecord(HashedRecord record, PublicKey publicKey) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        if(this.blockchain.size()==0){
            Block newBlock = new Block(0);
            newBlock.addRecord(publicKey, this.key, this.ivParameterSpec, record);
            this.blockchain.add(newBlock);
        }else {
            Block block = this.blockchain.get(this.blockchain.size()-1);
            if (block.addRecord(publicKey, this.key, this.ivParameterSpec, record)==1){
                //this.blockchain.add(block);
                this.blockchain.set(this.blockchain.size()-1,block);
            }
            else if (block.addRecord(publicKey, this.key, this.ivParameterSpec, record)==2){
                System.out.println("Cannot add record, try again");
            }
            else {
                Block newBlock = new Block(block.getBlockHash());
                newBlock.addRecord(publicKey, this.key, this.ivParameterSpec, record);
                this.blockchain.add(newBlock);
            }
        }
    }




    public Blockchain() throws NoSuchAlgorithmException {
        this.key = Encryption.generateKey(128);
    }
    public static void main(String[] args) throws Exception {
        Blockchain newBlockchain = new Blockchain();
        while (true){
            Scanner sc= new Scanner(System.in);
            System.out.println("If you would like to add new doctor write 'New doctor'," +
                    "\nif you would like to choose existing doctor write 'Existing doctor'," +
                    "\nif you want to print encrypted blockchain write 'Print:");
            String str= sc.nextLine();

            if(str.equalsIgnoreCase("Print")){
                System.out.print("Blockchain: ");
                System.out.println(newBlockchain.blockchain);
                System.out.println();
            }

            if(str.equalsIgnoreCase("New doctor")){
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
                    newBlockchain.addRecord(rec,newDoc.getPublicKey());
                    System.out.println(rec.getRecord());
                    System.out.println("Patient was added");
                    System.out.println();
                    newDoc.readRecord(rec.getRecord());
                }
                while(true){
                    System.out.println("If you would like to add a new patient write 'add patient'," +
                            "\nif you would like to add new visit to existing patient write 'add visit'," +
                            "\nif you would like to add lab test write 'add lab test'" +
                            "\nif you would like to view patient write 'view'" +
                            "\nwrite 'e' to exit");
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
                        System.out.println("Please add height");
                        double height= sc.nextDouble();
                        System.out.println("Please add weight");
                        double weight= sc.nextDouble();
                        System.out.println("Please add gender");
                        sc.nextLine();
                        String gender= sc.nextLine();
                        System.out.println("Please add patient id");
                        sc.nextLine();
                        String id= sc.nextLine();
                        System.out.println(id);
                        Record newRec = new Record(name,age,height,weight,gender);
                        HashedRecord rec = newDoc.addRecord(id,newRec);
                        newBlockchain.addRecord(rec,newDoc.getPublicKey());
                        System.out.println("Patient was added");
                        System.out.println();
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
                        newBlockchain.addRecord(rec,newDoc.getPublicKey());
                        System.out.println("Visit was added");
                        System.out.println();
                        try {
                            newDoc.readRecord(rec.getRecord());
                        }
                        catch (NullPointerException e){

                        }
                    }
                    if(str2.equals("add lab test")){
                        System.out.println("Please add test name");
                        String name = sc.nextLine();
                        System.out.println("Please add test result");
                        String result = sc.nextLine();
                        System.out.println("Please add test reason");
                        String reason = sc.nextLine();
                        System.out.println("Please enter patient id");
                        String pId= sc.nextLine();
                        Record newRec = new Record(name,result,reason);
                        HashedRecord rec = newDoc.addRecord(pId,newRec);
                        newBlockchain.addRecord(rec,newDoc.getPublicKey());
                        System.out.println("Lab test was added");
                        System.out.println();
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
                            System.out.println("Sorry, this is not your patient");System.out.println();

                        }
                    }
                    if(str2.equals("e")){
                        break;
                    }
                }
            }
            if(str.equalsIgnoreCase("Existing doctor")){
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
                            newBlockchain.addRecord(rec,newDoc.getPublicKey());
                            System.out.println("Patient was added");
                            System.out.println();
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
                            newBlockchain.addRecord(rec, newDoc.getPublicKey());
                            System.out.println("Visit was added");
                            System.out.println();
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
                                System.out.println();
                            }
                        }
                        if(str2.equals("e")){
                            break;
                        }
                    }

                }
                else {
                    System.out.println("Sorry doctor does not exist :( :(");
                    System.out.println();
                }

            }
            for (int i = 0; i < newBlockchain.docIDs.size(); i++) {
                System.out.print("Doctor "+(i+1)+" :");
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
//        Record newRec1 = new Record("Name1", "18");
//        Record newRec2 = new Record("Name2", "18");
//        Record newRec3 = new Record("Name3", "18");
//        Record newRec4 = new Record("Name4", "18");
//        LabTest newLab = new LabTest();
//        Record newRec5 = new Record("Name11", 18);
//        Record newRec6 = new Record("Name21", "18");
//        Record newRec7 = new Record("Name31", "18");
//        HashedRecord hashedRecord = newDoc.addRecord( "1", newRec);
//        HashedRecord hashedRecord1 = newDoc.addRecord( "1", newRec1);
//        HashedRecord hashedRecord2 = newDoc.addRecord( "1", newRec2);
//        HashedRecord hashedRecord3 = newDoc.addRecord("1",  newRec3);
//        HashedRecord hashedRecord4 = newDoc.addRecord("1",  newRec4);
//        HashedRecord hashedRecord5 = newDoc1.addRecord("2", newRec5);
//        HashedRecord hashedRecord6 = newDoc1.addRecord("2", newRec6);
//        HashedRecord hashedRecord7 = newDoc1.addRecord( "2",newRec7);
//        newBlockchain.addRecord(hashedRecord, newDoc.getPublicKey());
//        newBlockchain.addRecord(hashedRecord1, newDoc.getPublicKey());
//        newBlockchain.addRecord(hashedRecord2, newDoc.getPublicKey());
//        newBlockchain.addRecord(hashedRecord3, newDoc.getPublicKey());
//        newBlockchain.addRecord(hashedRecord4, newDoc.getPublicKey());
//        newBlockchain.addRecord(hashedRecord5, newDoc1.getPublicKey());
//        newBlockchain.addRecord(hashedRecord6, newDoc1.getPublicKey());
//        newBlockchain.addRecord(hashedRecord7, newDoc1.getPublicKey());
//
//        System.out.println("blockchain");
//        System.out.println(newBlockchain);
//        newDoc.readRecord(hashedRecord.getRecord());
//        SealedObject[] r =  newBlockchain.blockchain.get(0).readRecord(newBlockchain.key,newBlockchain.ivParameterSpec);
//        System.out.println(newBlockchain.blockchain.get(0));
//        newDoc.readRecord(r[r.length-1]);

    }
}
