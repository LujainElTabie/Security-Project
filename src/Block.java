import java.util.Arrays;

public class Block {


    private String [] transactions;
    private int blockHash;
    private int prevBlockHash;


    public Block(String [] transactions, int prevBlockHash){
        super();
        this.transactions=transactions;
        this.prevBlockHash=prevBlockHash;
        this.blockHash = Arrays.hashCode(new int []{ Arrays.hashCode(transactions), this.prevBlockHash});
    }

    /**
     * @return String [] return the transactions
     */
    public String [] getTransactions() {
        return transactions;
    }

    /**
     * @param transactions the transactions to set
     */
    public void setTransactions(String [] transactions) {
        this.transactions = transactions;
    }

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
                "transactions=" + Arrays.toString(transactions) +
                ", blockHash=" + blockHash +
                ", prevBlockHash=" + prevBlockHash +
                '}';
    }

}
