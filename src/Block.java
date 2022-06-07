import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class Block implements Serializable {



    private int blockHash;
    private int prevBlockHash;

    private HashedRecord [] records;

    private SealedObject encryptedRecords;






    public Block( int prevBlockHash){
        super();
        this.records =new HashedRecord[3];
        this.prevBlockHash=prevBlockHash;
        this.blockHash = Arrays.hashCode(new int []{ Arrays.hashCode(records), this.prevBlockHash});
    }

    public boolean addRecord(SecretKey key, IvParameterSpec ivParameterSpec, HashedRecord record) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        if(record.getHash()==record.getRecord().hashCode()){
            if(this.records[0]==null){
                records[0]=record;
                return true;
            }
            else if(this.records[1]==null){
                records[1]=record;
                return true;
            }
            else if(this.records[2]==null){
                records[2]=record;
                return true;
            }
            HashedRecord [] rec= this.records;
            String algorithm = "AES/CBC/PKCS5Padding";
            SealedObject sealedObject = Doctor.encryptObject(
                    algorithm, rec, key, ivParameterSpec);
            this.encryptedRecords=sealedObject;

        }
        return false;
    }


    public HashedRecord [] readRecord(SecretKey key, IvParameterSpec ivParameterSpec) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException, BadPaddingException, ClassNotFoundException {

        HashedRecord [] rec= this.records;
        String algorithm = "AES/CBC/PKCS5Padding";
        HashedRecord [] object = (HashedRecord []) Doctor.decryptObject(
                algorithm, this.encryptedRecords, key, ivParameterSpec);

        return object;
    }
    /**
     * @return String [] return the transactions
     */


    /**
     * @param transactions the transactions to set
     */


    /**
     * @return int return the blockHash
     */
    public int getBlockHash() {
        return blockHash;
    }

    /**
     * @param blockHash the blockHash to set
     */
    public void setBlockHash(int blockHash) {
        this.blockHash = blockHash;
    }

    /**
     * @return int return the prevBlockHash
     */
    public int getPrevBlockHash() {
        return prevBlockHash;
    }

    /**
     * @param prevBlockHash the prevBlockHash to set
     */
    public void setPrevBlockHash(int prevBlockHash) {
        this.prevBlockHash = prevBlockHash;
    }

    @Override
    public String toString() {
        return "Block{" +
                "record=" + encryptedRecords +
                ", blockHash=" + blockHash +
                ", prevBlockHash=" + prevBlockHash +
                '}';
    }

}
