public class User extends Player{
    User(Player prev, Player next, String name) {
        super(prev, next, name);
    }

    @Override
    void playCard(String colorToPlay) throws InterruptedException {
        Card topDiscardPile = Play.discardPile.peek();

        System.out.println("***** SELECT CARD TO PLAY *****");
        PlayMethods.displayHand(this);
        while (true) {
            Card chosenCard;
            // User inputs valid choice.
            int choice;
            while (true) {
                try {
                    System.out.print("Choice: ");
                    choice = Main.handleChoice();
                    if (choice < 1 || choice > hand.size()) throw new RuntimeException();
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid choice!");
                }
            }

            chosenCard =  hand.elementAt(choice - 1);

            // Card is playable.
            if (chosenCard.isPlayable(topDiscardPile, colorToPlay)) {
                // Player plays wild card.
                if (chosenCard instanceof WildCard) {
                    switch (((WildCard) chosenCard).type) {
                        case "Wild" -> {
                            System.out.printf("%s played a Wild Card.\n", this.name);
                            Thread.sleep(800);
                            WildCard.playWild(this);
                        }
                        case "Wild Draw 4" -> {
                            boolean playSuccess = WildCard.attemptPlayWildDraw4(this, colorToPlay);
                            // !!! Recursion !!! :(
                            // If playing wild draw 4 was unsuccessful, make the player play cards again.
                            if (!playSuccess) playCard(colorToPlay);
                        }
                    }
                    chosenCard.play(this);
                    break;
                    // Player plays action card.
                } else if (chosenCard instanceof ActionCard) {
                    System.out.printf("%s played the card: ", this.name);
                    Card.printCard(chosenCard);
                    Thread.sleep(800);
                    switch (((ActionCard) chosenCard).type) {
                        case "Skip" -> ActionCard.playSkip();
                        case "Reverse" -> ActionCard.playReverse();
                        case "Draw 2" -> ActionCard.playDraw2(this);
                    }
                    chosenCard.play(this);
                    break;
                } else if (chosenCard instanceof NumberCard) {
                    System.out.printf("%s played the card: ", this.name);
                    Card.printCard(chosenCard);
                    Thread.sleep(800);

                    chosenCard.play(this);
                    break;
                }
            }  else {
                // Card is unplayable
                PlayMethods.cardUnplayable();
            }
        }
    }
}