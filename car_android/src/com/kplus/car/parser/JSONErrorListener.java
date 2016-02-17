package com.kplus.car.parser;

public interface JSONErrorListener {
    void start(String text);
    void error(String message, int column);
    void end();
}
