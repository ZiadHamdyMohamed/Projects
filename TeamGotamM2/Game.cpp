#include "Game.h"
#include <iostream>

Game::Game() : player1Turn(true) {}

Game::Game(Player player1, Player player2) : player1(player1), player2(player2), player1Turn(true) {}

Game::~Game() {}

void Game::initializeGame() {
    vector<Card*> cards;
    for (int i = 1; i <= 6; ++i) {
        cards.push_back(new StandardCard(i));
        cards.push_back(new StandardCard(i));
    }
    cards.push_back(new BonusCard(7));
    cards.push_back(new BonusCard(7));
    cards.push_back(new PenaltyCard(8));
    cards.push_back(new PenaltyCard(8));

    deck.shuffle(cards);

    cout << "Game Initialized! Shuffled Deck:" << endl;
    deck.displayGrid();
}

void Game::playTurn() {
    Player& currentPlayer = player1Turn ? player1 : player2;
    int x1, y1, x2, y2;

    cout << currentPlayer.getName() << "'s turn. Enter coordinates for the first card (x y): ";
    cin >> x1 >> y1;
    cout << "Enter coordinates for the second card (x y): ";
    cin >> x2 >> y2;


    if (x1 < 0 || x1 >= deck.getRows() || y1 < 0 || y1 >= deck.getCols() ||
        x2 < 0 || x2 >= deck.getRows() || y2 < 0 || y2 >= deck.getCols()) {
        cout << "Invalid coordinates. Please try again." << endl;
        return;
    }

    Card* card1 = &deck.getGrid()[x1][y1];
    Card* card2 = &deck.getGrid()[x2][y2];

    card1->reveal();
    card2->reveal();
    deck.displayGrid();

    if (card1->getNumber() == card2->getNumber()) {
        handleStandardCards(card1, card2, currentPlayer);
    } else if (card1->getNumber() == 7 || card2->getNumber() == 7) {
        handleBonusCards(card1, card2, currentPlayer);
    } else if (card1->getNumber() == 8 || card2->getNumber() == 8) {
        handlePenaltyCards(card1, card2, currentPlayer);
    } else {
        card1->hide();
        card2->hide();
        player1Turn = !player1Turn;
    }

    displayScores();
}

void Game::handleStandardCards(Card* card1, Card* card2, Player& currentPlayer) {
    currentPlayer.increaseScore(1);
    deck.removeCard(card1->getNumber(), card2->getNumber());
    cout << "Matched! " << currentPlayer.getName() << " gains 1 point and takes another turn." << endl;
}

void Game::handleBonusCards(Card* card1, Card* card2, Player& currentPlayer) {
    if (card1->getNumber() == 7 && card2->getNumber() == 7) {
        int choice;
        cout << "Both are Bonus Cards! Choose an option: 1) Gain 2 points 2) Gain 1 point and take another turn: ";
        cin >> choice;
        if (choice == 1) {
            currentPlayer.increaseScore(2);
        } else {
            currentPlayer.increaseScore(1);
            player1Turn = !player1Turn;
        }
        deck.removeCard(card1->getNumber(), card2->getNumber());
    } else {
        currentPlayer.increaseScore(1);
        deck.removeCard(card1->getNumber(), card2->getNumber());
        card1->hide();
        card2->hide();
        player1Turn = !player1Turn;
    }
}

void Game::handlePenaltyCards(Card* card1, Card* card2, Player& currentPlayer) {
    if (card1->getNumber() == 8 && card2->getNumber() == 8) {
        int choice;
        cout << "Both are Penalty Cards! Choose an option: 1) Lose 2 points 2) Lose 1 point and skip next turn: ";
        cin >> choice;
        if (choice == 1) {
            currentPlayer.decreaseScore(2);
        } else {
            currentPlayer.decreaseScore(1);
            player1Turn = !player1Turn;
        }
        deck.removeCard(card1->getNumber(), card2->getNumber());
    } else {
        currentPlayer.decreaseScore(1);
        deck.removeCard(card1->getNumber(), card2->getNumber());
        card1->hide();
        card2->hide();
        player1Turn = !player1Turn;
    }
}

void Game::displayScores() const {
    player1.displayScore();
    player2.displayScore();
}

void Game::announceWinner() const {
    if (player1.getScore() > player2.getScore()) {
        cout << player1.getName() << " wins!" << endl;
    } else if (player2.getScore() > player1.getScore()) {
        cout << player2.getName() << " wins!" << endl;
    } else {
        cout << "It's a tie!" << endl;
    }
}

bool Game::isGameOver() const {
    for (int i = 0; i < deck.getRows(); ++i) {
        for (int j = 0; j < deck.getCols(); ++j) {
            if (deck.getGrid()[i][j].getNumber() != 0) {
                return false;
            }
        }
    }
    return true;
}