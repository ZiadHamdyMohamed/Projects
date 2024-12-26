#include "Card.h"

Card::Card() : number(0), isFaceUp(false) {}

Card::Card(int num) : number(num), isFaceUp(false) {}

Card::~Card() {}

int Card::getNumber() const {
    return number;
}

void Card::setNumber(int num) {
    number = num;
}

bool Card::getFaceUp() const {
    return isFaceUp;
}

void Card::setFaceUp(bool faceUp) {
    isFaceUp = faceUp;
}

void Card::display() const {
    if (isFaceUp)
        cout << number << " ";
    else
        cout << "* ";
}

void Card::reveal() {
    isFaceUp = true;
}

void Card::hide() {
    isFaceUp = false;
}