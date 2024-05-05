package Task1;

import java.util.*;

public class Task1 {
    private Map<Character, Set<Character>> dependencies;
    private List<String> sortedNames;
    private final char[] ALPHABET;
    private boolean impossible;

    public List<String> getSortedNames() {
        return sortedNames;
    }

    /**
     * A setter method for our sorted names variable.
     * Because the variables "dependencies" and "impossible" are dependent on the list of sorted names,
     * we need to calculate their values again.
     *
     * @param sortedNames - the list of sorted names we need to work with
     */
    public void setSortedNames(List<String> sortedNames) {
        newSettings(sortedNames);
    }

    public Map<Character, Set<Character>> getDependencies() {
        return dependencies;
    }

    public boolean isImpossible() {
        return impossible;
    }

    /**
     * The method newSettings is responsible for setting our variables "sortedNames", "impossible" and "dependencies".
     * Because the variables "impossible" and "dependencies" depend on the list of sorted names, we need a method that
     * sets all of them accordingly.
     *
     * @param sortedNames - a list of sorted names we need to work with
     */
    private void newSettings(List<String> sortedNames){
        this.sortedNames = sortedNames;
        impossible = false;
        dependencies = new HashMap<>();
        for(char c : ALPHABET){
            dependencies.put(c, new HashSet<>());
        }
        //Every letter of the English alphabet needs an entry in our dependency hash map.

        for(int i = 0; i < sortedNames.size() - 1; i++){
            if(!dependencySearch(sortedNames.get(i), sortedNames.get(i+1))){
                impossible = true;
            }
            /*
            Here we are calculating letter dependencies (for example: q needs to come before m and m needs to come before c)
            We need to come up with a permutation of the English alphabet for which we can say our list is lexicographically sorted
            (if we use this permutation for sorting our list and not the abc)
            dependencySearch is responsible for that
             */
        }
        if(hasCycle()){
            impossible = true;
        }
        /*Here we are checking, if there are any contradictory dependencies in our hash map
        Two dependencies are contradictory, if they form a cycle
        Let's use the notation (a1, a2) for saying the letter a1 needs to come before the letter a2
        (this means in any permutation of the English alphabet for which our list is lexicographically sorted,
        the letter a1 is placed further to the start of the permutation than the letter a2)
        For example: (a,b), (b,a) are to contradictory dependencies
                     (a,b), (b,c), (c,a) are to contradictory dependencies

        Let us visualize the dependencies of our desired permutation as a graph,
        in which the nodes are all the common letters of the English alphabet and the vertices are the dependencies
        For example: h -> n means the letter h needs to come before the letter n.
        This graph isn't allowed to have any cycles, otherwise we can't solve our problem.
         */
    }

    public Task1(List<String> sortedNames){
        ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        newSettings(sortedNames);
    }
    //basic constructor initializing the normal English alphabet

    /**
     * The method is looking for a character dependency between two strings, where the first string always has the higher priority.
     * It's recursively looking for the first letter, where the two words differ from one another. This pair of letters is the dependency.
     * The moment the first word is empty, we know that the first word has to be a prefix of the second word. This gives us no dependency,
     * since both words are the exact same until a certain point.
     * The moment the second word is empty (and the first one isn't), we know that the second word has to be a prefix of the first word.
     * This also gives us no dependency, but because in a lexicographically sorted list prefixes have priority over their successor, we can
     * tell that the given list can't be lexicographically sorted, regardless of whether we can find an alphabet, for which the list of
     * names is correctly sorted or not. That means we can stop right here.
     * The moment both words are empty, we also can't draw conclusions on any dependency.
     *
     * @param wordOne the word placed in front of "wordTwo" in our list "sortedNames"
     * @param wordTwo the word placed behind "wordOne" in our list "sortedNames"
     * @return Returns true, if it's still possible to create a valid permutation, though this verdict is not final.
     *         Returns false, if it's impossible. This can only be, because "wordTwo" is a prefix of "wordOne".
     */
    public boolean dependencySearch(String wordOne, String wordTwo){
        if (wordOne.isEmpty()){
            return true;
        }
        if (wordTwo.isEmpty()){
            return false;
        }
        if(wordOne.charAt(0) == wordTwo.charAt(0)){
            return dependencySearch(wordOne.substring(1), wordTwo.substring(1));
        }
        dependencies.get(wordOne.charAt(0)).add(wordTwo.charAt(0));
        return true;
    }

