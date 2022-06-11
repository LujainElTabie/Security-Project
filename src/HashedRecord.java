import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;
import java.io.Serializable;

public class HashedRecord implements Serializable {
    private byte[] hash;

    private SealedObject record;


//    public IvParameterSpec getIvParameterSpec() {
//        return ivParameterSpec;
//    }
//
//    private IvParameterSpec ivParameterSpec;

    public HashedRecord(byte[] hash, SealedObject record, IvParameterSpec ivParameterSpec) {
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

    public byte[] getHash() {
        return hash;
    }

    public SealedObject getRecord() {
        return record;
    }

}
