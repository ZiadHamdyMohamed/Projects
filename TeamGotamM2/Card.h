#ifndef TEAM_GOTHAM_CARD_H
#define TEAM_GOTHAM_CARD_H
#include <iostream>
using namespace std;

class Card {
protected:
    int number;
    bool isFaceUp;

public:
    Card();
    Card(int num);
    virtual ~Card();

    int getNumber() const;
    void setNumber(int num);

    bool getFaceUp() const;
    void setFaceUp(bool faceUp);

    virtual void display() const;
    void reveal();
    void hide();
};

#endif