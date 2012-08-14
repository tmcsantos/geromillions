/**
 * 
 */
package pt.santacasa.euromilhoes

import miscellaneous.Environment;
import miscellaneous.Humidity;
import miscellaneous.Temperature;

/**
 * @author helik
 *
 */
class Generator {
	def config
	def numbers
	def stars
	Key key = new Key()
	Random rand = new Random()
	
	Generator(){
		config = new ConfigSlurper().parse(new File('config/Config.groovy').toURI().toURL())
		def env = new Environment()
		env.setTemperature(Temperature.SUMMER)
		env.setHumidity(Humidity.SUMMER)
		numbers = config.euromilhoes.numbers.collect {
			new BallNumber(
				number: it,
				env: env,
				radius: config.euromilhoes.ball.radius,
				density: config.euromilhoes.ball.density
				)
		}
		stars = config.euromilhoes.stars.collect {
			new StarNumber(
				number: it,
				env: env,
				radius: config.euromilhoes.ball.radius,
				density: config.euromilhoes.ball.density
				)
		}
	}
	
	private def generate = {
		(0..4).collect { 
			key.addNumber numbers.remove (rand.nextInt(config.euromilhoes.numbers.last()-it)) 
		}
		(0..1).collect { 
			key.addStar stars.remove (rand.nextInt(config.euromilhoes.stars.last()-it)) 
		}
	}
	
	def newKey = {
		generate()
		key
	}
}
