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

package com.harshalworks.businessbg.board.cell;

import com.harshalworks.businessbg.dealers.MarketAssistant;
import com.harshalworks.businessbg.player.BoardGamePlayer;

/**
 * Blank cell represents that this cell apply no rules to the player.
 *
 * Is violation interface segregation principle?
 * My view is that it's not, because not doing anything when asked is it's functionality.
 * No other interfaces are forced to implement.
 */
public class BlankCell extends Cell{

    @Override
    public void execute(BoardGamePlayer player, MarketAssistant bank) {
        //Intentionally left blank to represent that this cell does nothing.
        //Have some better approach? Please raise the change with PR.
    }
}
