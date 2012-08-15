import miscellaneous.Environment
import pt.santacasa.euromilhoes.Extractor

g = new Extractor()
println "Random key: ${g.randomKey()}"
env = g.getEnvironment()
