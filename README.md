# Yandex.Metrika

Scala client wrapper for [Yandex.Metrika](http://api.yandex.com/metrika/doc/ref/concepts/About.xml) API. This library provides easy-to-use wrap to work with counters, goals, filters, operations, grants, delegates, accounts and statistics.

It is a beta version now. The library supports only `GET` method and `JSON` format.

Sing in via login (Your Yandex account) and token (acccess key for Your application).

It is assumed token is known by one of [those](http://api.yandex.com/oauth/doc/dg/reference/obtain-access-token.xml) ways.

## Dependency

For SBT, add these lines to Your SBT project definition:

```scala
libraryDependencies ++= Seq(
				// other dependencies here
				"com.github.krispo" %% "yandex-metrika" % "0.3-SNAPSHOT"
				)				 
```
and
```scala
resolvers ++= Seq(
		// other resolvers here
		"Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
		)
```

## Example

At first, import metrika api library

```scala
import yandex.metrika._
```

Then we can initialize new Metrika class with specified yandex fake login and token:

```scala
val login = "api-metrika2"
val token = "05dd3dd84ff948fdae2bc4fb91f13e22"

val metrika = Metrika(login, token)
```  

Now we can do simple requests for getting data from Yandex.Metrika. 
Just get counter list:

```scala
val counters = metrika.getCounterList()
```

The result for `counters` is in JSON format
```scala
{	
	"rows":2,
	"counters":[{
		"code_status":"CS_NOT_FOUND",
		"name":"Демо доступ к API Метрики",
		"permission":"own",
		"site":"api.yandex.ru",
		"type":"simple",
		"id":"2215573",
		"owner_login":"api-metrika"
		},{
		"code_status":"CS_NOT_FOUND_HOME_LOAD_DATA",
		"name":"Метрика (help.yandex.ru/metrika/)",
		"permission":"view",
		"site":"help.yandex.ru",
		"type":"simple","id":"2138128",
		"owner_login":"help-metrika"
		}]
}
```

Now we add some optional parameters by filling the `OParameters` case class structure. Ones optional elements have an `Option[_]` wrapper and meet the requirements for Yandex.Metrika type.
Get counter list again:

```scala
val params = OParameters(
	`type` = Some("simple"),
    permission = Some("own"),
    field = Some("goals,mirrors,grants"))

val new_counters = metrika.getCounterList(params)
```

The result for `new_counters` is in JSON format:
```scala
{
	"rows":2,
	"counters":[{
		"code_status":"CS_NOT_FOUND",	
		"name":"Демо доступ к API Метрики",
		"permission":"own",
		"site":"api.yandex.ru",
		"mirrors":["api-metrika.yandex.ru"],
		"id":"2215573",
		"type":"simple",
		"goals":[{
			"name":"Хорошо просмотрел сайт",
			"flag":"",
			"type":"number",
			"id":"334420",
			"depth":8
			},{
			"name":"Корзина",
			"flag":"basket",
			"type":"url",
			"id":"334423",
			"depth":0,
			"conditions":[{
				"url":"/basket.php?add",
				"type":"contain"
				}]
			},{
			"name":"Заказ",
			"flag":"order",
			"type":"url",
			"id":"334426",
			"depth":0,
			"conditions":[{
				"url":"/order.php?ok=1",
				"type":"contain"}]
			},{
			"name":"Контакты",
			"flag":"",
			"type":"url",
			"id":"334429",
			"depth":0,
			"conditions":[{
				"url":"/contacts.php",
				"type":"contain"}]
			}],
		"owner_login":"api-metrika"
		}]
}
```

To get statistics, for example for source phrases, we need to specify at least required parameter "id", and then call corresponding method:

```scala
val statParams = OParameters(id = Some(2138128)) //fake id parameter for Yandex
val statsDirect = metrika.getStatSourcesPhrases(statParams)
``` 

The result for statistics in JSON format You can see at [here](http://api-metrika.yandex.ru/stat/sources/phrases.json?id=2138128&pretty=1&oauth_token=05dd3dd84ff948fdae2bc4fb91f13e22).

Show the [source code](https://github.com/krispo/yandex-metrika/tree/master/src/main/scala/Demo.scala) of this simple example.

## License

This library is under MIT license. Check the [LICENSE](https://github.com/krispo/yandex-metrika/blob/master/LICENSE) file.