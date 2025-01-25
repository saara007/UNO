import java.util.Stack;

public class NumberCard extends Card{
    int number;
    private static final Stack<NumberCard> cards = new Stack<>();
    NumberCard(int number, String color) {
        super(color);
        this.number = number;

    }

    @Override
    boolean isPlayable(Card topDiscardPile, String colorToPlay) {
        if(colorToPlay.equals(color)) return true;
        else if (topDiscardPile instanceof NumberCard) {
            return ((NumberCard) topDiscardPile).number == number;
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

    public static Stack<NumberCard> getCards() {
        // Blue Number Cards
        for(int i = 0; i <= 9; i++){
            if (i > 0) {
                // All NumberCards from 1 to 9 are twice in quantity
                cards.push(new NumberCard(i, "Blue"));
                cards.push(new NumberCard(i, "Blue"));
            } else cards.push(new NumberCard(i, "Blue"));
        }

        // Green Number Cards
        for(int i = 0; i <= 9; i++){
            if (i > 0) {
                // All NumberCards from 1 to 9 are twice in quantity
                cards.push(new NumberCard(i, "Green"));
                cards.push(new NumberCard(i, "Green"));
            } else cards.push(new NumberCard(i, "Green"));
        }


        // Red Number Cards
        for(int i = 0; i <= 9; i++){
            if (i > 0) {
                // All NumberCards from 1 to 9 are twice in quantity
                cards.push(new NumberCard(i, "Red"));
                cards.push(new NumberCard(i, "Red"));
            } else cards.push(new NumberCard(i, "Red"));
        }


        // Yellow Number Cards
        for(int i = 0; i <= 9; i++){
            if (i > 0) {
                // All NumberCards from 1 to 9 are twice in quantity
                cards.push(new NumberCard(i, "Yellow"));
                cards.push(new NumberCard(i, "Yellow"));
            } else cards.push(new NumberCard(i, "Yellow"));
        }
        return  cards;
    }
}