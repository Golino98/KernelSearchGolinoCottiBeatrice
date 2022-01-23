package com.golinocottibeatrice.kernelsearch;

import com.golinocottibeatrice.kernelsearch.additions.RepetitionCounter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepetitionCounterTest {
    @Test
    void h0k0() {
        var counter = new RepetitionCounter(0, 0);

        // All'inizio torna false
        assertFalse(counter.check());

        counter.increment();
        // Dopo un incremento restituisce sempre false
        assertFalse(counter.check());
        assertFalse(counter.check());
    }

    @Test
    void h1k1() {
        var counter = new RepetitionCounter(1, 1);

        // All'inizio torna false
        assertFalse(counter.check());

        counter.increment();
        // Dopo il primo incremento dovrebbe ritornare true fino al prossimo incremento.
        assertTrue(counter.check());
        assertTrue(counter.check());

        counter.increment();
        // Adesso dovrebbe ritornare false
        assertFalse(counter.check());
        assertFalse(counter.check());
    }

    @Test
    void h2k2() {
        var counter = new RepetitionCounter(2, 2);

        // All'inizio torna false
        assertFalse(counter.check());

        counter.increment();
        // Dopo il primo incremento dovrebbe ritornare false
        assertFalse(counter.check());

        counter.increment();
        // Adesso dovrebbe ritornare true
        assertTrue(counter.check());
        assertTrue(counter.check());
    }
}