import miscellaneous.solids.Densities;

euromilhoes {
	numbers = 1..50
	stars = 1..11
	ball {
		radius = 0.025 //meter
		// density one of RUBBER_GUM, RUBBER_SOFT, RUBBER_HARD
		density = Densities.RUBBER_GUM
	}
}