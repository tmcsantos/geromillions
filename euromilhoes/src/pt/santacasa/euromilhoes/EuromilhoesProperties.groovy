/**
 * 
 */
package pt.santacasa.euromilhoes

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * @author helik
 *
 */
class EuromilhoesProperties {
	def config
	private final static Log log = LogFactory.getLog(EuromilhoesProperties.class)
	private static final instance = new EuromilhoesProperties()
	
	static getInstance(){ instance }
	
	private EuromilhoesProperties(){
		config = new ConfigSlurper().parse(EuromilhoesProperties.class.classLoader.getResource("Config.groovy"))
		try {
			def customConfig = new ConfigSlurper().parse(new File('config/Config.groovy').toURI().toURL())
			log.info "Merging with customized configurations"
			config = config.merge(customConfig)
		} catch (FileNotFoundException e) {
			log.warn "\r\n\tUsing default configurations\r\n\tCreate a ./config/Config.groovy file if you want to customize"
		}
	}
	
	def getProperty(String name) {
		config[name]
	}
	
	void setProperty(String name, value) {
		config[name] = value
	}
}
