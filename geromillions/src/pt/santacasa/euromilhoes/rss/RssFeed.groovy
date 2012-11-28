/**
 * 
 */
package pt.santacasa.euromilhoes.rss

import org.apache.commons.collections.map.LRUMap
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

import pt.santacasa.euromilhoes.EuromilhoesProperties

/**
 * @author helik
 *
 */
class RssFeed {
	static final def DEFAULT_TIME_TO_LIVE = 10 * 60 * 1000
	static final def DEFAULT_ACCESS_TIMEOUT = 5 * 60 * 1000
	static final def DEFAULT_TIMER_INTERVAL = 2 * 60 * 1000
	def config
	enum Channels {RESULTS, PRIZES}
	LRUMap cacheMap
	Timer cacheManager
	private final static Log log = LogFactory.getLog(RssFeed.class)

	RssFeed(){
		config = EuromilhoesProperties.instance
		cacheMap = new LRUMap(config.euromilhoes.rss.size())
		initialize()
	}

	protected class CachedObject {
		def cachedData,
		timeCached,
		timeAccessedLast,
		numberOfAccesses = 0,
		objectTTL = DEFAULT_TIME_TO_LIVE,
		objectIdleTimeout = DEFAULT_ACCESS_TIMEOUT

		CachedObject(cachedData){
			def now = System.currentTimeMillis()
			this.cachedData = cachedData
			timeCached = now
			timeAccessedLast = now
			++numberOfAccesses
		}

		CachedObject(cachedData, timeToLive, idleTimeout) {
			def now = System.currentTimeMillis()
			this.cachedData = cachedData
			objectTTL = timeToLive
			objectIdleTimeout = idleTimeout
			timeCached = now
			timeAccessedLast = now
			++numberOfAccesses
		}

		def getCachedData(key){
			def now = System.currentTimeMillis()
			if (hasExpired(now)){
				cachedData = null
				cacheMap.remove(key)
				return
			}
			timeAccessedLast = now
			++numberOfAccesses
			return cachedData
		}

		def hasExpired(now){
			def usedTTL = objectTTL
			def usedATO = objectIdleTimeout

			return (now > timeAccessedLast + usedATO || now > timeCached + usedTTL )
		}
	}

	private def initialize(){
		cacheManager?.cancel()
		cacheManager = new Timer(true)
		cacheManager.schedule(
				new TimerTask() {
					void run() {
						def now = System.currentTimeMillis()
						try {
							cacheMap.each { key, CachedObject cobj ->
								if (cobj == null || cobj.hasExpired(now)) {
									if (log.isDebugEnabled()) log.debug(
									"Removing " + key + ": Idle time=" +
									(now - cobj.timeAccessedLast) + "; Stale time:" +
									(now - cobj.timeCached))
									cacheMap.remove(key)
									Thread.yield()
								}
							}
						} catch (ConcurrentModificationException cme) {
							// Ignorable.
							if (log.isDebugEnabled()) log.debug "Ignorable ConcurrentModificationException"
						}
					}
				},0,DEFAULT_TIMER_INTERVAL
				)
	}

	def admit(key, dataToCache){
		CachedObject cobj = (CachedObject) cacheMap.get(key)
		if (!cobj) {
			cacheMap.put(key, new CachedObject(dataToCache))
			return
		}
		def obj = cobj.getCachedData(key)
		if (!obj) {
			if (!dataToCache) {
				cobj.timeCached = cobj.timeAccessedLast = System.currentTimeMillis()
				return
			}
			cacheMap.put(key, new CachedObject(dataToCache))
			return
		} else if (obj.equals(dataToCache)){
			cobj.timeCached = cobj.timeAccessedLast = System.currentTimeMillis()
			return
		}

		cacheMap.put(key, new CachedObject(dataToCache))
		obj
	}
	
	private def loadRss(){
		try {
			def admitions = [:]
			def results = new XmlSlurper().parse(config.euromilhoes.rss.results)
			def prizes = new XmlSlurper().parse(config.euromilhoes.rss.prizes)
			admit(Channels.RESULTS.toString().toLowerCase(), results)
			admit(Channels.PRIZES.toString().toLowerCase(), prizes)
			admitions[Channels.RESULTS.toString().toLowerCase()] = results
			admitions[Channels.PRIZES.toString().toLowerCase()] = prizes
			return admitions
		} catch (Exception e) {
			e.printStackTrace()
			log.warn "Could not parse the rss feed (${e.getMessage()})"
		}
		return
	}

	def getProperty(Channels channel){
		getProperty(channel.name.toLowerCase())
	}

	def getProperty(String name){
		CachedObject cobj = (CachedObject) cacheMap.get(name)
		if (!cobj) return loadRss()?.get(name)
		cobj?.getCachedData(name)
	}

	protected void finalize() throws Throwable {
		cacheManager?.cancel()
	}

}
