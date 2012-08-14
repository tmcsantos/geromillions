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
	
	BallNumber(){
		super()
		this.radius = 0.025
	}
	
	String toString() {
		"$number"
	}
}
