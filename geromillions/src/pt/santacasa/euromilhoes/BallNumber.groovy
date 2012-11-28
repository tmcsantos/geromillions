/**
 * 
 */
package pt.santacasa.euromilhoes

/**
 * @author helik
 *
 */
class BallNumber extends Ball {
	Integer number
	double heat
	
	BallNumber(){
		super()
	}
	
	def getWeight(){
		super.getWeight() - super.getLift(heat)
	}
	
	String toString() {
		"${number}"
	}
}
