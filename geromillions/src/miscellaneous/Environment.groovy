/**
 * 
 */
package miscellaneous

/**
 * @author helik
 *
 */

enum Humidity {
	WINTER(0.30),
	SUMMER(0.50)
	private double humidity
	Humidity(double humidity){
		this.humidity = humidity
	}
	public getHumidity(){
		return this.humidity
	}
} 

enum Temperature {
	WINTER(22.2222222),
	SUMMER(23.8888889)
	private double temperature
	Temperature(double temperature){
		this.temperature = temperature
	}
	public getTemperature(){
		return this.temperature
	}
}

class Environment {
	def gravity_force = 9.8067
	Humidity humidity
	Temperature temperature
	def pressure = 1013.25
}
