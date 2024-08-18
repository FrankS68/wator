package de.witchcafe.wator;

import org.junit.jupiter.api.Test;

import de.witchcafe.wator.WaTor;

class WatorTests {

	@Test
	void test() {
		WaTor wator = new WaTor(4,8,3);
		System.out.println(wator);
		for (int indexd = 0;indexd < 10;indexd ++) {
			wator.waTorMove();
			System.out.println(wator);			
		}
	}

}
