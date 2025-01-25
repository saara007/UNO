import java.util.Stack;

public abstract class Player {
    String name;
    boolean hasDrawn1Card = false;

    Stack<Card> hand;
    Player prev;
    Player next;

    Player(Player prev, Player next, String name) {
        this.prev = prev;
        this.next = next;
        this.name = name;
        this.hand = new Stack<>();
    }

    abstract void playCard(String colorToPlay) throws InterruptedException;
}