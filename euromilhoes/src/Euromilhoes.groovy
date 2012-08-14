import pt.santacasa.euromilhoes.Generator

g = new Generator()
println g.newKey()
k = g.key.numbers.first()
println "V: ${k.getVolume()}" 
println "M: ${k.getMass()}" 
println "W: ${k.getWeight()}"