package de.witchcafe.wator;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import de.witchcafe.wator.WaTor;

class WatorTests {

	@Test
	void test() {
		Species fish = new Species("Fisch", "#3ddc97", 10.0, 1.0, 0.5, 6.0, 0.0,
				1, 3, 360, true, false, Set.of(), Set.of("Hai"));
		Species shark = new Species("Hai", "#ff6b6b", 15.0, 1.5, 0.5, 6.0, 6.0,
				2, 6, 180, false, false, Set.of("Fisch"), Set.of());
		Map<Species, Integer> population = new LinkedHashMap<>();
		population.put(fish, 2);
		population.put(shark, 1);
		BiotopSettings biotopSettings = new BiotopSettings(1.0, 0.1, 0.1);
		WaTor wator = new WaTor(4, 8, population, biotopSettings);
		System.out.println(wator);
		for (int indexd = 0;indexd < 10;indexd ++) {
			wator.waTorMove();
			System.out.println(wator);			
		}
	}

}
