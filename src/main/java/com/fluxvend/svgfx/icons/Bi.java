/*
 * Copyright 2024 Fluxvend
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.fluxvend.svgfx.icons;

public enum Bi {
    ARROW_LEFT_CIRCLE("arrow-left-circle"),
    ARROW_LEFT_SQUARE_FILL("arrow-left-square-fill"),
    ARROW_REPEAT("arrow-repeat"),
    BANK("bank"),
    BOX_ARROW_IN_DOWN("box-arrow-in-down"),
    BOX_ARROW_UP("box-arrow-up"),
    CALCULATOR("calculator"),
    CART_X("cart-x"),
    CASH_COIN("cash-coin"),
    CASH_STACK("cash-stack"),
    CLOCK("clock"),
    CURRENCY_DOLLAR("currency-dollar"),
    DATABASE_FILL_X("database-fill-x"),
    DOOR_OPEN("door-open"),
    GEAR("gear"),
    GRID("grid"),
    LOCK("lock"),
    PALETTE("palette"),
    PERCENT("percent"),
    PERSON_ADD("person-add"),
    PRINTER("printer"),
    RECEIPT("receipt"),
    RULERS("rulers"),
    SAVE("save"),
    TRASH("trash"),
    UPC_SCAN("upc-scan"),
    X_CIRCLE("x-circle")
    ;
    private final String icon;
    Bi(String icon) {
        this.icon = icon;
    }

    public String getIcon(){
        return icon;
    }
}
