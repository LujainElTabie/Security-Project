import java.util.ArrayList;
import java.util.Arrays;

public class Blockchain {
    public static void main(String[] args) throws Exception {
        String statement1  ="My world is perfect.";
        int hashValue1 = statement1.hashCode();
        System.out.println("Statement 1 = "+ statement1+" and it's hash is  "+ hashValue1);
        String [] list1 = {"alex", "becky", "cyril"};
        String [] list2 = {"alex", "becky", "Cyril"};
        int hash1= Arrays.hashCode(list1);
        int hash2= Arrays.hashCode(list2);
        System.out.println("List 1 hash is  "+ hash1);
        System.out.println("List 2 hash is  "+ hash2);

        ArrayList<Block> blockChain= new ArrayList<Block>();
        String [] initValues ={"Shad has $700", "Miguel has $550"};
        Block firstBlock = new Block(initValues,0);
        blockChain.add(firstBlock);
        System.out.println("First block is  "+ firstBlock.toString());
        System.out.println("Blockchain is  "+ blockChain.toString());
        String [] values ={"Shad gives Miguel $200"};
        Block secondBlock = new Block(values,firstBlock.getBlockHash());
        blockChain.add(secondBlock);
        System.out.println("Second block is  "+ secondBlock.toString());
        System.out.println("Blockchain is  "+ blockChain.toString());
    }
}
