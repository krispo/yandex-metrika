package yandex.metrika

import Helpers._

import play.api.libs.json._
import scala.concurrent.{ Future, Await }
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits._
import scala.util._

import play.api.libs.ws._

case class Metrika(
  val login: String,
  val token: String) {

  def call(method: String, url: String, params: OParameters = OParameters(), bodyparams: JsValue = JsNull, field: Option[String] = None): JsValue = {
    val request = WS.url(url).withQueryString(("pretty", "1") +: ("oauth_token", token) +: params.toSeq: _*)
    val fres = method match {
      case "GET" => request.get()
      //case "POST" => request.post[JsValue](bodyparams)
      //case "PUT" => request.put[JsValue](bodyparams)
      //case "DELETE" => request.delete()
    }
    val jsRes = Await.result(fres, Duration.Inf).json

    /* for methods with per_page limit we use non empty field
     * "counters" - for getCounterList
     * "data" - for get Statistics
     */
    field
      .map { f =>
        (jsRes \ "links" \ "next").asOpt[String] map { link =>
          val fullData = (jsRes \ f).as[List[JsValue]] ::: getDataByLink(link, f)
          val data = JsObject(Seq((f, Json.toJson(fullData))))
          (jsRes.as[JsObject] ++ data).as[JsValue]
        } getOrElse (jsRes)
      }
      .getOrElse(jsRes)
  }

  def getDataByLink(link: String, field: String): List[JsValue] = {
    val fres = WS.url(link).get()
    val jsRes = Await.result(fres, Duration.Inf).json
    val data = (jsRes \ field).as[List[JsValue]]
    (jsRes \ "links" \ "next").asOpt[String] map { newlink =>
      data ::: getDataByLink(newlink, field)
    } getOrElse {
      data
    }
  }

  def getURI(method: String): String = {
    "%s%s.json".format(HOST, method)
  }

  /*def getURI(method: String, params: String = ""): String = {
    "%s%s.json?pretty=1&oauth_token=%s&%s".format(HOST, method, token, params)
  }*/

  /**
   * Counters
   */
  def getCounterList(params: OParameters = OParameters()): JsValue = {
    // Returns a list of existing counters available to the user
    val uri = getURI(_COUNTERS)
    call("GET", uri, params, field = Some("counters"))
  }

  def getCounter(counter_id: Long, params: OParameters = OParameters()): JsValue = {
    // Returns information about the specified counter
    val uri = getURI(_COUNTER.format(counter_id))
    call("GET", uri, params)
  }

  /**
   * Goals
   */
  def getCounterGoalList(counter_id: Long, params: OParameters = OParameters()): JsValue = {
    // Returns information about the goals of counter
    val uri = getURI(_GOALS.format(counter_id))
    call("GET", uri, params)
  }

  def getCounterGoal(counter_id: Long, goal_id: Long, params: OParameters = OParameters()): JsValue = {
    // Returns information about the specified goal of counter
    val uri = getURI(_GOAL.format(counter_id, goal_id))
    call("GET", uri, params)
  }

  /**
   * Filters
   */
  def getCounterFilterList(counter_id: Long): JsValue = {
    // Returns information about the filter of counter
    val uri = getURI(_FILTERS.format(counter_id))
    call("GET", uri)
  }

  def getCounterFilter(counter_id: Long, filter_id: Long): JsValue = {
    // Returns information about the specified filter of counter
    val uri = getURI(_FILTER.format(counter_id, filter_id))
    call("GET", uri)
  }

  /**
   * Operations
   */
  def getCounterOperationList(counter_id: Long): JsValue = {
    // Returns information about the operations of counter
    val uri = getURI(_OPERATIONS.format(counter_id))
    call("GET", uri)
  }

  def getCounterOperation(counter_id: Long, operation_id: Long): JsValue = {
    // Returns information about the specified operation of counter
    val uri = getURI(_OPERATION.format(counter_id, operation_id))
    call("GET", uri)
  }

  /**
   * Grants
   */
  def getCounterGrantList(counter_id: Long): JsValue = {
    // Returns information about the permissions to manage the counter and statistics
    val uri = getURI(_GRANTS.format(counter_id))
    call("GET", uri)
  }

  def getCounterGrant(counter_id: Long, user_login: String): JsValue = {
    // Returns information about a specific permit to control the counter and statistics
    val uri = getURI(_GRANT.format(counter_id, user_login))
    call("GET", uri)
  }

  /**
   * Delegates
   */
  def getDelegates: JsValue = {
    // Returns list of delegates who have been granted full access to the account of the current user
    val uri = getURI(_DELEGATES)
    call("GET", uri)
  }

  /**
   * Accounts
   */
  def getAccounts: JsValue = {
    // Returns a list of accounts, the delegate of which is the current user
    val uri = getURI(_ACCOUNTS)
    call("GET", uri)
  }

  /**
   * Statistics
   */

  /** Statistics Traffic **/
  def getStatTrafficSummary(params: OParameters = OParameters()): JsValue = {
    // Returns data about traffic of site
    val uri = getURI(_STAT_TRAFFIC_SUMMARY)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatTrafficDeepness(params: OParameters = OParameters()): JsValue = {
    // Returns data on the number of pages viewed and time visitors spent on the site
    val uri = getURI(_STAT_TRAFFIC_DEEPNESS)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatTrafficHourly(params: OParameters = OParameters()): JsValue = {
    // Returns data on the distribution of traffic on the site by time of day, for each hourly of period
    val uri = getURI(_STAT_TRAFFIC_HOURLY)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatTrafficLoad(params: OParameters = OParameters()): JsValue = {
    /* Returns the maximum number of requests (alarms counter) per second
       and the maximum number of online visitors each day selected
       time period */
    val uri = getURI(_STAT_TRAFFIC_LOAD)
    call("GET", uri, params, field = Some("data"))
  }

  /** Statistics Sources **/
  def getStatSourcesSummary(params: OParameters = OParameters()): JsValue = {
    // Returns the conversion data from all sources on the site, where installed the specified counter
    val uri = getURI(_STAT_SOURCES_SUMMARY)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatSourcesSites(params: OParameters = OParameters()): JsValue = {
    // Returns the conversion data from other sites on the web site, where installed the specified counter
    val uri = getURI(_STAT_SOURCES_SITES)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatSourcesSearchEngines(params: OParameters = OParameters()): JsValue = {
    // Returns the conversion data from the search engine's website, where installed the specified counter
    val uri = getURI(_STAT_SOURCES_SEARCH_ENGINES)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatSourcesPhrases(params: OParameters = OParameters()): JsValue = {
    // Returns information about the search phrases that visitors find link to the site with installed a counter
    val uri = getURI(_STAT_SOURCES_PHRASES)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatSourcesMarketing(params: OParameters = OParameters()): JsValue = {
    // Returns the conversion data from the advertising system on the site, where installed the specified counter
    val uri = getURI(_STAT_SOURCES_MARKETING)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatSourcesDirectSummary(params: OParameters = OParameters()): JsValue = {
    // Returns record of advertising campaigns Yandex.Direct, ad which visitors to a site
    val uri = getURI(_STAT_SOURCES_DIRECT_SUMMARY)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatSourcesDirectPlatforms(params: OParameters = OParameters()): JsValue = {
    // Returns a report on areas with which the transition through advertisements on the advertiser's site
    val uri = getURI(_STAT_SOURCES_DIRECT_PLATFORMS)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatSourcesDirectRegions(params: OParameters = OParameters()): JsValue = {
    /* Returns information about membership of visitors who clicked on the site 
	   through advertisements to a particular geographical region */
    val uri = getURI(_STAT_SOURCES_DIRECT_REGIONS)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatSourcesTags(params: OParameters = OParameters()): JsValue = {
    /* Returns information about visits to a site on the links, which
       contain any of the four most frequently used tags: utm, openstat, from, glcid */
    val uri = getURI(_STAT_SOURCES_TAGS)
    call("GET", uri, params, field = Some("data"))
  }

  /** Statistics Content **/
  def getStatContentPopular(params: OParameters = OParameters()): JsValue = {
    // Returns attendance rating web pages in descending order of display
    val uri = getURI(_STAT_CONTENT_POPULAR)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatContentEntrance(params: OParameters = OParameters()): JsValue = {
    // Returns information about entry points to the site (the first pages of visits)
    val uri = getURI(_STAT_CONTENT_ENTRANCE)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatContentExit(params: OParameters = OParameters()): JsValue = {
    // Returns information about exits from the site (the last pages of visits)
    val uri = getURI(_STAT_CONTENT_EXIT)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatContentTitles(params: OParameters = OParameters()): JsValue = {
    // Returns the rating of attendance page of the site showing their titles (from the tag title)
    val uri = getURI(_STAT_CONTENT_TITLES)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatContentUrlParam(params: OParameters = OParameters()): JsValue = {
    // Returns data about the parameters mentioned Metrika in the URL visited site pages
    val uri = getURI(_STAT_CONTENT_URL_PARAM)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatContentUserVars(params: OParameters = OParameters()): JsValue = {
    // Returns information about the user parameters, passed through the counter code
    val uri = getURI(_STAT_CONTENT_USER_VARS)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatContentECommerce(params: OParameters = OParameters()): JsValue = {
    // Returns information about ordering from an online store, passed through the counter code
    val uri = getURI(_STAT_CONTENT_ECOMMERCE)
    call("GET", uri, params, field = Some("data"))
  }

  /** Statistics Geography **/
  def getStatGeo(params: OParameters = OParameters()): JsValue = {
    /* Returns information about users belonging to the geographical
       regions. List of regions can be grouped by regions, countries and continents */
    val uri = getURI(_STAT_GEO)
    call("GET", uri, params, field = Some("data"))
  }

  /** Statistics Demography **/
  def getStatDemographyAgeGender(params: OParameters = OParameters()): JsValue = {
    // Returns the data separately by sex and age of visitors
    val uri = getURI(_STAT_DEMOGRAPHY_AGE_GENDER)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatDemographyStructure(params: OParameters = OParameters()): JsValue = {
    // Returns merged data by sex and age
    val uri = getURI(_STAT_DEMOGRAPHY_STRUCTURE)
    call("GET", uri, params, field = Some("data"))
  }

  /** Statistics Tech **/
  def getStatTechBrowsers(params: OParameters = OParameters()): JsValue = {
    // Returns data about the visitor's browser
    val uri = getURI(_STAT_TECH_BROWSERS)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatTechOs(params: OParameters = OParameters()): JsValue = {
    // Returns data about the operating systems of visitors
    val uri = getURI(_STAT_TECH_OS)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatTechDisplay(params: OParameters = OParameters()): JsValue = {
    // Returns data on the display resolution of site visitors
    val uri = getURI(_STAT_TECH_DISPLAY)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatTechMobile(params: OParameters = OParameters()): JsValue = {
    // Returns data about visitors who come to the site from mobile devices
    val uri = getURI(_STAT_TECH_MOBILE)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatTechFlash(params: OParameters = OParameters()): JsValue = {
    // Returns data about the versions of Flash-plugin on visitors' computers
    val uri = getURI(_STAT_TECH_FLASH)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatTechSilverlight(params: OParameters = OParameters()): JsValue = {
    // Returns data on the distribution of different versions of plugin Silverlight
    val uri = getURI(_STAT_TECH_SILVERLIGHT)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatTechDotNet(params: OParameters = OParameters()): JsValue = {
    // Returns version information .NET framework on visitors' computers
    val uri = getURI(_STAT_TECH_DOTNET)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatTechJava(params: OParameters = OParameters()): JsValue = {
    // Returns data on the availability of the Java platform on visitors' computers
    val uri = getURI(_STAT_TECH_JAVA)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatTechCookies(params: OParameters = OParameters()): JsValue = {
    // Returns data about visits visitors with disabled Cookies
    val uri = getURI(_STAT_TECH_COOKIES)
    call("GET", uri, params, field = Some("data"))
  }
  def getStatTechJavascript(params: OParameters = OParameters()): JsValue = {
    // Returns data about visits visitors with disabled JavaScript (ECMAScript)
    val uri = getURI(_STAT_TECH_JAVASCRIPT)
    call("GET", uri, params, field = Some("data"))
  }
}