import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Doctor {

    private SecretKey key;

    private PrivateKey privateKey;

    private PublicKey publicKey;

    IvParameterSpec ivParameterSpec = Encryption.generateIv();

    public String getDoctorID() {
        return doctorID;
    }

    private String doctorID;

    public ArrayList<String> getPatients() {
        return patients;
    }

    public ArrayList<HashedRecord> getLastRecord() {
        return lastRecord;
    }

    private ArrayList<String> patients =new ArrayList<>();

    private ArrayList<HashedRecord> lastRecord= new ArrayList<>();



    @Override
    public String toString() {
        return "Doctor{" +
                "privateKey=" + privateKey +
                ", publicKey=" + publicKey +
                ", doctorID='" + doctorID + '\'' +
                '}';
    }

//    public PrivateKey getPrivateKey() {
//        return privateKey;
//    }
//
//    public void setPrivateKey(PrivateKey privateKey) {
//        this.privateKey = privateKey;
//    }
//
//    public PublicKey getPublicKey() {
//        return publicKey;
//    }
//
//    public void setPublicKey(PublicKey publicKey) {
//        this.publicKey = publicKey;
//    }
//
//    public String getDoctorID() {
//        return doctorID;
//    }
//
//    public void setDoctorID(String doctorID) {
//        this.doctorID = doctorID;
//    }


    public HashedRecord addTest(LabTest newRec) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException {

//        Record [] records = {new Record("Name", 18)};
//        Block newBlock =new Block(records, 0);

        String algorithm = "AES/CBC/PKCS5Padding";
        SealedObject sealedObject = encryptObject(
                algorithm, newRec, this.key, this.ivParameterSpec);
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, this.privateKey);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        String encryptedBlock;
        byte[] encryptedBlockBytes;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(sealedObject);
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
        int hash = encryptedBlock.hashCode();
        HashedRecord hashedRecord = new HashedRecord(hash, encryptedBlockBytes, this.ivParameterSpec);



        return hashedRecord;
    }


    public HashedRecord addVisit( Visit newRec) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException {

//        Record [] records = {new Record("Name", 18)};
//        Block newBlock =new Block(records, 0);

        String algorithm = "AES/CBC/PKCS5Padding";
        SealedObject sealedObject = encryptObject(
                algorithm, newRec, this.key, this.ivParameterSpec);
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, this.privateKey);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        String encryptedBlock;
        byte[] encryptedBlockBytes;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(sealedObject);
            out.flush();
            byte[] blockBytes = bos.toByteArray();
            System.out.println(blockBytes.length);
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
        int hash = encryptedBlock.hashCode();
        HashedRecord hashedRecord = new HashedRecord(hash, encryptedBlockBytes, this.ivParameterSpec);



        return hashedRecord;
    }

    public HashedRecord enRecord(Record newRec) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException, BadPaddingException {

//        Record [] records = {new Record("Name", 18)};
//        Block newBlock =new Block(records, 0);


        String algorithm = "AES/CBC/PKCS5Padding";
        SealedObject sealedObject = encryptObject(
                algorithm, newRec, this.key, this.ivParameterSpec);
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, this.privateKey);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        String encryptedBlock;
        byte[] encryptedBlockBytes;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(sealedObject);
            out.flush();
            byte[] blockBytes = bos.toByteArray();
            System.out.println(blockBytes.length);
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
        int hash = encryptedBlock.hashCode();
        HashedRecord hashedRecord = new HashedRecord(hash, encryptedBlockBytes, this.ivParameterSpec);



        return hashedRecord;
    }


    public HashedRecord addRecord(String pID, Record newRec) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException, BadPaddingException {

//        Record [] records = {new Record("Name", 18)};
//        Block newBlock =new Block(records, 0);
        Record prev = null;
        newRec.setId(pID);
            System.out.println("not null");
            for (int i = 0; i < this.patients.size(); i++) {
                if (this.patients.get(i).equals(pID)) {
                    HashedRecord hashedRecord = this.lastRecord.get(i);
                    prev = this.readRecord(hashedRecord.getRecord());
                    newRec.setPrevRecord(prev);
                    this.lastRecord.set(i, this.enRecord(newRec));
                    System.out.println(this.enRecord(newRec));
                }
            }


            if (prev != null) {
                System.out.println("fm");
                newRec.setPrevRecord(prev);
            } else {
                System.out.println("hi");
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
        SealedObject sealedObject = encryptObject(
                algorithm, newRec, this.key, this.ivParameterSpec);
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, this.privateKey);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        String encryptedBlock;
        byte[] encryptedBlockBytes;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(sealedObject);
            out.flush();
            byte[] blockBytes = bos.toByteArray();
            System.out.println(blockBytes.length);
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
        int hash = encryptedBlockBytes.hashCode();
        HashedRecord hashedRecord = new HashedRecord(hash, encryptedBlockBytes, this.ivParameterSpec);



        return hashedRecord;
    }

    public void readVisit(byte[] record) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, this.publicKey);
        byte[] decryptedBlockBytes = decryptCipher.doFinal(record);
        ByteArrayInputStream bis = new ByteArrayInputStream(decryptedBlockBytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o =  in.readObject();
            SealedObject x = (SealedObject) o;

//            IvParameterSpec ivParameterSpec = generateIv();
            String algorithm = "AES/CBC/PKCS5Padding";
            Visit object = (Visit) decryptObject(
                    algorithm, x, this.key, this.ivParameterSpec);
            System.out.println(object.toString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }


    }

    public void readTest(byte[] record) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, this.publicKey);
        byte[] decryptedBlockBytes = decryptCipher.doFinal(record);
        ByteArrayInputStream bis = new ByteArrayInputStream(decryptedBlockBytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o =  in.readObject();
            SealedObject x = (SealedObject) o;

//            IvParameterSpec ivParameterSpec = generateIv();
            String algorithm = "AES/CBC/PKCS5Padding";
            LabTest object = (LabTest) decryptObject(
                    algorithm, x, this.key, this.ivParameterSpec);
            System.out.println(object.toString());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }


    }


    public Record readRecord(byte[] record) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        try{
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, this.publicKey);
            byte[] decryptedBlockBytes = decryptCipher.doFinal(record);
            ByteArrayInputStream bis = new ByteArrayInputStream(decryptedBlockBytes);
            ObjectInput in = null;
            try {
                in = new ObjectInputStream(bis);
                Object o =  in.readObject();
                SealedObject x = (SealedObject) o;

//            IvParameterSpec ivParameterSpec = generateIv();
                String algorithm = "AES/CBC/PKCS5Padding";
                Record object = (Record) decryptObject(
                        algorithm, x, this.key, this.ivParameterSpec);
                System.out.println(object.toString());
                return object;
            } catch (ClassNotFoundException e) {
                System.out.println("Sorry you cannot access this");
                throw new RuntimeException(e);
            } catch (InvalidAlgorithmParameterException e) {
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


        }

        catch (BadPaddingException e){
            System.out.println("Sorry you cannot access this");
            return null;
        }
    }



    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static SealedObject encryptObject(String algorithm, Serializable object,
                                             SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IOException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        SealedObject sealedObject = new SealedObject(object, cipher);
        return sealedObject;
    }

    public static Serializable decryptObject(String algorithm, SealedObject sealedObject,
                                             SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            ClassNotFoundException, BadPaddingException, IllegalBlockSizeException,
            IOException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        Serializable unsealObject = (Serializable) sealedObject.getObject(cipher);
        return unsealObject;
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public Doctor() throws NoSuchAlgorithmException {
        this.key = generateKey(128);
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(5120);
        KeyPair pair = generator.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
        this.doctorID = UUID.randomUUID().toString();
        try (FileOutputStream fos = new FileOutputStream("public"+doctorID+".key")) {
            fos.write(publicKey.getEncoded());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            InvalidAlgorithmParameterException, NoSuchPaddingException, IOException,
            BadPaddingException, ClassNotFoundException {
        Doctor newDoc = new Doctor();



        //RSA
        //Encrypt
        String secretMessage = "Baeldung secret message";
        Cipher encryptCipher = Cipher.getInstance("RSA");
        //encryptCipher.init(Cipher.ENCRYPT_MODE, newDoc.getPrivateKey());
        byte[] secretMessageBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
        String encryptedMessage = new String(encryptedMessageBytes, StandardCharsets.UTF_8);

        //Decrypt
        Cipher decryptCipher = Cipher.getInstance("RSA");
        //decryptCipher.init(Cipher.DECRYPT_MODE, newDoc.getPublicKey());
        byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
        String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);


        //Block
//        String[] initValues = {"Shad has $700", "Miguel has $550"};
//        Block firstBlock = new Block(initValues, 0);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectOutputStream out = null;
//        String encryptedBlock;
//        byte[] encryptedBlockBytes;
//        try {
//            out = new ObjectOutputStream(bos);
//            out.writeObject(firstBlock);
//            out.flush();
//            byte[] blockBytes = bos.toByteArray();
//            encryptedBlockBytes = encryptCipher.doFinal(blockBytes);
//            encryptedBlock = new String(encryptedBlockBytes, StandardCharsets.UTF_8);
//
//        } finally {
//            try {
//                bos.close();
//            } catch (IOException ex) {
//                // ignore close exception
//            }
//        }


//        byte[] decryptedBlockBytes = decryptCipher.doFinal(encryptedBlockBytes);
//
//        ByteArrayInputStream bis = new ByteArrayInputStream(decryptedBlockBytes);
//        ObjectInput in = null;
//        try {
//            in = new ObjectInputStream(bis);
//            Object o = (Block) in.readObject();
//
//        } finally {
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException ex) {
//                // ignore close exception
//            }
//        }
//
//
//        //AES
//        SecretKey key = generateKey(128);
//        IvParameterSpec ivParameterSpec = generateIv();
//        String algorithm = "AES/CBC/PKCS5Padding";
//        SealedObject sealedObject = encryptObject(
//                algorithm, firstBlock, key, ivParameterSpec);
//        Block object = (Block) decryptObject(
//                algorithm, sealedObject, key, ivParameterSpec);
//
//    }


}}
