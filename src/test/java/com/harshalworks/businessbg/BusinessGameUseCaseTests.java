package com.harshalworks.businessbg;

import org.junit.Assert;
import org.junit.Test;

public class BusinessGameUseCaseTests {

    @Test
    public void canNotStartAGameWithLessThan2UniquePlayers(){
        //Given
        Game game = new Game();
        Player player1 = new Player();

        //when
        game.registerPlayer(player1);
        game.registerPlayer(player1);
        game.start();

        //assert
        Assert.assertFalse(game.isRunning());
    }

}
