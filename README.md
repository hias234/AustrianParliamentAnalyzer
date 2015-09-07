# AustrianParliamentAnalyzer

The goal of this project (for my bachelor thesis) is to create an application which extracts unstructured data (in the form of HTML and plain text) from the website of the Austrian parliament (http://www.parlament.gv.at/PAKT/STPROT/), transforms it into a structured form (e.g. Java objects) and analyzes the data afterwards (which analysis is not fully clear yet).

### Extraction of the Data from the Parliament
The following data should be extracted from the website of the Austrian parliament and from protocols of the national council:
* Legislative Periods
* Sessions of each legislative period
* Politicians of the national council
* Parliament Clubs/Parties
* Discussions during the sessions
* Speeches of politicians (plus: analyze sentiment)

This data is available in form of RSS feeds which provide lists of HTML files containing the information of the just mentioned information. The application has to read the feeds, download the referred HTML files and transform its unstructured content into structured data (Java objects).

### Export
The result of the extraction will be exported in a database using JPA (I use postgres).

### Analysis on the Data
With the now available data there should be lots of possibilities for analysis: Here are only a few ideas what can be analyzed:
* Find the politician, who is serving longest in the national council
* Find the most active politicians (take part in the most discussions and sessions)
* Find groups of politicians, which discuss similar topics
* Find groups of politicians, which have similar attitudes
* Analyze how homogeneous the attitudes of politicians of the same club/party are

### Visualization of the Analysis
The analysis will be visualized in form of a web application.

