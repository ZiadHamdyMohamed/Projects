cmake_minimum_required(VERSION 3.27)
project(team_gotham)

set(CMAKE_CXX_STANDARD 23)

add_executable(team_gotham main.cpp
        Card.h
        StandardCard.h
        BonusCard.h
        PenaltyCard.h
        Deck.h
        Player.h
        Game.h
        Card.cpp
        StandardCard.cpp
        BounsCard.cpp
        PenaltyCard.cpp
        Player.cpp
        Deck.cpp
        Game.cpp
        Game.cpp
)
