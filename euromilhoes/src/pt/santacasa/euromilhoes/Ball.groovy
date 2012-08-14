/**
 * 
 */
package pt.santacasa.euromilhoes

import miscellaneous.Environment
import miscellaneous.solids.Densities


/**
 * @author helik
 *
 */
abstract class Ball {
	def weight
	def mass
	def volume
	def radius = 0.0
	def airRadius = 0.001
	Environment env = new Environment() 
	Densities density = Densities.RUBBER_SOFT //Rubber, soft commercial
	
	def getVolume() {
		this.volume = this.volume ?: (4 * Math.PI * this.radius**3) / 3
	}
	
	def getMass() {
		this.mass = this.mass ?: this.getVolume() * this.density.getDensity()
	}
	
	def getWeight(){
		this.weight = this.weight ?: env.getGravity_force()*getMass()
	}
	
	def getLift(double temperature) {
		def outerVolume = { ((4 * Math.PI * (this.radius + this.airRadius)**3) / 3) - getVolume() }
		outerVolume() * (env.getPressure()/2.87) * (1/env.getTemperature().getTemperature() - 1/temperature)
	}
}
