import miscellaneous.Environment
import miscellaneous.Temperature
import pt.santacasa.euromilhoes.Extractor
import pt.santacasa.euromilhoes.Polynomial

g = new Extractor()
g.setTemperature(Temperature.WINTER)
d = Calendar.instance
d.set(2012,10,26,21,0)
exk = g.extraction(5, 2, d)
println "Extraction key: ${exk}"