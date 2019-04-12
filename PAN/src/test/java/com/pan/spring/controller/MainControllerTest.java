package com.pan.spring.controller;

import static org.junit.Assert.*;

import org.junit.Test;

public class MainControllerTest {

	@Test
	public void testChill() {
		MainController mainController = new MainController();
        String result = mainController.chill();
        //comment
        assertEquals(result, "chill");	}

}
