import yandex.metrika._

object Demo {

  def main(args: Array[String]) {
    val login = "api-metrika2"
    val token = "05dd3dd84ff948fdae2bc4fb91f13e22"

    // Initialize Metrika class with specified login and token
    val metrika = Metrika(login, token)

    // Get counter list with default parameters
    val counters = metrika.getCounterList()

    // Specify some of the optional parameters
    val params = OParameters(
      `type` = Some("simple"),
      permission = Some("own"),
      field = Some("goals,mirrors,grants"))

    val new_counters = metrika.getCounterList(params)

    // Get statistics for phrases
    //This method require "id" parameter
    val statParams = OParameters(id = Some(2138128)) //fake id parameter for Yandex
    val statsDirect = metrika.getStatSourcesPhrases(statParams)
  }
}