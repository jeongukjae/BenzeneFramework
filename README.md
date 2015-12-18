# Benzene Framework

## What is Benzene Framework

Benzene framework is micro web framework written in java.

## Features

* Simple Routing
* Start Server with very few code

## Requirements

* Java 1.7+

## file structure

* BenzeneFramework - Framework module
* Jade4Benzene     - View engine module (jade)
* BenzeneTest      - test code

## Usage

### Start New Server

``` java
/* Make New Object for Server */
Benzene benzene  = new Benzene();

/* Set Controller(Router) */
benzene.setController(MainRouter.class);

/* add static file root path */
/* You can set multiple static file root path */
benzene.addPublic("public");
/* You can use this code instead of above code */
/* benzene.set("public", "public"); */

/* Set view root path */
benzene.set("view", "views");

/* Set View engine to Jade4Benzene */
/* View engine class have to be implemented */
/* BenzeneLibrary interface and BenzeneRenderer interface */
benzene.set("view engine", Jade4Benzene.class);

/* Start Server */
benzene.startServer();
/* Start Server With Port */
/* benzene.startServer(8800); */
/* You can set port before starting server using setPort(int) */
/* benzene.setPort(8800); */
```

### Routing

``` java
public class MainRouter {
	@Route(route="/")
	public static void index(Request request, Response response) {
		/* You can get parameter */
		/* request.get("param_name"); */
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("smile", "haha");
		response.render("smile.jade", params);
	}
}
```

###Cookie

``` java
@Route(route="/")
public static void index(Request request, Response response) {
	/* Set-Cookie */
	response.cookie("cookieKey", "cookieValue");
	/* Expire Cookie */
	response.expireCookie("cookieKey2");
	response.render("smile.jade");
}
```


## License

MIT License

Copyright (c) 2015 JeongUkJae, ParkJuChan

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

##Libraries

* [jade4j](https://github.com/neuland/jade4j) - MIT License
* [apache commons logging](https://commons.apache.org/proper/commons-logging/) - Apache License
* [apache commons jexl](https://commons.apache.org/proper/commons-jexl/) - Apache License
* [apache commons lang3](https://commons.apache.org/proper/commons-lang/) - Apache License

## Contributor

* JeongUkJae : JeongUkJae@gmail.com
* ParkJuChan : dhr05074@gmail.com

Thank you for using this framework!
We are student developer,
And this framework is our first open source project.