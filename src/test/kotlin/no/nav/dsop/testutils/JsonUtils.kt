package no.nav.dsop.testutils

fun readJsonFile(name: String): String {
    return {}.javaClass.getResource(name)!!.readText()
}
