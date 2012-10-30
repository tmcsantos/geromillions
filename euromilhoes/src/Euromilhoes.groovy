import java.text.DateFormat
import java.text.SimpleDateFormat

import miscellaneous.Environment
import miscellaneous.Temperature
import pt.santacasa.euromilhoes.EuromilhoesProperties
import pt.santacasa.euromilhoes.Extractor
import pt.santacasa.euromilhoes.Polynomial
import pt.santacasa.euromilhoes.rss.RssFeed

def init(args) {
	def cli = new CliBuilder(usage: 'Euromilhoes -[ht] [date]')
	cli.with {
		h longOpt: 'help', 'Show usage information'
		t longOpt: 'temperature', args: 1, argName: 'temperature','Set the room temperature [winter|summer] default: winter'
	}
	def options = cli.parse(args)
	if (!options) return
		if (options.h) {
			cli.usage()
			return ''
		}

	def temperature = Temperature.WINTER
	if (options.'temperature'){
		switch(options.t.toLowerCase()){
			case 'summer': temperature=Temperature.SUMMER
		}
	}

	def date = Calendar.getInstance() // Default is current date
	def extraArguments = options.arguments()
	if (extraArguments) {
		def parsedDate= Date.parse("yyyy/MM/dd",extraArguments[0])
		date.set(
				parsedDate.getAt(Calendar.YEAR),
				parsedDate.getAt(Calendar.MONTH),
				parsedDate.getAt(Calendar.DATE),
				20,
				41,
				40
				)
	}

	def extractor = new Extractor()
	extractor.setTemperature(temperature)
	"Date: ${date.getTime()}\nRoom temperature: ${temperature}\nExtraction: ${extractor.extraction(5, 2, date)}"
}

Locale.setDefault(Locale.US)

RssFeed feed = new RssFeed()
println feed.results.channel.item.find {it.title =~ 'Euromilhões' }.pubDate
println feed.results.channel.item.find {it.title =~ 'Euromilhões' }.description
println ""

//println init(args)
println init(['-t','winter','2012/10/30'])
