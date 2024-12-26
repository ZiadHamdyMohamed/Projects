#include "Player.h"
#include "Game.h"
#include <random>
#include <iostream>
using namespace std;

int main() {
    Player player1("Player 1");
    Player player2("Player 2");

    Game game(player1, player2);
    game.initializeGame();

    while (!game.isGameOver()) {
        game.playTurn();
    }
    game.announceWinner();
    return 0;
}