/**
 * 
 */
package pt.santacasa.euromilhoes

/**
 * @author helik
 *
 */
class Generator {
	def config = new ConfigSlurper().parse(new File('config/Config.groovy').toURI().toURL())
	def numbers = config.euromilhoes.numbers.collect {new BallNumber(number: it)}
	def stars = config.euromilhoes.stars.collect {new StarNumber(number: it)}
	Key key = new Key()
	Random rand = new Random()
	
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
