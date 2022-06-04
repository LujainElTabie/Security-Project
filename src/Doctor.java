import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.UUID;

public class Doctor {

    //private SecretKey key;

    private PrivateKey privateKey;

    private PublicKey publicKey;
    private String doctorID;

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


    public HashedRecord addTest(SecretKey key, LabTest newRec, IvParameterSpec ivParameterSpec) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException {

//        Record [] records = {new Record("Name", 18)};
//        Block newBlock =new Block(records, 0);

        String algorithm = "AES/CBC/PKCS5Padding";
        SealedObject sealedObject = encryptObject(
                algorithm, newRec, key, ivParameterSpec);
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
        HashedRecord hashedRecord = new HashedRecord(hash, encryptedBlockBytes, ivParameterSpec);



        return hashedRecord;
    }


    public HashedRecord addVisit(SecretKey key, Visit newRec , IvParameterSpec ivParameterSpec) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException {

//        Record [] records = {new Record("Name", 18)};
//        Block newBlock =new Block(records, 0);

        String algorithm = "AES/CBC/PKCS5Padding";
        SealedObject sealedObject = encryptObject(
                algorithm, newRec, key, ivParameterSpec);
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
        HashedRecord hashedRecord = new HashedRecord(hash, encryptedBlockBytes, ivParameterSpec);



        return hashedRecord;
    }


    public HashedRecord addRecord(SecretKey key, Record newRec, IvParameterSpec ivParameterSpec) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        newRec = new Record("Name", 18);
//        Record [] records = {new Record("Name", 18)};
//        Block newBlock =new Block(records, 0);

        String algorithm = "AES/CBC/PKCS5Padding";
        SealedObject sealedObject = encryptObject(
                algorithm, newRec, key, ivParameterSpec);
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
        HashedRecord hashedRecord = new HashedRecord(hash, encryptedBlockBytes, ivParameterSpec);



        return hashedRecord;
    }

    public void readVisit(SecretKey key, byte[] record, IvParameterSpec ivParameterSpec) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
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
                    algorithm, x, key, ivParameterSpec);
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

    public void readTest(SecretKey key, byte[] record, IvParameterSpec ivParameterSpec) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
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
                    algorithm, x, key, ivParameterSpec);
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


    public void readRecord(SecretKey key, byte[] record, IvParameterSpec ivParameterSpec) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
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
                    algorithm, x, key, ivParameterSpec);
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
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(3072);
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
