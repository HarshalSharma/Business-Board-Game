/*
 * Copyright (c) 2020 Harshal Sharma
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.harshalworks.businessbg;

import com.harshalworks.businessbg.exceptions.CannotRegisterPlayerWhileGameHasBegun;
import com.harshalworks.businessbg.player.BoardGamePlayer;

import java.util.HashSet;
import java.util.Set;

public class Game {

    private Set<BoardGamePlayer> players = null;
    private boolean isRunning = false;
    private final int FIXED_START_AMOUNT_FOR_PLAYER;
    private int bankAmount;

    public Game(int fixedAmountForPlayer, int initialAmountOfBank) {
        this.FIXED_START_AMOUNT_FOR_PLAYER = fixedAmountForPlayer;
        this.bankAmount = initialAmountOfBank;
        this.players = new HashSet<>();
    }

    public Player registerPlayer(String uniqueName) {
        if (!isRunning) {
            BoardGamePlayer player = new BoardGamePlayer(FIXED_START_AMOUNT_FOR_PLAYER, uniqueName);
            players.add(player);
            return player;
        } else {
            throw new CannotRegisterPlayerWhileGameHasBegun();
        }
    }

    public void start() {
        if (players.size() >= 2) {
            isRunning = true;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getBankMoneyValue() {
        return bankAmount;
    }

}
