package com.kplus.car.parser;

public class StdoutStreamErrorListener extends BufferErrorListener {
    
    public void end() {
        System.out.print(buffer.toString());
    }
}
