#ifndef TEAM_GOTHAM_DECK_H
#define TEAM_GOTHAM_DECK_H
#include <iostream>
#include "Card.h"
#include <vector>
using namespace std;

class Deck {
private:
    Card** grid;
    int rows;
    int cols;

public:
    Deck(int rows = 4, int cols = 4);
    ~Deck();

    void shuffle(vector<Card*>& cards);
    void displayGrid() const;
    void resetGrid();
    void removeCard(int x, int y);

    Card** getGrid() const;
    int getRows() const;
    int getCols() const;
};

#endif //TEAM_GOTHAM_DECK_H