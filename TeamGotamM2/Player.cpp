#include "Player.h"

Player::Player() : name(""), score(0) {}

Player::Player(string name) : name(name), score(0) {}

Player::~Player() {}

string Player::getName() const {
    return name;
}

void Player::setName(string playerName) {
    name = playerName;
}

int Player::getScore() const {
    return score;
}

void Player::setScore(int playerScore) {
    score = playerScore;
}

void Player::increaseScore(int points) {
    score += points;
}

void Player::decreaseScore(int points) {
    score -= points;
}

void Player::displayScore() const {
    cout << name << "'s Score: " << score << endl;
}