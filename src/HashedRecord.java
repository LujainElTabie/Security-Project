import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;
import java.io.Serializable;
import java.util.Arrays;

public class HashedRecord implements Serializable {
    private int hash;

    private byte[] record;


//    public IvParameterSpec getIvParameterSpec() {
//        return ivParameterSpec;
//    }
//
//    private IvParameterSpec ivParameterSpec;

    public HashedRecord(int hash, byte[] record, IvParameterSpec ivParameterSpec) {
        this.hash = hash;
        this.record = record;
//        this.encrypted = encrypted;
       // this.ivParameterSpec=ivParameterSpec;
    }

    @Override
    public String toString() {
        return "HashedRecord{" +
                "hash=" + hash +
                ", record=" + record +
         //       ", ivParameterSpec=" + ivParameterSpec +
                '}'+'\n';
    }

    public int getHash() {
        return hash;
    }

    public byte[] getRecord() {
        return record;
    }

}
