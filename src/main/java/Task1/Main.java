package Task1;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Alex heard that the list of names in publications is sorted in lexicographical order.\nAlex is very vain, he tries to figure the alphabet in which his name will appear first in the publication.\nHelp Alex to write a program that would calculate an alphabet in which a given list of names would be lexicographically sorted or determine that this is impossible.\n");
        System.out.println("We will now scan a few input lines.\nThe first line has to contain an integer n (1 <= n <= 100), the number of names being processed.");
        Scanner scanner = new Scanner(System.in);
        try {
            int n = scanner.nextInt();
            scanner.nextLine();
            //I found out that nextInt() only consumes the integer input, not the end of line (\n)
            //That is why I placed the scanner.nextLine(); below scanner.nextInt();
            if(n < 1 || n > 100){
                System.out.println("n has to be a natural number between 1 and 100, yours is " + n);
                return;
            }
            System.out.println("Now that a valid number got scanned, we will be scanning " + n + " lines from System.in for further input:");
            System.out.println("Every line has to contain one word in a format STRING%i, denoting the i-th name.\nThe String itself should only contain 100 characters, the name in lowercase letters and the denotation.\nNo uppercase letters, no numbers, no special characters.\nAll names should be different.");
            //Note: In my case the names don't necessarily need to be different, when you look at dependencySearch in Task1
            String[] namesAsString = new String[n];
            //I'm creating this String array, because I can't simply add elements in List<String> names at random places.
            String placeholder;
            String[] parts;
            for(int i = 0; i < n; i++){
                placeholder = scanner.nextLine().trim();
                if (placeholder.isEmpty()){
                    i--;
                    continue; // Überspringt leere Zeilen
                }
                parts = placeholder.split("(?<=\\D)(?=\\d)");

                // Hat der Split funktioniert (gibt es ein Wort und eine Zahl)?
                if (parts.length >= 2) {
                    try {
                        int index = Integer.parseInt(parts[1]) - 1;
                        // Ist der Index überhaupt im Array?
                        if (index >= 0 && index < n) {
                            // Löscht alles außer Buchstaben (A-Z, a-z) und macht es klein
                            namesAsString[index] = parts[0].replaceAll("[^a-zA-Z]", "").toLowerCase();
                        } else {
                            System.out.println("Zahl zu groß/klein, überspringe: " + placeholder);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ungültige Zahl, überspringe: " + placeholder);
                    }
                } else {
                    System.out.println("Ungültiges Format, überspringe: " + placeholder);
                }
            }
            scanner.close();

            List<String> names = new ArrayList<>();
            for (String s : namesAsString) {
                if (s != null && !s.isEmpty()) {
                    names.add(s);
                }
            }
            /*
            The runtime complexity of scanning and splitting our input should be O(n*m) (n = number of input strings, m = maximum number of characters in our string)
            It may be possible to do this more efficiently, but because there is a limit of 100 names with 100 characters at most, I won't try to optimize it any further, since it won't make a practical difference.
            Doing it like this removes the necessity to sort the list of names by their placement stated in the integer part of the input strings separately.
             */
            Task1 task1 = new Task1(names);
            if(task1.isImpossible()){
                System.out.println("Impossible");
            } else {
                List<Character> alphabet = task1.findSequence();
                for (Character character : alphabet) {
                    System.out.println(character);
                }
            }

        } catch(InputMismatchException e){
            System.out.println("First input must be a natural number n with 1 <= n <= 100, yours probably wasn't even an integer");
            scanner.close();
        }
    }
}
