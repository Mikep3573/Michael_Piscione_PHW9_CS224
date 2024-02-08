import java.util.Scanner;
import java.util.ArrayList;

/*
This program simulates the knapsack algorithm studied in CS 224
 */
public class Main {
    // TODO: Clean up code
    // TODO: Write Comments

    /**
     * optimalSet performs post-processing of the knapsack algorithm. It returns an integer array list with all the
     * items chosen to be in the optimal set using recursion.
     * @param memoTable, the filled-in memoization table from the knapsack algorithm to perform post-processing on
     * @param items, the list of items with their respective weights and values (note that an item here is really
     *               just an index)
     * @param optimalItems, the array list that accumulates the elements of the optimal set
     * @param i, the row to be chosen in the memoization table
     * @param w, the column to be chosen in the memoization table
     * @return ArrayList, the integer array list containing the optimal set of items
     */
    static ArrayList<Integer> optimalSet(ArrayList<int []> memoTable, ArrayList<int[]> items,
                                         ArrayList<Integer> optimalItems, int i, int w) {
        // Get the weight and value of 'i'
        int weightI = 0;
        int valI = 0;

        // Make sure the 'i' is not zero to not index out of bounds of the array
        if (i != 0) {
            weightI = items.get(i - 1)[1];
            valI = items.get(i - 1)[0];
        }

        // Recurse
        // Return the items if we have searched through all values of 'i'
        if (i == 0) {
            return optimalItems;
        }
        // If the weight of the current 'i' cannot be held in the knapsack with the other chosen weights,
        // recurse to a lower 'i'
        else if (weightI > w) {
            return optimalSet(memoTable, items, optimalItems, i - 1, w);
        }
        // Use the relation from the algorithm to decide which path to choose
        else if ((valI + memoTable.get(w - weightI)[i]) > memoTable.get(w)[i - 1]) {
            optimalItems.add(i);
            return optimalSet(memoTable, items, optimalItems,i - 1, w - weightI);
        }
        // Recurse to a lower 'i' if the above check returns false
        else {
            return optimalSet(memoTable, items, optimalItems,i - 1, w);
        }
    }

    /**
     * knapSackAlg is a function that implements the knapsack algorithm studied in CS 224. This function takes a total
     * maximum weight of the knapsack and a list of items and their weights and values. It prints the optimal set of
     * items chosen and returns the maximum total value of the optimal set using the given information.
     * @param knapWeight, the total maximum weight that can be held by the 'knapsack'
     * @param items, the list of items and their values and weights
     * @return int, the integer value of the maximum total value of the optimal set
     */
    static int knapSackAlg(int knapWeight, ArrayList<int[]> items) {
        // Initialize the memoization table
        ArrayList<int []> memoTable = new ArrayList<>();

        // Fill the table with zeroes -> Note that this is done instead of just the first row being zeroes because java
        // arrays can't be expanded once they're created
        for (int w = 0; w <= knapWeight; w++) {
            memoTable.add(new int[items.size() + 1]);
        }

        // Output the formatted first row of the memoization table to the console
        // Print the header
        System.out.println("==========================================================");
        System.out.println("ROWS");
        System.out.println("==========================================================");

        // Print the items
        for (int j = 0; j < 1; j++) {
            for (int w = 0; w <= knapWeight; w++) {
                System.out.format("%-3s", memoTable.get(w)[j]);
            }
            System.out.print("\n");
        }

        // Fill in the rest of the table
        for (int i = 1; i <= items.size(); i++) {
            for (int w = 0; w <= knapWeight; w++) {
                // Get i's weight
                int weightI = items.get(i - 1)[1];
                // Fill in the table using the recursion relation given by the optimal substructure
                if (weightI > w) {
                    memoTable.get(w)[i] = memoTable.get(w)[i - 1];
                }
                else {
                    memoTable.get(w)[i] = Math.max(memoTable.get(w)[i - 1],
                            items.get(i - 1)[0] + memoTable.get(w - weightI)[i - 1]);
                }
            }

            // Print formatted output of the memoization table each time a new row is filled in
            System.out.println("==========================================================");
            for (int j = 0; j <= i; j++) {
                for (int w = 0; w <= knapWeight; w++) {
                    System.out.format("%-3s", memoTable.get(w)[j]);
                }
                System.out.print("\n");
            }
        }

        // Create an array list that will store the optimal set of items
        ArrayList<Integer> optimalItems = new ArrayList<>();

        // Find the optimal set of items using post-processing
        optimalItems = optimalSet(memoTable, items, optimalItems, items.size(), knapWeight);

        // Print formatted output of the optimal set of items
        String optString = "OPTIMAL SET OF ITEMS: ";
        for (Integer optimalItem : optimalItems) {
            optString += optimalItem + " ";
        }
        System.out.println("==========================================================");
        System.out.println(optString);

        // Return the maximum total value of the optimal set
        return memoTable.get(knapWeight)[items.size()];
    }
    public static void main(String[] args) {
        // Create a scanner object for user input
        Scanner inputScan = new Scanner(System.in);

        // Prompt for user input for the total carrying capacity of the knapsack
        System.out.print("What is the total carrying capacity of the 'knapsack': ");

        // Get user input for the total carrying capacity of the knapsack
        int knapWeight = Integer.parseInt(inputScan.next());

        // Create a 2-D array to store information about each item
        // Note that an item is really just an index to a certain array
        // Note that the 0 index in any given array in the array list is the value of the item
        // while the 1 index is the weight
        // Note for grader; if a new set of items is to be tested, use the above information and change the following
        // arrays/array list as needed
        ArrayList<int[]> items = new ArrayList<>();
        items.add(new int[] {1, 1});
        items.add(new int[] {6, 2});
        items.add(new int[] {18, 5});
        items.add(new int[] {22, 6});
        items.add(new int[] {28, 7});

        // Print formatted output for the resulting maximum total value of the included items
        int maxVal = knapSackAlg(knapWeight, items);
        System.out.println("==========================================================");
        System.out.println("MAXIMUM TOTAL VALUE: " + maxVal);
    }
}