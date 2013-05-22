package api

import org.specs2.mutable._
import org.specs2.specification._

import play.api.libs.json._

class MetrikaSpec extends Specification with AllExpectations {

  //initialize Metrika with FAKE parameters
  var user_login = "api-metrika2" //fake login
  var token = "05dd3dd84ff948fdae2bc4fb91f13e22" //fake token
  var counter_id = 2215573 //fake counter id
  var goal_id = 334423 //fake goal_id
  var filter_id = 66943 //fake filter_id
  var operation_id = 66955 //fake operation_id
  var id = 2138128 //fake id

  val m = Metrika(user_login, token)

  /**
   * Counters
   */

  "getCounterList" should {
    sequential

    "send TRUE request" in {
      val res = m.getCounterList()
      res \ "rows" must_== (JsNumber(2))
      res \\ "code_status" map (_.as[String]) must_== (List("CS_NOT_FOUND", "CS_NOT_FOUND_HOME_LOAD_DATA"))
      res \\ "id" map (_.as[String]) must_== (List("2215573", "2138128"))
    }
  }

  "getCounter" should {
    sequential

    "send TRUE request" in {
      val res = m.getCounter(counter_id)
      val c = res \ "counter"
      c \ "code_status" must_== (JsString("CS_NOT_FOUND"))
      c \ "permission" must_== (JsString("own"))
      (c \ "monitoring" \ "emails").as[List[String]] must_== (List("api-metrika@yandex.ru"))
    }
  }

  /**
   * Goals
   */

  "getCounterGoalList" should {
    sequential

    "send TRUE request" in {
      val res = m.getCounterGoalList(counter_id)

      res \ "goal" must_!= (JsNull)
      (res \\ "detailed_statistics").length must_== (4)
      res \\ "id" map (_.as[String]) must_== (List("334420", "334423", "334426", "334429"))
    }
  }

  "getCounterGoal" should {
    sequential

    "send TRUE request" in {
      val res = m.getCounterGoal(counter_id, goal_id)

      res \ "goal" must_!= (JsNull)
      res \ "goal" \ "detailed_statistics" must_== (JsNumber(1))
      res \ "goal" \ "flag" must_== (JsString("basket"))
      res \ "goal" \ "id" must_== (JsString("334423"))
    }
  }

  /**
   * Filters
   */

  "getCounterFilterList" should {
    sequential

    "send TRUE request" in {
      val res = m.getCounterFilterList(counter_id)

      res \ "filters" must_!= (JsNull)
      (res \\ "status").length must_== (6)
      res \\ "id" map (_.as[String]) must_== (List("66940", "66928", "66943", "66946", "66949", "66952"))
    }
  }

  "getCounterFilter" should {
    sequential

    "send TRUE request" in {
      val res = m.getCounterFilter(counter_id, filter_id)

      res \ "filter" must_!= (JsNull)
      res \ "filter" \ "status" must_== (JsString("active"))
      res \ "filter" \ "end_ip" must_== (JsString("192.168.0.255"))
      res \ "filter" \ "id" must_== (JsString("66943"))
    }
  }

  /**
   * Operations
   */

  "getCounterOperationList" should {
    sequential

    "send TRUE request" in {
      val res = m.getCounterOperationList(counter_id)

      res \ "operations" must_!= (JsNull)
      (res \\ "status").length must_== (2)
      res \\ "id" map (_.as[String]) must_== (List("66955", "66958"))
    }
  }

  "getCounterOperation" should {
    sequential

    "send TRUE request" in {
      val res = m.getCounterOperation(counter_id, operation_id)

      res \ "operation" must_!= (JsNull)
      res \ "operation" \ "status" must_== (JsString("active"))
      res \ "operation" \ "value" must_== (JsString("debug"))
      res \ "operation" \ "id" must_== (JsString("66955"))
    }
  }

  /**
   * Grants
   */

  "getCounterGrantList" should {
    sequential

    "send TRUE request" in {
      val res = m.getCounterGrantList(counter_id)

      res \ "grants" must_!= (JsNull)
      (res \\ "user_login").length must_== (2)
      res \\ "perm" map (_.as[String]) must_== (List("public_stat", "view"))
    }
  }

  "getCounterGrant" should {
    sequential

    "send TRUE request" in {
      val res = m.getCounterGrant(counter_id, user_login)

      res \ "grant" must_!= (JsNull)
      res \ "grant" \ "user_login" must_== (JsString("api-metrika2"))
      res \ "grant" \ "created_at" must_== (JsString("2010-12-08 20:02:01"))
      res \ "grant" \ "perm" must_== (JsString("view"))
    }
  }

  /**
   * Delegates
   */

  "getDelegates" should {
    sequential

    "send TRUE request" in {
      val res = m.getDelegates

      res \ "delegates" must_!= (JsNull)
      (res \\ "user_login").length must_== (1)
      res \\ "created_at" map (_.as[String]) must_== (List("2010-12-08 19:33:00"))
    }
  }

  /**
   * Accounts
   */

  "getAccounts" should {
    sequential

    "send TRUE request" in {
      val res = m.getAccounts

      res \ "accounts" must_!= (JsNull)
      (res \\ "user_login").length must_== (1)
      res \\ "created_at" map (_.as[String]) must_== (List("2010-12-08 19:32:03"))
    }
  }

  /**
   * Statistics
   */

  /** Statistics Traffic **/

  "getStatTrafficSummary" should {
    sequential

    "send TRUE request" in {
      val res = m.getStatTrafficSummary(OParameters(id = Some(id)))

      res \ "id" must_== (JsString("2138128"))
      res \ "rows" must_== (JsNumber(7))
      res \ "date1" must_!= (JsNull)
      res \ "date2" must_!= (JsNull)
      res \ "totals" \ "visits" must_!= (JsNull)
      val data = (res \ "data").as[List[JsValue]]
      data.length must_== (7)
      data.head \ "denial" must_!= (JsNull)
    }
  }

  /** Statistics Sources **/

  "getStatSourcesPhrases" should {
    sequential

    "send TRUE request" in {
      val res = m.getStatSourcesPhrases(OParameters(id = Some(id)))

      res \ "id" must_== (JsString("2138128"))
      res \ "rows" must_!= (JsNull)
      res \ "date1" must_!= (JsNull)
      res \ "date2" must_!= (JsNull)
      res \ "totals" \ "visits" must_!= (JsNull)
      val data = (res \ "data").as[List[JsValue]]
      data.length must_== (100)
      data.head \ "denial" must_!= (JsNull)
      data.head \ "id" must_== (JsString("1097347136981725315"))
      data.head \ "phrase" must_!= (JsNull)
    }
  }

  /** Statistics Content **/

  /** Statistics Geography **/

  /** Statistics Demography **/

  /** Statistics Tech **/
}