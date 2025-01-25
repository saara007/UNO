public abstract class Card {
    String color;

    Card(String color) {
        this.color = color;
    }

    // Returns true is card is currently playable.
    abstract boolean isPlayable(Card topDiscardPile, String colorToPlay);
    abstract void play(Player player);

    static void printCard(Card card) {
        if (card instanceof NumberCard) {
            System.out.printf("(%s) %d\n", card.color, ((NumberCard) card).number);
        } else if (card instanceof ActionCard) {
            System.out.printf("(%s) %s\n", card.color, ((ActionCard) card).type);
        } else if (card instanceof WildCard) {
            System.out.printf("%s\n", ((WildCard) card).type);
        }
    }
}