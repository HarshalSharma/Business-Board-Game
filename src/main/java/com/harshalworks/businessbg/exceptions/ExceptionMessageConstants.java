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

package com.harshalworks.businessbg.exceptions;

public interface ExceptionMessageConstants {

    String TURNS_WHEN_IT_S_NOT_THEIR_CHANCE = "PLAYERS CANNOT MAKE TURNS WHEN IT'S NOT THEIR CHANCE.";

    String GAME_IS_ALREADY_RUNNING = "GAME IS ALREADY RUNNING.";

    String WHEN_THE_GAME_HAS_ALREADY_STARTED = "WHEN THE GAME IS ALREADY STARTED.";

    String GIVEN_BOARD_LENGTH_IS_ZERO = "GIVEN BOARD LENGTH IS ZERO.";

    String NO_CELL_TYPE_DEFINED = "NO CELL TYPE IS DEFINED FOR THE GIVEN TYPE ";

    String CANNOT_PURCHASE_CELL = "CANNOT PURCHASE THIS CELL.";

    String NOT_ENOUGH_PLAYERS = "NOT ENOUGH PLAYERS AVAILABLE TO START GAME";

    String GAME_HAS_FINISHED = "GAME IS ALREADY FINISHED.";
}