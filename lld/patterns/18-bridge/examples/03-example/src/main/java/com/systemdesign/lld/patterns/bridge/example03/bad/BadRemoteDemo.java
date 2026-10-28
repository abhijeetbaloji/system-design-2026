package com.systemdesign.lld.patterns.bridge.example03.bad;

public class BadRemoteDemo {
    public static void main(String[] args) {
        BadBasicTvRemote tvRemote = new BadBasicTvRemote();
        tvRemote.togglePower();
        tvRemote.volumeUp();

        BadAdvancedRadioRemote radioRemote = new BadAdvancedRadioRemote();
        radioRemote.togglePower();
        radioRemote.mute();

        System.out.println("TV Power: " + tvRemote.isTvPower() + ", Volume: " + tvRemote.getTvVolume());
        System.out.println("Radio Power: " + radioRemote.isRadioPower() + ", Volume: " + radioRemote.getRadioVolume());
    }
}
