import java.util.Collections;
import java.util.Stack;

public class Deck {
    private static final Stack<Card> deck = new Stack<>();
    static Stack<Card> getDeck() {
        deck.addAll(NumberCard.getCards());
        deck.addAll(ActionCard.getCards());
        deck.addAll(WildCard.getCards());

        return deck;
    }

    static void shuffle(Stack<Card> deck) {
        Collections.shuffle(deck);
    }
}