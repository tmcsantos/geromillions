import miscellaneous.Environment
import miscellaneous.Temperature;
import pt.santacasa.euromilhoes.Extractor
import pt.santacasa.euromilhoes.Polynomial

g = new Extractor()
g.setTemperature(Temperature.SUMMER)
k = g.randomKey()
d = Calendar.instance
d.set(2012,8,24,21,0,0)
exk = g.extraction(5, 2, d)
println "Random key: ${k}"
println "Extraction key: ${exk}"