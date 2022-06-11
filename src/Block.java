import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;

public class Block implements Serializable {
    private final SealedObject[] records;
    private int blockHash;
    private int prevBlockHash;
    private SealedObject encryptedRecords;


    public Block(int prevBlockHash) {
        super();
        this.records = new SealedObject[3];
        this.prevBlockHash = prevBlockHash;
        this.blockHash = Arrays.hashCode(new int[]{Arrays.hashCode(records), this.prevBlockHash});
    }

    public int addRecord(PublicKey publicKey, SecretKey key, IvParameterSpec ivParameterSpec, HashedRecord record) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        int o = 0;
        try {
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] decryptedBlockBytes = decryptCipher.doFinal(record.getHash());
            ByteArrayInputStream bis = new ByteArrayInputStream(decryptedBlockBytes);
            ObjectInput in = null;
            try {
                in = new ObjectInputStream(bis);
                o = (int) in.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("Sorry you cannot access this");
                throw new RuntimeException(e);
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                    System.out.println("Sorry you cannot access this");
                }
            }
        } catch (BadPaddingException e) {
            System.out.println("Sorry you cannot access this");
        }
        SealedObject r = record.getRecord();
        int hash = r.hashCode();
        if (o == hash) {
            SealedObject[] rec = this.records;
            String algorithm = "AES/CBC/PKCS5Padding";
            SealedObject sealedObject = Encryption.encryptObject(
                    algorithm, rec, key, ivParameterSpec);
            this.encryptedRecords = sealedObject;
            if (this.records[0] == null) {
                records[0] = record.getRecord();
                return 1;
            } else if (this.records[1] == null) {
                records[1] = record.getRecord();
                return 1;
            } else if (this.records[2] == null) {
                records[2] = record.getRecord();
                return 1;
            }
        } else {
            return 2;
        }
        return 0;
    }


    public SealedObject[] readRecord(SecretKey key, IvParameterSpec ivParameterSpec) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException, BadPaddingException, ClassNotFoundException {

        SealedObject[] rec = this.records;
        String algorithm = "AES/CBC/PKCS5Padding";
        SealedObject[] object = (SealedObject[]) Encryption.decryptObject(
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
