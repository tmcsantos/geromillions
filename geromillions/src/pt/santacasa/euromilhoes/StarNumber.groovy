/**
 * 
 */
package pt.santacasa.euromilhoes

/**
 * @author helik
 *
 */
class StarNumber extends Ball {
	Integer number
	double heat
	
	StarNumber(){
		super()
	}
	
	def getWeight(){
		super.getWeight() - super.getLift(heat)
	}
	
	String toString() {
		"$number"
	}
}
