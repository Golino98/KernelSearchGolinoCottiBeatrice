package com.golinocottibeatrice.kernelsearch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AcceptanceCounterTest {
    @Test
    void sameObjCounterOneResetOne() {
        var counter = new AcceptanceCounter(1,1);

        assertFalse(counter.solution(1));
        // Dopo il primo numero dovrebbe ritornare true per 1 chiamata
        assertTrue(counter.solution(1));
        assertFalse(counter.solution(2));

        // Adesso dovrebbe ritornare ancora una volta true
        assertTrue(counter.solution(3));
    }

    @Test
    void sameObjCounter3Reset1() {
        var counter = new AcceptanceCounter(3,1);
        assertFalse(counter.solution(1));
        assertFalse(counter.solution(1));
        assertFalse(counter.solution(1));

        // Dovrebbe ritornare true esattamente per una chiamata
        assertTrue(counter.solution(1));
        assertFalse(counter.solution(1));
    }

    @Test
    void allDiffObj() {
        var counter = new AcceptanceCounter(3,3);
        assertFalse(counter.solution(1));
        assertFalse(counter.solution(2));
        assertFalse(counter.solution(3));

        assertFalse(counter.solution(4));
    }

    @Test
    void oneDiffObj() {
        var counter = new AcceptanceCounter(3,3);
        assertFalse(counter.solution(1));
        assertFalse(counter.solution(1));
        assertFalse(counter.solution(3));

        assertFalse(counter.solution(1));
    }

    @Test
    void resetAllEqual() {
        var counter = new AcceptanceCounter(3,3);
        assertFalse(counter.solution(1));
        assertFalse(counter.solution(1));
        assertFalse(counter.solution(1));

        // Dovrebbe ritornare true per 3 chiamate
        assertTrue(counter.solution(1));
        assertTrue(counter.solution(1));
        assertTrue(counter.solution(1));

        // Poi torna ad essere false
        assertFalse(counter.solution(1));
        assertFalse(counter.solution(1));
    }

    @Test
    void resetDifferent() {
        var counter = new AcceptanceCounter(3,3);
        assertFalse(counter.solution(1));
        assertFalse(counter.solution(1));
        assertFalse(counter.solution(1));

        // Ritorna true per 3 chiamate
        assertTrue(counter.solution(1));
        assertTrue(counter.solution(2));
        assertTrue(counter.solution(3));

        // Poi torna ad essere false
        assertFalse(counter.solution(3));
        assertFalse(counter.solution(3));
        assertFalse(counter.solution(3));

        assertTrue(counter.solution(1));
    }
}