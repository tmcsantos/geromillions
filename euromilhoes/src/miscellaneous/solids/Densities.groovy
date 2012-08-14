/**
 * 
 */
package miscellaneous.solids

/**
 * @author helik
 * http://www.engineeringtoolbox.com/density-solids-d_1265.html
 */
enum Densities {
	RUBBER_HARD(1.2),
	RUBBER_SOFT(1.1),
	RUBBER_GUM(0.92)
	private double den
	public Densities(double den) {
		this.den = den
	}
	public getDensity(){
		this.den * 10**3
	}
}