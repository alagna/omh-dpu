# Open mHealth DPU (Data Process Unit) Implementation
<a name="top"></a>

* <a href="#process">Process API Call</a>
* <a href="#registry">Registry API Call</a>

## Table of Contents

* <a href="#process">Process API Call</a>
* <a href="#registry">Registry API Call</a>

## <a name="process"></a>Process API Call

This is the main worker call into a DPU. Process receives data that belongs to one schema ID-version pair and returns data that belongs to another schema ID-version pair. There are cases where the input and output schema ID-version pairs could be the same.

Parameters and further documentation are forthcoming.

The process endpoint must include support for a parameter that instructs it to preserve the original raw input data. 

<a href="#top">⇡ top</a>

## <a name="registry"></a>Registry API Call

Registry functions much like the DSU registry. It returns the schema ID-version pairs that can be processed and the schema-ID version pairs that are returned.

Parameters and further documentation are forthcoming.

<a href="#top">⇡ top</a>



-----------------
<a name="top"></a>

**Note that this is a beta specification and it will change.**


* <a href="#exampleScenarios">Example Scenarios</a>
* <a href="#process">Process API Call</a>
* <a href="#registry">Registry API Call</a>

## <a name="exampleScenarios">Example Scenarios</a>

### Scenario 1
![DPU Scenario 1](https://web.ohmage.org/~selsky/dpu_scenario_1.jpg)

A client application reads data from a DSU and then passes the resulting data to a DPU for processing.

_Pros_
* DSUs and DPUs are completely decoupled 
* The API that a DPU would have to support is simple: a client provides an array of data points to process and the DPU returns a result (or results).

_Cons_
* Relying on a client application to push data around the network is dangerous because the amount of bandwidth could be constrained. (Is this just premature optimization though?)
* Cross-domain issues.

### Scenario 2
![DPU Scenario 2](https://web.ohmage.org/~selsky/dpu_scenario_2.JPG)

A client application invokes a DPU by telling it which DSU to read data from. 

_Pros_
* Server-to-server communication between a DPU and DSU has less potential to suffer from the network latency problem in Scenario 1.

_Cons_
* This has a bad design smell because it seems like DSUs should be invoking DPUs, not the other way around.
* Also, the authorization token has to be provided to the DPU, which is a security risk.

### Scenario 3
![DPU Scenario 3](https://web.ohmage.org/~selsky/dpu_scenario_3.JPG)

As above in Scenario 2 with the addition of a DPU chain.

Pros and cons as above.

### Scenario 4
![DPU Scenario 4](https://web.ohmage.org/~selsky/dpu_scenario_4.JPG)

A client app invokes the Read API call on a DSU and provides a list of DPU URLs to the DSU. The DSU then supplies the first DPU in the chain with the read results. The first DPU then passes its output to the next DPU in the chain.

_Pros_
* This flips the order of Scenario 3 by putting the DSU in the center of the action. Since the DSU "owns" the data and the client is authenticated by the DSU, this has a better security model. There is no need to have DPUs toss auth tokens around. One could imagine that a DSU could also whitelist the DPUs it prefers to interact with.

_Cons_
* Passing a list of DPU URLs to DSUs is not very elegant. Perhaps there should be a way for DPUs to have nicknames that can be served up from a given DSU's registry.
* Highly inefficient as DSUs are tied down while they wait for DPUs.
* Adds complexity to the DSU specification.
* Having the first DPU call the second DPU is brittle because it relies on the output of the first to be the correct input for the second.

### Scenario 5
![DPU Scenario 5](https://web.ohmage.org/~selsky/dpu_scenario_5.JPG)

Like Scenario 4, but decouples DPUs from each other and relies on the DSU to be the choreographer of the DPU chain. 

_Pros_
* Most decoupled scenario of them all. Clients don't need to deal with the mechanics of interfacing with DPUs; DPUs in a chain don't need a next parameter; auth stays between clients and DSUs. 

_Cons_
* Adds further complexity to the DSU specification.
* More inefficient than 4 because, on top of waiting for the DPUs, the DSU must also incur additional network latency.

<a href="#top">⇡ top</a>







-------------------



## <a name="top">Table of Contents</a>

* <a href="#authentication">Authentication</a>
* <a href="#authorization">Authorization</a>
* <a href="#registryRead">Registry Read</a>
* <a href="#read">Read</a>
* <a href="#write">Write</a>

## <a name="authentication">Authentication</a>

A DSU is free to implement its own form of user-facing authentication. Open mHealth does not prescribe authentication or access rules for a given DSU because each DSU may have its own access policy and user roles.

<a href="#top">⇡ top</a>

## <a name="authorization">Authorization</a>

Third-party authorization allows an authenticated user to authorize a third-party request for data.

The third-party authorization protocol that Open mHealth uses is OAuth 2.0. Please see the [OAuth 2.0 Protocol specification](http://oauth.net/) for more information.

All OAuth APIs reside under the path `omh/v1/auth/oauth`.

<a href="#top">⇡ top</a>

## <a name="registryRead">Registry Read</a>

### Request

The registry provides the Concordia definitions for data payloads that are served up from a DSU. The only allowed HTTP method is GET.

The request URL has three paths.

* The path to get all schema IDs from a DSU is: `omh/v1`. 
    * This will return a JSON Array of strings that are Open mHealth schema IDs.
    * `["omh:example:steps", "omh:example:velocity", "omh:example:mets"]`
* The path to get all schema versions for a specific schema ID is: `omh/v1/<schema_id>`. 
    * This will return a JSON Array of numbers that are schema versions for the provided schema ID.
    * `[1,2]`
* The path to get a schema for a specific ID-version pair is: `omh/v1/<schema_id>/<schema_version>`. 
    * This will return the Concordia schema (a JSON Object) that defines the data referenced by the schema ID-version pair.
    * The meta-data about the schema, if any, will be added as headers.
    * See below for an example.

#### Parameters

<table>
  <tr font="bold">
    <th>Name</th>
    <th>Description</th>
    <th>Required</th>
  </tr>
  <tr>
    <td>num_to_skip</td>
    <td>Skips forward the number of registry entries. If this parameter is not provided, the default of zero is used.</td>
    <td>no</td>
  </tr>
  <tr>
    <td>num_to_return</td>
    <td>Overrides the default page size. If it is greater than the default, the request may be rejected or the server may limit the number of entries to the default. If this parameter is not provided, the default value is the maximum page size, which is 100.</td>
    <td>no</td>
  </tr>
</table>

### Response

This is the description of the results when both an ID and version are given.

This will always return exactly one result. When no result exists (e.g. the ID and/or version do not exist), a HTTP 404 status code will be returned.

The response is a JSON object that is the definition.

**Example HTTP request header**

     GET /app/omh/v1/omh:example:steps/1 HTTP/1.1
     User-Agent: curl/7.24.0 (x86_64-apple-darwin12.0) libcurl/7.24.0 OpenSSL/0.9.8r zlib/1.2.5
     Host: example.com
     Content-Type: application/x-www-form-urlencoded

**Example HTTP response header**

     HTTP/1.1 200 OK
     Server: Apache-Coyote/1.1
     Expires: Fri, 5 May 1995 12:00:00 GMT
     Content-Type: application/json

**Example HTTP response content**

    {
      "type":"object",
      "doc":"This is an example.",
      "fields":[
        {
          "name":"count",
          "type":"number",
          "doc":"A simple step count."
        }
      ]
    }

<a href="#top">⇡ top</a>

## <a name="read">Read</a>

Provides a way to read data.

The path is `/omh/v1/<schema_id>/<schema_version>/data`. The only allowed HTTP method is GET.

Data returned by the Read API should be ordered based on some timestamp for each point and the ordering must be consistent between requests. Returning the timestamp for a point is optional, even if it is requested, so it can be completely arbitrary and not returned as long as the ordering is consistent between requests.

Certain sections of the data returned in the Read response may conform to other schema ID-version pairs. We are working on an example of this. The concept is embodied by the `conforms_to` keyword. The idea is that if a client parses the response and finds a conforms_to element in the JSON, it can utilize the conforms_to field as context on the data. There is an [open issue](https://github.com/openmhealth/developer/issues/7) to add an example of this concept to the reference implementation.

### Request

#### Parameters

The auth_token must be included in the HTTP header section as a cookie.

<table>
  <tr font="bold">
    <th>Name</th>
    <th>Description</th>
    <th>Required</th>
  </tr>
  <tr>
    <td>owner</td>
    <td>The username of the user about whom data is desired. This may or may not be the requesting user. DEPRECATED because OAuth takes care of this.</td>
    <td>no</td>
  </tr>
  <tr>
    <td>t_start</td>
    <td>A W3C ISO8601-formatted date-time string. The earliest time for the resulting data.</td>
    <td>no</td>
  </tr>
  <tr>
    <td>t_end</td>
    <td>A W3C ISO8601-formatted date-time string. The latest time for the resulting data.</td>
    <td>no</td>
  </tr>
  <tr>
    <td>column_list</td>
    <td>A comma-separated list of <a href="http://goessner.net/articles/JsonPath/">JSONPath</a> expressions. Limits the results for each point. This includes the point’s metadata and data, so all expressions should begin with <code>$.metadata</code> or <code>$.data</code></td>
    <td>no</td>
  </tr>
  <tr>
    <td>num_to_skip</td>
    <td>The number of results for skip before returning the first result. If not provided, the default is zero.</td>
    <td>no</td>
  </tr>
  <tr>
    <td>num_to_return</td>
    <td>Overrides the default page size. If it is greater than the default, the request may be rejected or the server may limit the number of entries to the default.</td>
    <td>no</td>
  </tr>
</table>

### Response 

#### Headers

The count, next URL and previous URL are part of the HTTP headers. This is done in order to allow clients to perform an HTTP HEAD request to obtain the request metadata (E.g., a client may want to fast-forward through results without having to issue the full GET).

<table>
  <tr font="bold">
    <th>Key</th>
    <th>JSON type</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>Count</td>
    <td>number</td>
    <td>The number of points being returned.</td>
  </tr>
  <tr>
    <td>Total-Count</td>
    <td>number</td>
    <td>The total number of visible elements in the list regardless of query parameters. This is only recommended, and systems that find it too arduous should attempt to give an estimate.</td>
  </tr>
  <tr>
    <td>Previous</td>
    <td>string</td>
    <td>The URL for the previous set of data. This is based on the parameters, so, if data exists before this point but there are no points between the given <code>t_start</code> and the first point, then this field may be omitted. This field may be provided even if this response and/or this URL will return no data.</td>
  </tr>
  <tr>
    <td>Next</td>
    <td>string</td>
    <td>The URL for the next set of data. This is based on the parameters, so, if data exists after this point but there are no points between the last point and <code>t_end</code>, then this field may be omitted. This field may be provided even if this response and/or this URL will return no data.</td>
  </tr>
</table>

##### Example

     HTTP/1.1 200 OK
     Server: Apache-Coyote/1.1
     Expires: Fri, 5 May 1995 12:00:00 GMT
     Content-Type: application/json
     Count: 1000
     Previous: /omh/v1/omh:schema:id/1/data? ...
     Next: /omh/v1/omh:schema:id/1/data? ...

#### Body

The response is a JSON array of JSON objects where each object is a point with the following fields.

<table>
  <tr font="bold">
    <th>Key</th>
    <th>JSON type</th>
    <th>Description</th>
  </tr>
  <tr>
    <td>metadata</td>
    <td>object</td>
    <td>This is metadata about this point. It may include any of the following fields.  
      <table>
        <tr font="bold">
          <th>Key</th>
          <th>JSON type</th>
          <th>Description</th>
        </tr>
        <tr>
          <td>id</td>
          <td>string</td>
          <td>A unique ID for this data point.</td>
        </tr>
        <tr>
          <td>timestamp</td>
          <td>string</td>
          <td>A W3C ISO8601-formatted date-time value. </td>
        </tr>
        <tr>
          <td colspan="3">This metadata section may include any other system-specific fields; however, they will be non-standard fields and their use must be explicitly defined for third-party applications.</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td>data</td>
    <td>a JSON array or object</td>
    <td>The data point’s data. This must conform to the schema definition as provided by the registry for the schema ID-version pair that identifies this point.</td>
  </tr>
</table>

**Example HTTP request header**

    GET /omh/v1/omh:ohmage:observer:edu.ucla.cens.Mobility:extended/2012050700/data
       ?num_to_skip=1&num_to_return=1 HTTP/1.1
     User-Agent: curl/7.24.0 (x86_64-apple-darwin12.0) libcurl/7.24.0 OpenSSL/0.9.8r zlib/1.2.5
     Host: example.com
     Cookie: auth_token=8fba448c-46c9-428d-a255-2d7cf2b71e45;

**Example HTTP response header**

     HTTP/1.1 200 OK
     Server: Apache-Coyote/1.1
     Expires: Fri, 5 May 1995 12:00:00 GMT
     Content-Type: application/json
     Transfer-Encoding: chunked
     Count: 1
     Previous: /omh/v1/omh:ohmage:observer:edu.ucla.cens.Mobility:extended/2012050700/data
       ?num_to_skip=0&num_to_return=1
     Next: /omh/v1/omh:ohmage:observer:edu.ucla.cens.Mobility:extended/2012050700/data
       ?num_to_skip=2&num_to_return=1

**Example HTTP response content**

       [
          {
             "metadata":{
                "id":"b2882e40-05fb-4f95-88bf-72902f72a29a",
                "timestamp":"2012-11-08T17:13:00.195-08:00"
             },
             "data":{
                "wifi_data":{
                   "scan":[
                      {
                         "ssid":"00:17:5a:b7:ef:90",
                         "strength":-56
                      },
                      {
                         "ssid":"00:1a:1e:1f:3a:24",
                         "strength":-82
                      },
                      {
                         "ssid":"00:1a:1e:1f:3c:d4",
                         "strength":-77
                      }
                   ],
                   "time":1352423550836,
                   "timezone":"America/Los_Angeles"
                },
                "speed":0,
                "accel_data":[
                   {
                      "z":-8.507708549499512,
                      "y":-0.49756529927253723,
                      "x":-4.559518337249756
                   },
                   {
                      "z":-8.392746925354004,
                      "y":-0.42092469334602356,
                      "x":-4.559518337249756
                   },
                   {
                      "z":-8.354427337646484,
                      "y":-0.5742059350013733,
                      "x":-4.559518337249756
                   },
                   {
                      "z":-8.277786254882812,
                      "y":-0.5358856320381165,
                      "x":-4.521198272705078
                   }
                ],
                "mode":"still"
             }
          }
       ]

<a href="#top">⇡ top</a>

## <a name="write">Write</a>

### Request

Provides a way to write data. This call is optional. DSUs that do not support write must return an HTTP 501 response code.

The path is `/omh/v1/<schema_id>/<schema_version>/data`, and one must use a POST to write data.

Check the [FAQ](https://github.com/openmhealth/developer/wiki/DSU-FAQ) for why write is optional.

#### Parameters

<table>
  <tr font="bold">
    <th>Name</th>
    <th>Description</th>
    <th>Required</th>
  </tr>
  <tr>
    <td>auth_token</td>
    <td>The uploading user's authentication token.</td>
    <td>true</td>
  </tr>
  <tr>
    <td>data</td>
    <td>A JSON array of JSON objects representing the data. Each JSON object should have two sections, "metadata" and "data". The "metadata" is based on the "id", "timestamp", and "location" fields and should conform to whatever the registry dictates. The "data" should conform to the schema as provided by the registry.</td>
    <td>true</td>
  </tr>
</table>

### Response

If the user is not authenticated, a HTTP 401 will be returned.

If there is any problem with the data, from the entire text not being valid JSON to an individual point having invalid metadata or data that does not conform to its schema, the entire upload will be rejected with a HTTP 400.

Otherwise, unless another HTTP status code is more appropriate, a 204 (no content) status code is returned.

**Example HTTP request header**

     POST /omh/v1/write HTTP/1.1
     User-Agent: Example Open mHealth Client 1.1.1
     Host: example.com
     Cookie: auth_token=8fba448c-46c9-428d-a255-2d7cf2b71e45;
     
     schema_id=omh:ohmage:observer:edu.ucla.cens.Mobility:extended
     &schema_version=2012050700
     &data=[JSON Array of points that adhere to the schema ID-version pair.]

**Example HTTP response header**

    HTTP 204

<a href="#top">⇡ top</a>


-------------------
mvn install:install-file -DgroupId=name.jenkins.paul.john -DartifactId=concordia -Dversion=1.2.4 -Dpackaging=jar -DgeneratePom=true -Dfile=

curl http://localhost:8080/omh-engine-1.0.0/dpu/bloodPressure ; echo ""

curl http://localhost:8080/omh-engine-1.0.0/dpu/bloodPressure -X POST -H "Content-Type: application/json" -d '{"systolic":100, "diastolic":80}' ; echo ""

