/**
 * 
 */
package pt.santacasa.euromilhoes

import miscellaneous.Environment
import miscellaneous.Humidity
import miscellaneous.Temperature

/**
 * @author helik
 *
 */
class Extractor {
	def config
	def numbers
	def stars
	def environment
	
	Extractor(){
		config = new ConfigSlurper().parse(new File('config/Config.groovy').toURI().toURL())
		environment = new Environment()
		setNumbers()
		setStars()
	}
	
	def setNumbers() {
		numbers = config.euromilhoes.numbers.collect {
			new BallNumber(
				number: it,
				env: getEnvironment(),
				radius: this.config.euromilhoes.ball.radius,
				density: this.config.euromilhoes.ball.density
				)
		}
	}
	
	def setStars() {
		stars = config.euromilhoes.stars.collect {
			new StarNumber(
				number: it,
				env: getEnvironment(),
				radius: config.euromilhoes.ball.radius,
				density: config.euromilhoes.ball.density
				)
		}
	}
	
	def randomKey() {
		Key key = new Key()
		Random rand = new Random()
		
		def generate = {
			(0..4).collect {
				key.addNumber numbers.remove (rand.nextInt(config.euromilhoes.numbers.last()-it))
			}
			(0..1).collect {
				key.addStar stars.remove (rand.nextInt(config.euromilhoes.stars.last()-it))
			}
		}
		generate()
		key
	}
}
