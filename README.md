# Open mHealth DPU (Data Process Unit) Implementation

The Open mHealth engine has been developed with the support of DPU, following the [Scenario 1](https://github.com/openmhealth/developer/wiki/DPU-API-1.0-Beta#scenario-1)
 
## Table of Contents

* Process API Call
* Registry API Call
* Some example calls to the blood pressure calculator
* Note on compiling the server with Maven
* Next steps

## Process API Call

This is the main worker call into a DPU. Process receives data that belongs to one schema ID-version pair and returns data that belongs to another schema ID-version pair. There are cases where the input and output schema ID-version pairs could be the same.

The process endpoint must include support for a parameter that instructs it to preserve the original raw input data. 
(in the current version is not implemented)

### Request

The request URL has one path.

* The path to send some data to be processed is: `dpu/{processName}`. 
    * This will return the result of the application of `{processName}` to the input data.
* The HTTP method is a POST
	* The payload follows the schema id and version described by the readRegistry operation
	 
### Response

In case of success, the response is a JSON object following the schema id and version described by the readRegistry operation.
In case of error, the response has a 500 status code and is a JSON object following the omh:dpu:SystemException and  omh:dpu:BusinessException schema.


## Registry API Call

Registry functions much like the DSU registry. It returns the schema ID-version pairs that can be processed and the schema-ID version pairs that are returned.


### Request

The request URL has one path.

* The path to send some data to be processed is: `dpu/{processName}`. 
    * This will return the result of the application of `{processName}` to the input data.
* The HTTP method is a GET
	* The result is the schema ID-version pairs that can be processed and the schema-ID version pairs that are returned.


### Response

In case of success, the response is a JSON object containing the schema ID-version pairs that can be processed and the schema-ID version pairs that are returned by the corresponding process method.
In case of error, the response has a 500 status code and is a JSON object following the omh:dpu:SystemException and  omh:dpu:BusinessException schema.



## Some example Calls to the blood pressure calculator
A DPU that calculates the blood pressure categories as described in [an example](http://www.heart.org/HEARTORG/Conditions/HighBloodPressure/AboutHighBloodPressure/Understanding-Blood-Pressure-Readings_UCM_301764_Article.jsp) has been developed.

The following are some example calls along with the result produced by the DPU engine

### BloodPressure DPU process call
Right request: 
curl -v http://localhost:8080/omh-engine-1.0.0/dpu/bloodPressure -X POST -H "Content-Type: application/json" \
-d '{"systolic":100, "diastolic":99}' ; echo ""

**Example HTTP request header**

	POST /omh-engine-1.0.0/dpu/bloodPressure HTTP/1.1
	User-Agent: curl/7.30.0
	Host: localhost:8080
	Accept: */*
	Content-Type: application/json
	Content-Length: 32

**Example HTTP response header (from a tomcat servlet engine)**

	HTTP/1.1 200 OK
	Server Apache-Coyote/1.1 is not blacklisted
	Server: Apache-Coyote/1.1
	Content-Type: text/plain;charset=ISO-8859-1
	Content-Length: 41

**Example HTTP response content**

	{"category":"high_blood_pressure_stage1"}

Wrong request (triggering a json not well formed error):
curl -v http://localhost:8080/omh-engine-1.0.0/dpu/bloodPressure -X POST -H "Content-Type: application/json" \
-d '{"systolic":100, "diastolic":99' ; echo ""

**Example HTTP response content**

	{"BusinessException":
		{"code":"json not well formed"},
		{"message":"the json input is not well formed: {"systolic":100, "diastolic":99"}}

### BloodPressure DPU readRegistry call
curl -v http://localhost:8080/omh-engine-1.0.0/dpu/bloodPressure ; echo ""

**Example HTTP request header**

	GET /omh-engine-1.0.0/dpu/bloodPressure HTTP/1.1
	User-Agent: curl/7.30.0
	Host: localhost:8080
	Accept: */*

**Example HTTP response header (from a tomcat servlet engine)**

	HTTP/1.1 200 OK
	Server Apache-Coyote/1.1 is not blacklisted
	Server: Apache-Coyote/1.1
	Content-Type: application/json;charset=UTF-8

**Example HTTP response content**

	[{"schemaId":"omh:dpu:bloodpressure",
		"schemaVersion":"1"},
	{"schemaId":"omh:dpu:bloodpressure",
	"schemaVersion":"1"}]


## Note on compiling the server with Maven
The project has a maven pom that lists the library dependencies.

###Using Lombok
The [lombok library](http://projectlombok.org/) is used because it saves to write a lot of boilerplate code.
The following [instructions](http://projectlombok.org/download.html) must be followed to install it in the IDe of choice.

### Using Concordia
If in the next steps the [Concordia library](https://github.com/jojenki/Concordia/wiki) will be used, in must be downloaded and compiled and installed in the local maven repository

	
	mvn install:install-file -DgroupId=name.jenkins.paul.john -DartifactId=concordia -Dversion=1.2.4 -Dpackaging=jar -DgeneratePom=true -Dfile=


## Next Steps
* write the documentation
	* ppt presentation
	* UML diagram
	
* BaseException: a possible extension could be to customize the status, depending on the error code