    /**
     * This method checks, whether there is a contradictory dependency in our map "dependencies" or not.
     *
     * @return Returns true, if we were able to find a contradictory dependency. Returns false, if we weren't.
     */
    public boolean hasCycle(){
        Set<Character> visited = new HashSet<>();
        Set<Character> currentPath = new HashSet<>();

        for(char node : dependencies.keySet()){
            if(!visited.contains(node) && hasCycleDFS(node, visited, currentPath)){
                return true;
            }
        }
        return false;
    }

    /**
     * This method implements a version of depth-first traversal.
     * In order to find a contradictory dependency, we're modelling a graph after our map "dependencies".
     * The nodes are all 26 letters of the English alphabet, the vertices are the dependencies our permutation needs to pay attention to.
     * The method hasCycle is a starting point of our depth-first traversal of the graph.
     * There we are calling the method "hasCycleDFS" on every node in our keySet, except we already visited the node in our graph.
     * We can be sure a cycle is found, when the neighbour of our current node is in the set of nodes in our current path.
     *
     * @param node The node we are currently working with, it's like the end point of our current path.
     * @param visited A set of nodes we already visited.
     * @param currentPath A set of nodes on our current path through the graph. It's used to track our steps.
     * @return Returns true, if we found a cycle, else false.
     */
    private boolean hasCycleDFS(char node, Set<Character> visited, Set<Character> currentPath){
        visited.add(node);
        currentPath.add(node);
        if(dependencies.containsKey(node)){
            for(char neighbor : dependencies.get(node)){
                if(!visited.contains(neighbor) && hasCycleDFS(neighbor, visited, currentPath)){
                    return true;
                } else if (currentPath.contains(neighbor)){
                    return true; //Cycle found
                }
            }
        }
        currentPath.remove(node); //we're leaving the current node
        return false;
    }

    /**
     * This method is responsible for finding a permutation of the English alphabet,
     * for which our sorted list of names is lexicographically sorted.
     *
     * @return Returns a valid list of characters, which represents our permutation.
     *         If it's impossible to find a valid permutation, it returns null.
     */
    public List<Character> findSequence(){
        if(impossible){
            return null;
        }
        List<Character> result = new ArrayList<>();
        Set<Character> visited = new HashSet<>();
        Set<Character> currentPath = new HashSet<>();
        for(char node : dependencies.keySet()) {
            if(!visited.contains(node)){
                findSequenceDFS(node, visited, currentPath, result);
            }
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * This method implements a version of the depth-first traversal.
     * Again, we are modelling a graph after our map "dependencies".
     * The nodes are all 26 letters of the English alphabet, the vertices are the dependencies our permutation needs to pay attention to.
     * The method findSequence is a starting point of our depth-first traversal.
     * There we are calling the method "findSequenceDFS" on every node in our keySet, except we already visited the node in our graph.
     * After we're finished, we need to reverse our list, because we're only adding letters to our result list,
     * when there aren't any vertices left to take. This means we found an end in our graph.
     *
     * @param node The node we are currently working with, it's like the end point of our current path.
     * @param visited A set of nodes we already visited.
     * @param currentPath A set of nodes on our current path through the graph. It's used to track our steps.
     * @param result Our permutation (when we're done).
     */
    private void findSequenceDFS(char node, Set<Character> visited, Set<Character> currentPath, List<Character> result){
        if(!visited.contains(node)){
            //this if-clause may be redundant, because of the if-clause in the findSequence method
            currentPath.add(node);
            if(dependencies.containsKey(node)){
                for(char neighbor : dependencies.get(node)){
                    findSequenceDFS(neighbor, visited, currentPath, result);
                }
            }
            currentPath.remove(node);
            visited.add(node);
            result.add(node);
        }
    }
    /*
    Notes: 1) It is possible to combine the findSequenceDFS and hasCycleDFS methods, because the traversal is the exact same.
           2) "visited" and "result" contain the exact same elements, and they are also always being added in the same sequence at the same time.
              We could easily remove visited and only work with result, but I chose using "visited" for tracking steps and "result" for building our permutation,
              because visited is a set of 26 characters max. It's contains method will always be O(1). Using result for tracking steps is simply slower.
     */

}
