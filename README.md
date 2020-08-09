![Build and Test](https://github.com/HarshalSharma/Business-Board-Game/workflows/Build%20and%20Test/badge.svg)

# Scope

This Repository is supposed to be a standard **"Business Board Game *(see below)*"** with configure-able Rules for hotels or jails or something new which could be added in the classic board game.

It would not have any UI or other user interaction mechanisms, though, client APIs would be provided, it is supposed to act as a library for exciting future projects or you might build something amazing on top of it as it's going to stay open-sourced. 😃️

Please don't forget to STAR🌟️ it, review it's code🗃️ and provide your valuable feedback or open tickets for feature requests.. 😍️😍️😍️

*Also there would be blog series on TDD and Object Oriented Modelling starting along with it's development. commit by commit. at harshalworks.com*

---
## Usage

### Library Setup
Setup Business-Board-Game as dependency:

gradle setup:

```gradle
repositories {
    mavenCentral()
    ...
    maven { url "https://jitpack.io" }
    ...
}

dependencies {
    ...
    implementation 'com.github.HarshalSharma:Business-Board-Game:v1.0.0'
    ...
}

```
maven setup:
1. Add repository:
```maven
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
2. Add dependency
```maven
<dependency>
    <groupId>com.github.HarshalSharma</groupId>
    <artifactId>Business-Board-Game</artifactId>
    <version>v1.0.0</version>
</dependency>
```

### CODE

Basic usage:

1. Create an instance of FixedRoundGame or extend Game/PublishableGame to create your own BusinessHouse or define your own Builders!

```Java
FixedRoundsGame game = new FixedRoundsGame(START_PLAYER_AMOUNT, INITIAL_AMOUNT_OF_BANK, 
                                                            DICE, BOARDS, ROUNDS_TO_PLAY);
```

2. Register players who want to play with unique names (minimum 2).

```Java
Player player = game.registerPlayer(PLAYER_1_UNIQUE_NAME);
...
game.start();
```

3. Make moves to roll the provided dice, move player ahead and make them follow the rules defined on BOARD cells.
```Java
game.makeMove(player);
```

4. Listen to Events as they occur on the game with PublishableGames ex:
```Java
 game.subscribe(VIEWER)
 ```
---
### About Business: Board Game
Business is the game of buying and selling real estate. Snap up as many squares of land, utilities, and railroads.

The objective of the game is to be the last player remaining with any money.

It is a board game where players roll two six-sided dice to move around the game board, buying and trading properties, and develop them with houses and hotels. Players collect rent from their opponents, with the goal being to drive them into bankruptcy. Money can also be gained or lost through Chance and Community Chest cards, and tax squares; players can end up in jail, which they cannot move from until they have met one of several conditions. There are other places on the board which can not be bought, but instead require the player to draw a card and perform the action on the card, pay taxes, collect income, or even go to jail.


---
*More Documentation would be added over time.*
