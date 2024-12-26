#ifndef TEAM_GOTHAM_PLAYER_H
#define TEAM_GOTHAM_PLAYER_H
#include <iostream>
using namespace std;

class Player {
private:
    string name;
    int score;

public:
    Player();
    Player(string name);
    ~Player();

    string getName() const;
    void setName(string playerName);

    int getScore() const;
    void setScore(int playerScore);

    void increaseScore(int points);
    void decreaseScore(int points);

    void displayScore() const;
};

#endif //TEAM_GOTHAM_PLAYER_H