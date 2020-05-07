/**
 * This cheating hangman game employs two different strategies to ensure
 * that the player will not beat the system, alternating each round.
 * The first strategy determines the largest group of words that don't
 * contain the player's guess and return that, and the second method
 * averages the relative rarity of remaining letters and returns the
 * rarest group. Incidentally, these frequently end up leading to the
 * same result despite employing different logic.
 *
 * @author Saxon McIntosh
 * @version March 16, 2020
 */

import java.util.*;
import java.io.*;

public class Hangman {
    // 'visual' is the visual representation of the correct guesses.
    public static String visual;
    public static int guesses;
    
    public static void main(String args[])
    throws FileNotFoundException {
	Scanner sc = new Scanner(new File("dictionary.txt"));
	// lengths is required so that the user can't pick a word length
	// that doesn't exist.
	ArrayList<Integer> lengths = new ArrayList<>();
	HashSet<String> dictionary = new HashSet<>();
	String current;
	int cLength;
	while (sc.hasNextLine()) {
	    current = sc.nextLine();
	    // I'm choosing not to include words that have symbols in
	    // them (because that's just mean).
	    if (!current.contains("'") && !current.contains("-")) {
		// The proper nowns in the list will create problems if
		// they are not taken care of.
		dictionary.add(current.toLowerCase());
		cLength = current.length();
		if (!lengths.contains(cLength)) {
		    lengths.add(cLength);
		}
	    }
	}
	
	// I looked up the rarity of letters in the English language to
	// build this string.
	String hpla = "etaoinshrdlcumwfgypbvkjxqz";
	HashMap<Character, Integer> weights = new HashMap<>();
	for (int i = 0; i < hpla.length(); i++) {
	    weights.put(hpla.charAt(i), i + 1);
	}

	do {
	    HashSet<String> dict = new HashSet<String>();
	    System.out.print("Please select the number of letters you would like the guess word to contain: ");
	    sc = new Scanner(System.in);
	    int wordLength = lengthPicker(sc, lengths);
	    /* Originally I wanted to remove the set items that didn't
	      match as I iterated through it, but I couldn't modify
	      them concurrently. My best approach was to just build a
	      newly culled set and clear the old one to free up memory.
	    */
	    Iterator<String> iter = dictionary.iterator();
	    while (iter.hasNext()) {
		current = iter.next();
		if (current.length() == wordLength) {
		    dict.add(current);
		}
	    }

	    System.out.print("Type the number of Mulligans you'd like: ");
	    guesses = guessPicker(sc);
	    if (guesses == 0) {
		System.out.println("Wow, hard-mode huh? Good luck!");
	    }
	
	    visual = "";
	    for (int i = 0; i < wordLength; i++) {
		visual = visual.concat("_ ");
	    }
        
	    ArrayList<String> pastMoves = new ArrayList<>();
	    boolean notFirst = false;
	    // turn is implemented to alternate between the two
	    // different cheating strategies.
	    int turn = 0;
	    do {
		System.out.println("\t\t\t" + visual);
		// This flag lets me tell the user their past moves,
		// without doing so needlessly on the first pass.
		if (notFirst == true) {
		    System.out.println("Past moves: " + pastMoves);
		    System.out.println("Remaining guesses: " + guesses);
		}
		System.out.print("Pick a letter: ");
	    
		String userIn = validater(sc, pastMoves);
		pastMoves.add(userIn);

		HashMap<String, String> flagMap = cartographer(dict,
							       userIn);
		dict = choiceMaker(userIn, flagMap, weights, turn);
	    
		notFirst = true;
		turn++;
	    } while (!isFinished());
	    List<String> dictList = new ArrayList<>(dict);
	    String answer;
	    if (guesses == -1) {
		int rand = new Random().nextInt(dictList.size());
		answer = dictList.get(rand);
		
		System.out.println("Whomp whomp, the answer was " + answer + "!");
		System.out.println("Wanna play again?");
	    } else {
		answer = dictList.get(0);
		System.out.println("Wow, I'm shocked that you guessed " + answer + "!");
		System.out.println("Wanna play again?");
	    }
	    System.out.print("Enter 'y' for yes, or 'n' for no: ");
	} while (reduxer(sc));
    }

