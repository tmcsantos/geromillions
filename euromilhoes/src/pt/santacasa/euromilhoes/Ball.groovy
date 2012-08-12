/**
 * 
 */
package pt.santacasa.euromilhoes

import miscellaneos.solids.Densities


/**
 * @author helik
 *
 */
abstract class Ball {
	def weight
	def volume
	def radius
	Densities density = Densities.RUBBER_SOFT //Rubber, soft commercial
	
	def getVolume() {
		this.volume = this.volume ?: (4 * Math.PI * this.radius**3) / 3
	}
	
	def getWeight() {
		this.weight = this.weight ?: this.getVolume() * this.density.getDensity()
	}
}
