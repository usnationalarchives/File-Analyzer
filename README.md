NARA File Analyzer and Metadata Harvester
==

The NARA File Analyzer Tool walks a directory tree and performs a "File Test"
on each file that is encountered.  The application framework allows new File
Tests to be quickly developed and deployed into the application.  The results
of each File Test are compiled into a table that summarizes the results of the
analysis.
  
A File Test is a simple set of actions that are performed upon a single file
such as filename validation, file size statistical analysis, checksum
calculation, file type extraction.  Depending on the action, the content of
the file may or may not be read.  Each File Test is configured with filters
that determine which files will be processed by the File Test (i.e. only image
files).
   
Each File Test will generate a table of results.  The number of columns and
the definition of the columns will vary from test to test.  For example, a
file type analysis will report the file extension and the number of files
discovered with that extension.  The checksum file tests will report the name
of a file and the checksum string associated with that file.
    
The File Analyzer tool can be run as a GUI in which the results are
displayed in a table. The File Analyzer can also be run in batch mode.  In
batch mode, the results will be written to a tab-separated file.  The GUI
version of the application allows the results of multiple executions to be
merged.  The merged information can be filtered to display matching values
and mismatched values.
	 
## Benefits
	  
The NARA File Analyzer automates a number of simple tasks that would be
tedious to perform either manually or with other COTS applications. 
	   
The match/merge capabilities have provided a very powerful and simple
mechanism to ensure quality control checks on large numbers of files.  
	   
Note: NARA has also deployed a customized version of this application
that performs file tests that implement custom business rules such as
file name validation and metadata introspection.  
	    
## Public Domain

This project is in the public domain within the United States, and
copyright and related rights in the work worldwide are waived through
the [CC0 1.0 Universal public domain dedication](https://creativecommons.org/publicdomain/zero/1.0/).

All contributions to this project will be released under the CC0 dedication. By submitting a pull request, you are agreeing to comply with this waiver of copyright interest

For more information, see [license](https://github.com/usnationalarchives/File-Analyzer/blob/master/LICENSE.md).

## Privacy

All comments, messages, pull requests, and other submissions received through official NARA pages including this GitHub page may be subject to archiving requirements. See the [Privacy Statement](http://www.archives.gov/global-pages/privacy.html) for more information.

## Contributions

We welcome contributions. If you would like to contribute to the project you can do so by forking the repository and submitting your changes in a pull request. You can submit issues using [GitHub Issues](https://github.com/usnationalarchives/File-Analyzer/issues).

## Deployment
		 
The basic File Analyzer application is deployed as a self-extracting
jar file.  The application requires Java SE 1.6 or higher to be present
on the user's workstation.  The application can be launched by double
clicking the jar file. 
		  
If additional runtime memory is needed when running the application, a
simple windows bat file can be created to launch the application with
a larger memory allocation.

	java -mx1000m -jar fileAnalyzer.jar 
		   
Additional documentation can be found in the `doc/NARA File Analyzer and Metadata Harvester.doc` document.

