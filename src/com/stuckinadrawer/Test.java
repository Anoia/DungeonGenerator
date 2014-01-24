package com.stuckinadrawer;

import com.stuckinadrawer.generator.Generator;
import com.stuckinadrawer.generator.GeneratorBSPLayout;
import com.stuckinadrawer.renderer.Renderer;

public class Test {
    public static void main(String[] args){

        Generator g = new GeneratorBSPLayout();
        Tile[][] level = g.generate();

        new Renderer(level);

    }
}
