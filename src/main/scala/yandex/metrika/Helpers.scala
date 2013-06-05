package yandex.metrika

/**
 * --- YANDEX Global Information ---
 */

object Helpers {
  /* urls for YANDEX METRIKA api */
  val HOST = "https://api-metrika.yandex.ru/"

  val _COUNTERS = "counters"
  val _COUNTER = "counter/%d"
  val _GOALS = _COUNTER + "/goals"
  val _GOAL = _COUNTER + "/goal/%d"
  val _FILTERS = _COUNTER + "/filters"
  val _FILTER = _COUNTER + "/filter/%d"
  val _OPERATIONS = _COUNTER + "/operations"
  val _OPERATION = _COUNTER + "/operation/%d"
  val _GRANTS = _COUNTER + "/grants"
  val _GRANT = _COUNTER + "/grant/%s"
  val _DELEGATES = "delegates"
  val _DELEGATE = "delegate/%s"
  val _ACCOUNTS = "accounts"
  val _ACCOUNT = "account/%s"

  val _STAT = "stat"

  val _STAT_TRAFFIC = _STAT + "/traffic"
  val _STAT_TRAFFIC_SUMMARY = _STAT_TRAFFIC + "/summary"
  val _STAT_TRAFFIC_DEEPNESS = _STAT_TRAFFIC + "/deepness"
  val _STAT_TRAFFIC_HOURLY = _STAT_TRAFFIC + "/hourly"
  val _STAT_TRAFFIC_LOAD = _STAT_TRAFFIC + "/load"

  val _STAT_SOURCES = _STAT + "/sources"
  val _STAT_SOURCES_SUMMARY = _STAT_SOURCES + "/summary"
  val _STAT_SOURCES_SITES = _STAT_SOURCES + "/sites"
  val _STAT_SOURCES_SEARCH_ENGINES = _STAT_SOURCES + "/search_engines"
  val _STAT_SOURCES_PHRASES = _STAT_SOURCES + "/phrases"
  val _STAT_SOURCES_MARKETING = _STAT_SOURCES + "/marketing"
  val _STAT_SOURCES_DIRECT = _STAT_SOURCES + "/direct"
  val _STAT_SOURCES_DIRECT_SUMMARY = _STAT_SOURCES_DIRECT + "/summary"
  val _STAT_SOURCES_DIRECT_PLATFORMS = _STAT_SOURCES_DIRECT + "/platforms"
  val _STAT_SOURCES_DIRECT_REGIONS = _STAT_SOURCES_DIRECT + "/regions"
  val _STAT_SOURCES_TAGS = _STAT_SOURCES + "/tags"

  val _STAT_CONTENT = _STAT + "/content"
  val _STAT_CONTENT_POPULAR = _STAT_CONTENT + "/popular"
  val _STAT_CONTENT_ENTRANCE = _STAT_CONTENT + "/entrance"
  val _STAT_CONTENT_EXIT = _STAT_CONTENT + "/exit"
  val _STAT_CONTENT_TITLES = _STAT_CONTENT + "/titles"
  val _STAT_CONTENT_URL_PARAM = _STAT_CONTENT + "/url_param"
  val _STAT_CONTENT_USER_VARS = _STAT_SOURCES + "/user_vars"
  val _STAT_CONTENT_ECOMMERCE = _STAT_CONTENT + "/ecommerce"

  val _STAT_GEO = _STAT + "/geo"

  val _STAT_DEMOGRAPHY = _STAT + "/demography"
  val _STAT_DEMOGRAPHY_AGE_GENDER = _STAT_DEMOGRAPHY + "/age_gender"
  val _STAT_DEMOGRAPHY_STRUCTURE = _STAT_DEMOGRAPHY + "/structure"

  val _STAT_TECH = _STAT + "/tech"
  val _STAT_TECH_BROWSERS = _STAT_TECH + "/browsers"
  val _STAT_TECH_OS = _STAT_TECH + "/os"
  val _STAT_TECH_DISPLAY = _STAT_TECH + "/display"
  val _STAT_TECH_MOBILE = _STAT_TECH + "/mobile"
  val _STAT_TECH_FLASH = _STAT_TECH + "/flash"
  val _STAT_TECH_SILVERLIGHT = _STAT_TECH + "/silverlight"
  val _STAT_TECH_DOTNET = _STAT_TECH + "/dotnet"
  val _STAT_TECH_JAVA = _STAT_TECH + "/java"
  val _STAT_TECH_COOKIES = _STAT_TECH + "/cookies"
  val _STAT_TECH_JAVASCRIPT = _STAT_TECH + "/javascript"
}

case class OParameters(
  //counters
  var `type`: Option[String] = None,
  var permission: Option[String] = None,
  var ulogin: Option[String] = None,
  var field: Option[String] = None,
  //stats
  var id: Option[Long] = None,
  var mirror_id: Option[String] = None,
  var goal_id: Option[Long] = None,
  var se_id: Option[Long] = None,
  var date1: Option[String] = None, //YYYMMDD
  var date2: Option[String] = None, //YYYMMDD
  var table_mode: Option[String] = None,
  var group: Option[String] = None,
  var per_page: Option[Long] = None,
  var sort: Option[String] = None,
  var reverse: Option[Byte] = None) {

  def toSeq: Seq[(String, String)] = {
    Seq(
      //counters
      ("type" -> `type`.getOrElse("")),
      ("permission" -> permission.getOrElse("")),
      ("ulogin" -> ulogin.getOrElse("")),
      ("field" -> field.getOrElse("")),
      //stats
      ("id" -> id.map(_.toString).getOrElse("")),
      ("mirror_id" -> mirror_id.getOrElse("")),
      ("goal_id" -> goal_id.map(_.toString).getOrElse("")),
      ("se_id" -> se_id.map(_.toString).getOrElse("")),
      ("date1" -> date1.getOrElse("")),
      ("date2" -> date2.getOrElse("")),
      ("table_mode" -> table_mode.getOrElse("")),
      ("group" -> group.getOrElse("")),
      ("per_page" -> per_page.map(_.toString).getOrElse("")),
      ("sort" -> sort.getOrElse("")),
      ("reverse" -> reverse.map(_.toString).getOrElse("")))
      .filter(_._2 != "")
  }
}