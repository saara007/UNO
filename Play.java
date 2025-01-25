import java.util.Scanner;
import java.util.Stack;

public class Play {
    private static boolean turnReversed = false;
    private static int botCount;
    private static Player winner;
    private static User user;
    static Stack<Card> discardPile = new Stack<>();
    static Stack<Card> deck = Deck.getDeck();
    private static String colorToPlay;
    private static boolean skip;
    static void startGame() throws InterruptedException {
        System.out.println("\t\t*********** Let's Play UNO! ***********");

        if(botCount == 0) {
            System.out.println("How many Bots would you like to play with? [min:1, max: 9]");
            while (true) {
                try {
                    botCount = Main.handleChoice();
                    if (botCount < 1 || botCount > 9) throw new RuntimeException();
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid choice!");
                }
            }
            // Generate Bots.
            Bot[] bots = Bot.generateBots(botCount);
            Bot.printBotNames(bots);

            // Create and Link user to bots
            Scanner scan = new Scanner(System.in);
            System.out.print("Enter your username: ");
            String username = scan.nextLine();
            user = new User(bots[botCount - 1], bots[0], username);
            bots[0].prev = user;
            bots[botCount - 1].next = user;
        }

        System.out.println("Shuffling Deck...");
        Thread.sleep(1500);
        Deck.shuffle(deck);

        // Deal cards to players
        PlayMethods.dealCards(user);
        Thread.sleep(2000);

        // Create Discard Pile and set the first color to be played.
        PlayMethods.startDiscardPile(deck, discardPile);
        setColorToPlay(discardPile.peek().color);

        Player currentPlayer = user;
        while (winner == null) {
            skip = false;
            Card topDiscardPile = discardPile.peek();

            // Getting current player's playable cards.
            int playableCards = PlayMethods.scanPlayableCards(currentPlayer, topDiscardPile, colorToPlay);

            System.out.printf("\nCurrent Player in turn: %s\n", currentPlayer.name);
            Thread.sleep(1500);
            PlayMethods.printDeckStats();
            Thread.sleep(2500);

            // Check first if current player doesn't have any playable cards.
            if (playableCards == 0) {
                System.out.printf("%s currently doesn't have any playable cards. Draws 1 card from the Draw Pile...\n",
                        currentPlayer.name);
                Thread.sleep(3500);

                PlayMethods.drawCards(1, currentPlayer);
                Card drawnCard = currentPlayer.hand.peek();
                currentPlayer.hasDrawn1Card = true;

                System.out.print("\nCard drawn: ");
                Card.printCard(drawnCard);
                Thread.sleep(1000);

                if(drawnCard.isPlayable(topDiscardPile, colorToPlay)) {
                    currentPlayer.playCard(colorToPlay);
                } else {
                    System.out.println("( Card drawn is not playable. Skipping turn... )");
                    Thread.sleep(1500);
                }
            } else {
                // Player in turn plays a card.
                currentPlayer.playCard(colorToPlay);
            }

            PlayMethods.checkWinner(currentPlayer);


            // SET NEXT PLAYER IN TURN.
            // Reversed direction.
            if (turnReversed && botCount > 1) {
                if (skip) {
                    System.out.printf("%s was skipped.\n", currentPlayer.prev.name);
                    currentPlayer = currentPlayer.prev.prev;
                } else {
                    currentPlayer = currentPlayer.prev;
                }
                // Normal direction.
            } else {
                if (skip) {
                    System.out.printf("%s was skipped.\n", currentPlayer.next.name);
                    currentPlayer = currentPlayer.next.next;
                } else {
                    currentPlayer = currentPlayer.next;
                }
            }
            Thread.sleep(1500);

        }
        System.out.printf("""
                    
                    %s has no more cards left.
                    
                    GAME OVER!
                    WINNER: %s
                    
                    """, winner.name, winner.name);

    }

    public static void setWinner(Player winner) {
        Play.winner = winner;
    }

    public static String getColorToPlay() {
        return colorToPlay;
    }
    public static void setColorToPlay(String color) {
        colorToPlay = color;
    }
    public static void activateSkip() {
        skip = true;
    }
    public static boolean getTurnReversed() {
        return turnReversed;
    }

    public static void setTurnReversed(boolean bool) {
        turnReversed = bool;
    }

    public static int getBotCount() {
        return botCount;
    }
}