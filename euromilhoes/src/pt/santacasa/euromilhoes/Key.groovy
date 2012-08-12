/**
 * 
 */
package pt.santacasa.euromilhoes

/**
 * @author helik
 *
 */
class Key {
	Set<BallNumber> numbers = []as Set
	Set<StarNumber> stars = []as Set
	
	def addNumber = { BallNumber number ->
		numbers << number
	}
	
	def addStar = { StarNumber star ->
		stars << star
	}
	
	String toString() {
		"${numbers.join(',')} +${stars.join(' +')}"
	}
}
