import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

public class Doctor {

    private IvParameterSpec ivParameterSpec = Encryption.generateIv();
    private final SecretKey key;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final String doctorID;
    private final ArrayList<String> patients = new ArrayList<>();
    private final ArrayList<HashedRecord> lastRecord = new ArrayList<>();

    public String getDoctorID() {
        return doctorID;
    }
    public ArrayList<String> getPatients() {
        return patients;
    }
    public ArrayList<HashedRecord> getLastRecord() {
        return lastRecord;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }


    public Doctor() throws NoSuchAlgorithmException {
        this.key = Encryption.generateKey(128);
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(5120);
        KeyPair pair = generator.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
        this.doctorID = UUID.randomUUID().toString();
//        try (FileOutputStream fos = new FileOutputStream("public"+doctorID+".key")) {
//            fos.write(publicKey.getEncoded());
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public String toString() {
        return "Doctor{" +
                ", publicKey=" + publicKey +
                ", doctorID='" + doctorID + '\'' +
                '}';
    }

    public HashedRecord enRecord(Record newRec) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException, BadPaddingException {
        //AES
        String algorithm = "AES/CBC/PKCS5Padding";
        SealedObject sealedObject = Encryption.encryptObject(
                algorithm, newRec, this.key, this.ivParameterSpec);

        //Hash of cipher
        int hash = sealedObject.hashCode();

        //RSA (Private Key)
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, this.privateKey);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        String encryptedBlock;
        byte[] encryptedBlockBytes;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(hash);
            out.flush();
            byte[] blockBytes = bos.toByteArray();
            encryptedBlockBytes = encryptCipher.doFinal(blockBytes);
            encryptedBlock = new String(encryptedBlockBytes, StandardCharsets.UTF_8);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
            }
        }

        HashedRecord hashedRecord = new HashedRecord(encryptedBlockBytes, sealedObject, this.ivParameterSpec);
        return hashedRecord;
    }

    public HashedRecord addRecord(String pID, Record newRec) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException, BadPaddingException, ClassNotFoundException {
        Record prev = null;
        newRec.setId(pID);

        for (int i = 0; i < this.patients.size(); i++) {
            if (this.patients.get(i).equals(pID)) {
                HashedRecord hashedRecord = this.lastRecord.get(i);
                prev = this.readRecord(hashedRecord.getRecord());
                newRec.setPrevRecord(prev);
                this.lastRecord.set(i, this.enRecord(newRec));
            }
        }
        if (prev != null) {
            newRec.setPrevRecord(prev);
        } else {
            if (newRec.getName() == null) {
                System.out.println("There are no previous records of this patient, please add general info.");
                return null;
            } else {
                newRec.setPrevRecord(null);
                this.patients.add(pID);
                this.lastRecord.add(this.enRecord(newRec));
            }
        }


        String algorithm = "AES/CBC/PKCS5Padding";
        SealedObject sealedObject = Encryption.encryptObject(
                algorithm, newRec, this.key, this.ivParameterSpec);

        int hash = sealedObject.hashCode();
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, this.privateKey);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        String encryptedBlock;
        byte[] encryptedBlockBytes;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(hash);
            out.flush();
            byte[] blockBytes = bos.toByteArray();
            encryptedBlockBytes = encryptCipher.doFinal(blockBytes);
            encryptedBlock = new String(encryptedBlockBytes, StandardCharsets.UTF_8);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }

        HashedRecord hashedRecord = new HashedRecord(encryptedBlockBytes, sealedObject, this.ivParameterSpec);
        return hashedRecord;
    }

    public Record readRecord(SealedObject record) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, ClassNotFoundException {
        //Decrypt AES
        String algorithm = "AES/CBC/PKCS5Padding";
        Record object = (Record) Encryption.decryptObject(
                algorithm, record, this.key, this.ivParameterSpec);
        //Print encrypted
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(record);
            byte[] bytes = bos.toByteArray();
            System.out.print("Encrypted record: ");
            System.out.println(Arrays.toString(bytes));
//            String base64encoded = Base64.getEncoder().encodeToString(bytes);
//            System.out.println(base64encoded);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Print after decryption
        System.out.print("Decrypted record: ");
        System.out.println(object.toString());
        return object;
    }
}
