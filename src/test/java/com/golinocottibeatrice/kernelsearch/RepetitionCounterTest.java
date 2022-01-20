package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.additions.RepetitionCounter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepetitionCounterTest {
    @Test
    void sameObjCounterOneResetOne() {
        var counter = new RepetitionCounter(1, 1, 0);

        assertFalse(counter.value(1));
        // Dopo il primo numero dovrebbe ritornare true per 1 chiamata
        assertTrue(counter.value(1));
        assertFalse(counter.value(2));

        // Adesso dovrebbe ritornare ancora una volta true
        assertTrue(counter.value(3));
    }

    @Test
    void sameObjCounter3Reset1() {
        var counter = new RepetitionCounter(3, 1, 0);
        assertFalse(counter.value(1));
        assertFalse(counter.value(1));
        assertFalse(counter.value(1));

        // Dovrebbe ritornare true esattamente per una chiamata
        assertTrue(counter.value(1));
        assertFalse(counter.value(1));
    }

    @Test
    void allDiffObj() {
        var counter = new RepetitionCounter(3, 3, 0);
        assertFalse(counter.value(1));
        assertFalse(counter.value(2));
        assertFalse(counter.value(3));

        assertFalse(counter.value(4));
    }

    @Test
    void oneDiffObj() {
        var counter = new RepetitionCounter(3, 3, 0);
        assertFalse(counter.value(1));
        assertFalse(counter.value(1));
        assertFalse(counter.value(3));

        assertFalse(counter.value(1));
    }

    @Test
    void resetAllEqual() {
        var counter = new RepetitionCounter(3, 3, 0);
        assertFalse(counter.value(1));
        assertFalse(counter.value(1));
        assertFalse(counter.value(1));

        // Dovrebbe ritornare true per 3 chiamate
        assertTrue(counter.value(1));
        assertTrue(counter.value(1));
        assertTrue(counter.value(1));

        // Poi torna ad essere false
        assertFalse(counter.value(1));
        assertFalse(counter.value(1));
    }

    @Test
    void resetDifferent() {
        var counter = new RepetitionCounter(3, 3, 0);
        assertFalse(counter.value(1));
        assertFalse(counter.value(1));
        assertFalse(counter.value(1));

        // Ritorna true per 3 chiamate
        assertTrue(counter.value(1));
        assertTrue(counter.value(2));
        assertTrue(counter.value(3));

        // Poi torna ad essere false
        assertFalse(counter.value(3));
        assertFalse(counter.value(3));
        assertFalse(counter.value(3));

        assertTrue(counter.value(1));
    }
}