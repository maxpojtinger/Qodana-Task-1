Test task 1

Alex heard that the list of names in publications is sorted in lexicographical order. Alex is very vain, he tries to figure the alphabet in which his name will appear first in the publication. Help Alex to write a program that would calculate an alphabet in which a given list of names would be lexicographically sorted or determine that this is impossible.

Programs are accepted in Java, Kotlin, Groovy. As an answer, please send a link to Github with the solution.

Input:

Sent to standard input. The first line contains an integer n (1 ≤ n ≤ 100), the number of names.

Each of the next n lines contains one word name%i , denoting the i-th name. Each name contains only lowercase letters of the Latin alphabet, no more than 100 characters. All names are different.

Output:

Sent to standard output.
If there is an order of letters such that the names in the given list appear in lexicographical order, print any such order as a permutation of the characters 'a'–'z' (in other words, print the first letter of the modified alphabet first, then the second, and so on) .

Otherwise, print the single word "Impossible" (without quotes).
