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
