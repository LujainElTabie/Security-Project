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

public class Blockchain {
    private SecretKey key;

    IvParameterSpec ivParameterSpec = Encryption.generateIv();
    private ArrayList<Block> blockchain = new ArrayList<Block>();

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
        Doctor newDoc = new Doctor();
        Record newRec = new Record("Name0", 18);
        Record newRec1 = new Record("Name1", 18);
        Visit newVis = new Visit("B+", "bayraga3");
        Record newRec2 = new Record("Name2", 18);
        Record newRec3 = new Record("Name3", 18);
        Record newRec4 = new Record("Name0", 18);
        LabTest newLab = new LabTest();
        Record newRec5 = new Record("Name1", 18);
        Record newRec6 = new Record("Name2", 18);
        Record newRec7 = new Record("Name3", 18);
        HashedRecord hashedRecord = newDoc.addRecord(newBlockchain.key, newRec, newBlockchain.ivParameterSpec);
        HashedRecord hashedRecord1 = newDoc.addRecord(newBlockchain.key, newRec1, newBlockchain.ivParameterSpec);
        HashedRecord hashedRecord8 = newDoc.addVisit(newBlockchain.key, newVis , newBlockchain.ivParameterSpec);
        HashedRecord hashedRecord9 = newDoc.addTest(newBlockchain.key, newLab, newBlockchain.ivParameterSpec);
        HashedRecord hashedRecord2 = newDoc.addRecord(newBlockchain.key, newRec2, newBlockchain.ivParameterSpec);
        HashedRecord hashedRecord3 = newDoc.addRecord(newBlockchain.key, newRec3, newBlockchain.ivParameterSpec);
        HashedRecord hashedRecord4 = newDoc.addRecord(newBlockchain.key, newRec4, newBlockchain.ivParameterSpec);
        HashedRecord hashedRecord5 = newDoc.addRecord(newBlockchain.key, newRec5, newBlockchain.ivParameterSpec);
        HashedRecord hashedRecord6 = newDoc.addRecord(newBlockchain.key, newRec6, newBlockchain.ivParameterSpec);
        HashedRecord hashedRecord7 = newDoc.addRecord(newBlockchain.key, newRec7, newBlockchain.ivParameterSpec);
        newBlockchain.addRecord(hashedRecord);
        newBlockchain.addRecord(hashedRecord1);
        newBlockchain.addRecord(hashedRecord2);
        newBlockchain.addRecord(hashedRecord3);
        newBlockchain.addRecord(hashedRecord4);
        newBlockchain.addRecord(hashedRecord5);
        newBlockchain.addRecord(hashedRecord6);
        newBlockchain.addRecord(hashedRecord7);
        newBlockchain.addRecord(hashedRecord8);
        newBlockchain.addRecord(hashedRecord9);
       System.out.println(newBlockchain.toString());
        newDoc.readRecord(newBlockchain.key,hashedRecord.getRecord(), newBlockchain.ivParameterSpec);
        newDoc.readVisit(newBlockchain.key,hashedRecord8.getRecord(),newBlockchain.ivParameterSpec);


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
