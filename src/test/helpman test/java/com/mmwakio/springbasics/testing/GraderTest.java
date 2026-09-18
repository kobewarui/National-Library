package com.mmwakio.helpman.testing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraderTest {

    @Test
    void fiftyNineShouldReturnF() {
        Grader grader = new Grader();
        char result = grader.determineLetterGrade(59);
        assertEquals('F', result);
    }

    @Test
    void sixtyNineShouldReturnD() {
        Grader grader = new Grader();
        char result = grader.determineLetterGrade(69);
        assertEquals('D', result);
    }

    @Test
    void seventyNineShouldReturnC() {
        Grader grader = new Grader();
        char result = grader.determineLetterGrade(79);
        assertEquals('C', result);
    }

    @Test
    void eightyNineShouldReturnB() {
        Grader grader = new Grader();
        char result = grader.determineLetterGrade(89);
        assertEquals('B', result);
    }

    @Test
    void ninetyNineShouldReturnA() {
        Grader grader = new Grader();
        char result = grader.determineLetterGrade(99);
        assertEquals('A', result);
    }

    @Test
    void negativeOneShouldReturnIllegalArgumentException() {
        Grader grader = new Grader();
        char result = grader.determineLetterGrade(-1);
        assertThrows(IllegalArgumentException.class, () -> {
            grader.determineLetterGrade(result);
        });
    }
}