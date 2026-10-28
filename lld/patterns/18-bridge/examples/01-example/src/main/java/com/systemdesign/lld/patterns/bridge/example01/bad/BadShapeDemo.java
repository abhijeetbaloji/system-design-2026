package com.systemdesign.lld.patterns.bridge.example01.bad;

public class BadShapeDemo {
    public static void main(String[] args) {
        // Combinatorial explosion
        BadVectorCircle vc = new BadVectorCircle(10.0);
        BadRasterCircle rc = new BadRasterCircle(10.0);
        BadVectorRectangle vr = new BadVectorRectangle(20.0, 30.0);
        BadRasterRectangle rr = new BadRasterRectangle(20.0, 30.0);

        System.out.println(vc.drawVector());
        System.out.println(rc.drawPixels());
        System.out.println(vr.drawVector());
        System.out.println(rr.drawPixels());
    }
}
