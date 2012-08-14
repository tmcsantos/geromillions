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
	
	StarNumber(){
		super()
		this.radius = 0.025
	}
	
	String toString() {
		"$number"
	}
}
