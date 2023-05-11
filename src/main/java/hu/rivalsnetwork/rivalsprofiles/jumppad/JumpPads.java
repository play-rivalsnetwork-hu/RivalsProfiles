package hu.rivalsnetwork.rivalsprofiles.jumppad;

import java.util.ArrayList;

public class JumpPads {
    private static final ArrayList<JumpPad> pads = new ArrayList<>(24);

    public static void add(JumpPad pad) {
        pads.add(pad);
    }

    public static ArrayList<JumpPad> getPads() {
        return pads;
    }
}
