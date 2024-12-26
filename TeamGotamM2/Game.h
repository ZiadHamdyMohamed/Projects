#ifndef TEAM_GOTHAM_GAME_H
#define TEAM_GOTHAM_GAME_H
#include "Deck.h"
#include "Player.h"
#include "StandardCard.h"
#include "BonusCard.h"
#include "PenaltyCard.h"
#include <vector>
using namespace std;

class Game {
private:
    Deck deck;
    Player player1;
    Player player2;
    bool player1Turn;

    void handleStandardCards(Card* card1, Card* card2, Player& currentPlayer);
    void handleBonusCards(Card* card1, Card* card2, Player& currentPlayer);
    void handlePenaltyCards(Card* card1, Card* card2, Player& currentPlayer);

public:
    Game();
    Game(Player player1, Player player2);
    ~Game();

    void initializeGame();
    void playTurn();
    void displayScores() const;
    void announceWinner() const;
    bool isGameOver() const;
};

#endif //TEAM_GOTHAM_GAME_H