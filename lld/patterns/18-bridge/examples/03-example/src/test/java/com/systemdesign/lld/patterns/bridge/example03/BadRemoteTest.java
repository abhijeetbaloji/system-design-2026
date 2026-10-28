package com.systemdesign.lld.patterns.bridge.example03;

import com.systemdesign.lld.patterns.bridge.example03.bad.BadAdvancedRadioRemote;
import com.systemdesign.lld.patterns.bridge.example03.bad.BadBasicTvRemote;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Bad Remote Inflexible Inheritance Tests")
class BadRemoteTest {

    @Test
    @DisplayName("Bad remotes are permanently hardcoded to specific appliance hardware")
    void testHardcodedAppliance() {
        BadBasicTvRemote tvRemote = new BadBasicTvRemote();
        tvRemote.togglePower();
        tvRemote.volumeUp();
        assertTrue(tvRemote.isTvPower());
        assertEquals(25, tvRemote.getTvVolume());

        BadAdvancedRadioRemote radioRemote = new BadAdvancedRadioRemote();
        radioRemote.togglePower();
        radioRemote.mute();
        assertEquals(0, radioRemote.getRadioVolume());
        // Cannot use tvRemote on a radio, or radioRemote on a tv
    }
}
