/**
 * 
 */
package pt.santacasa.euromilhoes

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.tools.shell.commands.ExitCommand;

import miscellaneous.Environment
import miscellaneous.Humidity
import miscellaneous.Temperature

@Category(Integer)
class Polynomial{
	def polynomial(index=0) { ((this-5.5)**2) / 1000 + index}
	def inv_polynomial(index=0) { (-(this-5.5)**2 + 35) / 1000 + index}
}

/**
 * @author helik
 *
 */
class Extractor {
	def config
	def numbers = []
	def stars = []
	Environment environment
	private final static Log log = LogFactory.getLog(Extractor.class)
	
	Extractor(){
		config = EuromilhoesProperties.instance
		environment = new Environment()
		initNumbers()
		initStars()
	}
	
	def setTemperature(Temperature temp){
		environment.setTemperature(temp)
		initNumbers()
		initStars()
	}
	
	def initNumbers() {
		numbers = config.euromilhoes.numbers.collect {
			new BallNumber(
				number: it,
				env: getEnvironment(),
				radius: this.config.euromilhoes.ball.radius,
				density: this.config.euromilhoes.ball.density,
				heat: getEnvironment()?.getTemperature()?.getTemperature()?:0.0
				)
		}
	}
	
	def initStars() {
		stars = config.euromilhoes.stars.collect {
			new StarNumber(
				number: it,
				env: getEnvironment(),
				radius: config.euromilhoes.ball.radius,
				density: config.euromilhoes.ball.density,
				heat: getEnvironment()?.getTemperature()?.getTemperature()?:0.0
				)
		}
	}
	
	private def extractNumbers(numbers, Random rand){
		def _numbers = this.numbers.collate(10)
		use(Polynomial){
			def vector = (0..4)*.inv_polynomial(rand.nextDouble())
			def prevheat = 0
			def heatvector = { ->
				prevheat += rand.nextDouble() * 0.015
				prevheat
			}
			def matrix = [
				(1..10)*.polynomial(heatvector()),
				(1..10)*.polynomial(heatvector()),
				(1..10)*.polynomial(heatvector()),
				(1..10)*.polynomial(heatvector()),
				(1..10)*.polynomial(heatvector())
				].transpose().collect {
					def z = []
					it.eachWithIndex { v,i -> z << v.multiply(vector[i]) }
					z
				}.transpose()
			matrix.eachWithIndex {_l,l->
				_l.eachWithIndex {_c,c->
					def temp = environment.getTemperature().getTemperature()
					_numbers[l][c].setHeat(temp + _c)
				}
			}
		}
		numbers.sort { a,b -> b.getWeight() <=> a.getWeight() }
	}
	
	private def extractStars(stars, Random rand){
		def _stars = this.stars.reverse().collate(6).reverse()
		use(Polynomial){
			def vector = (0..5)*.inv_polynomial(rand.nextDouble())
			def prevheat = 0
			def heatvector = { ->
				prevheat += rand.nextDouble() * 0.0015
				prevheat
			}
			def matrix = [
				(1..5)*.polynomial(heatvector()),
				(1..6)*.polynomial(heatvector())
				].transpose().collect {
					def z = []
					it.eachWithIndex { v,i -> z << v.multiply(vector[i]) }
					z
				}.transpose()
			matrix.eachWithIndex {_l,l->
				_l.eachWithIndex {_c,c->
					def temp = environment.getTemperature().getTemperature()
					_stars[l][c].setHeat(temp + _c)
				}
			}
		}
		stars.sort { a,b -> b.getWeight() <=> a.getWeight() }
	}
	
	def extraction(def numbers = 5, def stars = 2, Calendar date = Calendar.instance){
		Key key = new Key()
		def __numbers = this.numbers.clone()
		def __stars = this.stars.clone()
		def _numbers = this.numbers.collate(10)
		def _stars = this.stars.reverse().collate(6).reverse()
		Random rand = new Random(date.getTimeInMillis())
		
		extractNumbers(__numbers, rand)
		(0..numbers-1).collect {
			rand.setSeed(date.getTimeInMillis() + rand.nextInt(8) + rand.nextInt(15))
			key.addNumber __numbers.remove (rand.nextInt(config.euromilhoes.numbers.last()-it))
		}
		extractStars(__stars, rand)
		(0..stars-1).collect {
			rand.setSeed(date.getTimeInMillis() + rand.nextInt(8) + rand.nextInt(15))
			key.addStar __stars.remove (rand.nextInt(config.euromilhoes.stars.last()-it))
		}
		key
	}
	
	def randomKey(def numbers = 5,def stars = 2) {
		Key key = new Key()
		Random rand = new Random()
		def _numbers = this.numbers.clone()
		def _stars = this.stars.clone()
		
		def generate = {
			(0..numbers-1).collect {
				key.addNumber _numbers.remove (rand.nextInt(config.euromilhoes.numbers.last()-it))
			}
			(0..stars-1).collect {
				key.addStar _stars.remove (rand.nextInt(config.euromilhoes.stars.last()-it))
			}
		}
		generate()
		key
	}
}
