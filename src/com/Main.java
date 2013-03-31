/* CPSC 304 - Library Checkout System
 * © Mar. 2013 Kevin Petersen. All rights reserved.
 */

package com;

import com.ui.Interface;

public class Main {
	public static void main(String args[]) {
		Interface i = new Interface();
		i.loadDriver();
		System.out.print("Welcome!");
		int attempts = 3;
		while(attempts-- > 0) {
			if(i.connect()) {
				i.showMenu();
				System.exit(0);
			}
		}
		System.exit(-1);
	}
	
	
}