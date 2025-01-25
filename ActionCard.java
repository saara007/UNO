import java.util.Stack;

public class ActionCard extends Card{
    private static final Stack<ActionCard> cards = new Stack<>();
    String type;
    ActionCard(String color, String type) {
        super(color);
        this.type = type;
    }
    @Override
    boolean isPlayable(Card topDiscardPile, String colorToPlay) {
        if (colorToPlay.equals(color)) return true;
        else if (topDiscardPile instanceof ActionCard) {
            // has Matching Types
            return ((ActionCard) topDiscardPile).type.equals(type);
        }
        return false;
    }
    @Override
    void play(Player player) {
        // Transfer chosen card from hand to discard pile.
        Play.discardPile.push(this);
        player.hand.remove(this);

        // Set color of chosen card as color to be played next turn.
        Play.setColorToPlay(this.color);
    }
    static void playReverse() {
        if (Play.getBotCount() > 1) {
            boolean currentTurnDirection = Play.getTurnReversed();
            Play.setTurnReversed(!currentTurnDirection);
        } else {
            // Reverse cards act as a Skip card if there are only 2 players.
            playSkip();
        }
    }
    static void playSkip() {
        Play.activateSkip();
    }
    static void playDraw2(Player player) throws InterruptedException {
        Player nextPlayer;
        if (Play.getTurnReversed()) {
            nextPlayer = player.prev;
        } else nextPlayer = player.next;

        System.out.printf("%s draws 2 cards from the draw pile and loses turn.\n", nextPlayer.name);
        PlayMethods.drawCards(2, nextPlayer);
        Play.activateSkip();
        Thread.sleep(1500);
    }

    public static Stack<ActionCard> getCards(){
        String[] colors = {"Blue", "Green", "Red", "Yellow"};

        // Skip Cards
        for(String color: colors){
            cards.push(new ActionCard(color, "Skip"));
            cards.push(new ActionCard(color, "Skip"));
        }

        // Reverse Cards
        for(String color: colors){
            cards.push(new ActionCard(color, "Reverse"));
            cards.push(new ActionCard(color, "Reverse"));
        }

        // Draw2 Cards
        for(String color: colors){
            cards.push(new ActionCard(color, "Draw 2"));
            cards.push(new ActionCard(color, "Draw 2"));
        }
        return cards;
    }
}