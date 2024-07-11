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
    /**
     * bi-arrow-left-circle
     */
    ARROW_LEFT_CIRCLE("arrow-left-circle"),
    /**
     * bi-arrow-left-square-fill
     */
    ARROW_LEFT_SQUARE_FILL("arrow-left-square-fill"),
    /**
     * bi-arrow-repeat
     */
    ARROW_REPEAT("arrow-repeat"),
    /**
     * bi-bank
     */
    BANK("bank"),
    /**
     * bi-arrow-in-down
     */
    BOX_ARROW_IN_DOWN("box-arrow-in-down"),
    /**
     * bi-box-arrow-up
     */
    BOX_ARROW_UP("box-arrow-up"),
    /**
     * bi-calculator
     */
    CALCULATOR("calculator"),
    /**
     * bi-cart-x
     */
    CART_X("cart-x"),
    /**
     * bi-cash-coin
     */
    CASH_COIN("cash-coin"),
    /**
     * bi-cash-stack
     */
    CASH_STACK("cash-stack"),
    /**
     * bi-clock
     */
    CLOCK("clock"),
    /**
     * bi-currency-dollar
     */
    CURRENCY_DOLLAR("currency-dollar"),
    /**
     * bi-database-fill-x
     */
    DATABASE_FILL_X("database-fill-x"),
    /**
     * bi-door-open
     */
    DOOR_OPEN("door-open"),
    /**
     * bi-gear
     */
    GEAR("gear"),
    /**
     * bi-grid
     */
    GRID("grid"),
    /**
     * bi-lock
     */
    LOCK("lock"),
    /**
     * bi-palette
     */
    PALETTE("palette"),
    /**
     * bi-percent
     */
    PERCENT("percent"),
    /**
     * bi-person-add
     */
    PERSON_ADD("person-add"),
    /**
     * bi-printer
     */
    PRINTER("printer"),
    /**
     * bi-receipt
     */
    RECEIPT("receipt"),
    /**
     * bi-rulers
     */
    RULERS("rulers"),
    /**
     * bi-save
     */
    SAVE("save"),
    /**
     * bi-trash
     */
    TRASH("trash"),
    /**
     * bi-upc-scan
     */
    UPC_SCAN("upc-scan"),
    /**
     * bi-x-circle
     */
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
