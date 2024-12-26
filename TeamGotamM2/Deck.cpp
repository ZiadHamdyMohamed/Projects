#include "Deck.h"
#include <algorithm>
#include <random>
#include <iostream>

Deck::Deck(int rows, int cols) : rows(rows), cols(cols) {
    grid = new Card*[rows];
    for (int i = 0; i < rows; ++i) {
        grid[i] = new Card[cols];
    }
}

Deck::~Deck() {
    for (int i = 0; i < rows; ++i) {
        delete[] grid[i];
    }
    delete[] grid;
}
void Deck::shuffle(vector<Card*>& cards) {
    random_shuffle(cards.begin(), cards.end());
    int index = 0;
    for (int i = 0; i < rows; ++i) {
        for (int j = 0; j < cols; ++j) {
            grid[i][j] = *cards[index++];
        }
    }
}

void Deck::displayGrid() const {
    for (int i = 0; i < rows; ++i) {
        for (int j = 0; j < cols; ++j) {
            grid[i][j].display();
        }
        cout << endl;
    }
}

void Deck::resetGrid() {
    for (int i = 0; i < rows; ++i) {
        for (int j = 0; j < cols; ++j) {
            grid[i][j].hide();
        }
    }
}

void Deck::removeCard(int x, int y) {
    grid[x][y].setNumber(0);
    grid[x][y].hide();
}

Card** Deck::getGrid() const {
    return grid;
}

int Deck::getRows() const {
    return rows;
}

int Deck::getCols() const {
    return cols;
}