    /**
     * One of many input validation mechanisms to clean user input, this
     * is for choosing the length of the word.
     *
     * @param sc the scanner object
     * @param lengths the array that denotes the available lengths
     *
     * @return the length that the user ultimately picks
     */
    public static int lengthPicker(Scanner sc,
				   ArrayList<Integer> lengths) {
	int userInt = 0;
	// flag contributes to control flow when the user inputs a
	// non-integer.
	boolean flag = false;
	do {
	    try {
		userInt = sc.nextInt();
	    } catch (InputMismatchException e) {
		System.out.print("That's not even a number, you little goofball! Try again: ");
		flag = true;
		sc.nextLine();
	    }
	    if (!lengths.contains(userInt) && flag != true) {
		System.out.print("Sorry, the dictionary doesn't contain any words that length. Try again: ");
		userInt = 0;
	    }
	    flag = false;
	} while (userInt == 0 || !lengths.contains(userInt));
	sc.nextLine();
	/* I'm not actually sure if this is what I'm supposed to do
	   here, but I had to keep clearing out the scanner so it didn't
	   retain old input.
	*/
	return userInt;
    }

    /**
     * The next input validation method, this time to decide how many
     * chances the user will have to 'fail' at guessing a letter.
     *
     * @param sc the scanner object
     *
     * @return the selected number of guesses
     */
    public static int guessPicker(Scanner sc) {
	int userInt = -1;
	boolean flag = false;
	do {
	    try {
		userInt = sc.nextInt();
	    } catch (InputMismatchException e) {
		System.out.print("That's not even a number, you little goofball! Try again: ");
		flag = true;
		sc.nextLine();
	    }
	    if (flag == false && (userInt < 0 || userInt > 10)) {
		System.out.println("Well that doesn't seem like a very reasonable ");
		System.out.print("number of guesses, does it? Try again: ");
		userInt = -1;
	    }
	} while (userInt == -1);
	sc.nextLine();
	return userInt;
    }

    /**
     * validater is for the actual character guesses.
     *
     * @param sc the scanner object
     * @param pastMoves an list of all the previous moves
     */
    public static String validater(Scanner sc,
				   ArrayList<String> pastMoves) {
	String alph = "abcdefghijklmnopqrstuvwxyz";
	String userIn = sc.nextLine().toLowerCase();
	/* If the user input is longer than one character, is a choice
	   that's already been made, or contains a non-alphabetical
	   character the user must try again.
	*/
	while (userIn.length() > 1 || pastMoves.contains(userIn) ||
	       !alph.contains(userIn.substring(0, 1))) {
	    System.out.print("Sorry, but that input isn't valid. Try again! > ");
	    userIn = sc.nextLine().toLowerCase();
	}
	return userIn;
    }

    /**
     * I'm actually proud of this idea - cartographer makes a map (heh)
     * whose key/value pairs consist of a word and a string representing
     * where the chosen character is in the string, if it is there. For
     * instance, if the letter was a and the word was facade, the value
     * would look like this: 121211.
     */
    public static HashMap<String, String> cartographer(HashSet<String> dict, String guess) {
	HashMap<String, String> flagMap = new HashMap<>();
	Iterator<String> iter = dict.iterator();
	String current = "";
	String flags = "";
	while (iter.hasNext()) {
	    current = iter.next();
	    for (int i = 0; i < current.length(); i++) {
		if (current.charAt(i) == guess.charAt(0)) {
		    // I picked 2 and 1 instead of the more traditional
		    // 1 and 0 to prevent confusion later on.
		    flags = flags.concat("2");
		} else {
		    flags = flags.concat("1");
		}
	    }
	    flagMap.put(current, flags);
	    flags = "";
	}
	return flagMap;
    }

    /**
     * The heart of the game logic, choiceMaker is how the game culls
     * the original dictionary down based on the user's input. On even
     * turns the word with the greatest volume by type is returned. On
     * odd moves words with the greatest average letter rarity are
     * returned. It also updates the visual.
     *
     * @param userIn the user's choice of character
     * @param flagMap a mapping of the word 'types'
     * @param weights the rarity of each letter relative to each other
     * @param turn the number of turns that have passed thus far
     *
     * @return the newly culled dictionary
     */
    public static HashSet<String> choiceMaker(String userIn, HashMap<String, String> flagMap, HashMap<Character, Integer> weights, int turn) {
	// The new dictionary will be built into dict using the values
	// from flagMap.
	HashSet<String> dict = new HashSet<>();
	/* rarity will eventually contain a map of the rarity of each
	   word choice based on how infrequently the letter in them are
	   used.
	*/
	HashMap<String, Integer> rarity = new HashMap<>();
	// flagCount contains a map of the number of each 'type' of word
	// there is based on userIn placement in them.
	HashMap<String, Integer> flagCount = new HashMap<>();
	// rareMap is the map of the average rarity of each of the
	// aforementioned types.
	HashMap<String, Integer> rareMap = new HashMap<>();
	Set<String> flagKeys = flagMap.keySet();

	Iterator<String> iter = flagKeys.iterator();
	String current;
	String word;
	int weight = 0;
	while (iter.hasNext()) {
	    word = iter.next();
	    current = flagMap.get(word);

	    // This loop is responsible for building out the rarity map,
	    // grabbing the value of each letter that is not userIn.
	    for (int i = 0; i < word.length(); i++) {
		if (word.charAt(i) != userIn.charAt(0)) {
		    weight += weights.get(word.charAt(i));
		}
	    }
	    rarity.put(word, weight);
	    weight = 0;
	    
	    if (!flagCount.containsKey(current)) {
		flagCount.put(current, 1);
		rareMap.put(current, rarity.get(word));
	    } else {
		int oldCount = flagCount.get(current);
		int oldRare = rareMap.get(current);
		flagCount.replace(current, oldCount + 1);
		rareMap.replace(current, oldRare + rarity.get(word));
	    }
	}

	// The section assigns the type with the greatest number of
	// values to bigKey.
	String bigKey = "";
	int bigVal = 0;
	Set<String> countKeys = flagCount.keySet();
	iter = countKeys.iterator();
	while (iter.hasNext()) {
	    current = iter.next();
	    if (bigVal < flagCount.get(current)) {
		bigVal = flagCount.get(current);
		bigKey = current;
	    }
	}

	// On odd turns, this block is used instead to select bigKey.
	if (turn % 2 == 1) {
	    bigVal = 0;
	    Set<String> rareKeys = rareMap.keySet();
	    iter = rareKeys.iterator();
	    while (iter.hasNext()) {
		current = iter.next();
		// This replaces the values in rareMap with the average
		// rarity per type instead of the total.
		rareMap.replace(current, rareMap.get(current) / flagCount.get(current));
		if (bigVal < rareMap.get(current)) {
		    bigVal = rareMap.get(current);
		    bigKey = current;
		}
	    }
	}

	// The dicionary is populated with words of the type denoted by
	// bigKey.
	iter = flagKeys.iterator();
	while (iter.hasNext()) {
	    word = iter.next();
	    if (flagMap.get(word).equals(bigKey)) {
		dict.add(word);
	    }
	}

	String newVisual = "";
	for (int i = 0; i < bigKey.length(); i++) {
	    if (bigKey.charAt(i) == '2') {
		newVisual = newVisual.concat(userIn + " ");
	    } else {
		newVisual = newVisual.concat(visual.charAt(i * 2) + " ");
	    }
	}
	if (visual.equals(newVisual)) {
	    System.out.println("Nope, sorry!");
	    guesses--;
	}
	visual = newVisual;

	return dict;
    }

    /**
     * This helper method is used to tell if the game is over.
     *
     * @return a boolean value of whether the game is finished
     */
    public static boolean isFinished() {
	// If the player has run out of guesses:
	if (guesses < 0) {
	    return true;
	}
	// If the visual still contains any underscores:
	for (int i = 0; i < visual.length(); i++) {
	    if (visual.charAt(i) == '_') {
		return false;
	    }
	}
	return true;
    }

    /**
     * One last input validation method in case the player would like to
     * play more than one round.
     *
     * @param sc the scanner object
     * 
     * @return a boolean of the player's desire to resume playing
     */
    public static boolean reduxer(Scanner sc) {
	String redux = "";
	do {
	    redux = sc.nextLine().toLowerCase();
	    if (!redux.equals("y") && !redux.equals("n")) {
		System.out.print("It's just one letter, bud. Try again: ");
	    }
	} while(!redux.equals("y") && !redux.equals("n"));
	if (redux.equals("y")) {
	    return true;
	}
	return false;
    }
}
