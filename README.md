# SearchEngie-CSE272-UCSC

Tanuj Gupta:
Code: Github
A) Design decisions and high-level software
architecture
Teck Stack
The language used: java
Project management tool: maven IDE: Intellij
Version control: Github
Programming tools or libraries
Lucene version 9.1.0 ->
Lucene-core, lucene-analyzers-common, lucene-queryparser
Design decisions and high-level software architecture
The program is written following object-oriented principles to make it more manageable and readable. The program is divided into seven packages:
1. Parsing:
   This package parses query, document, and relevance file. To hold different properties of each query and document, I have created two classes(value objects), namely ParsedDocument and ParsedQuery
2. VO:
   This package defines all the classes to hold objects. It follows popular design principle of the value object. It has 3 main classes:
   a) ParsedQuery: to hold properties of each query
   b) ParsedDocument: to hold properties of each parsed document
   c) LogFileResultRow: to hold a single row of final results in the format defined for logfile
3. SearchEngine:
   It is the main package which has 3 classes:
   a) Indexer: This class indexes all the parsed documents in byteBufferIndex (in

RAM). It uses lucenes standard analyzer that removes stop worlds and perform stemming and lemmetization
b) Reader and Searcher: This class is used to query index created by indexer. In constructor, I also calculate document Frequencuies for all the terms to be used while ranking document using my own algorithm.
4. Ranking:
   This package defines all the 5 ranking algorithms, namely (Boolean, TF, TF-IDF, Relevance feedback, and Own algorithm)
5. VoTransformation:
   This package is used for conversion among different class objects. For instance, function convertParsedDocumentToDocument function inside VoTransformations
   Class is used to convert my own created ParsedDocument object to lucenes document object.
6. Run:
   This package is used to run everything. It contains main method, where every java program starts. This is what make all the required function calls.
7. Comparator:
   This package defines comparison functions for objects necessary to be able to sort them.
   B) Implementation of ranking algorithms:
1. Boolean: Program uses used lucenes booleanSimilarity for Boolean ranking
2. Tf-Idf: Program uses used lucenes classicSimilarity that implements interface
   TfIDFSimilarity for Boolean Tf-Idf:
3. Tf: I have created my own class called TfSimilarity for this. TfSimilarity also
   implements TfIDFSimilarity interface like lucenes classicSimilarity class. The only difference is that the method that returns idf is overridden and made to return 1 in TfSimilarity.
4. Relevance Feedback: For this, I have used Pseudo_Relevance_Feedback. The program first fetches the top 5 documents using lucenes Tf-Idf method. Then the original query is expanded using these 5 documents. This query is then used to fetch the top 50 final documents using lucenes Tf-Idf method.

C) Strengths and weaknesses of your design:
Strengths:
1. The program uses a well-established Lucene library to build the search engine. It gives a lot of in-built functionalities that we can use to add more enhanced features to the current code.
2. Java follows an object-oriented pattern, which makes application development very understandable and developer-friendly.
   Weaknesses:
1. Using Lucene library also comes with a big disadvantage:
   Here the developer has less control over so many aspects. Understanding the internals of Lucene is very difficult and may hinder writing one’s own optimization on so many aspects.
   Lucene also requires a lot of knowledge of its classes to be able to use them.
2. Java doesn't provide many Machine learning modules and libraries. So, adding machine learning is challenging when using Java. In that case, the combination of python and java seems more suitable, like pylucene.
   D) Customized new algorithm:
- The algorithm is the combination of pseudo-relevance-feedback, relevance-feedback, and optimized tf-idf
- It follows these three steps in order:
1. Relevance-feedback:
   1.1) Randomly pick two relevant documents for a query from the relevance-file shared
   1.2) Use these two documents to expand the query
2. Pseudo-relevance-feedback:
   2.1) Now Use the expanded query generated in 1st step to fetch the top 5 documents using normal tf-idf.
   2.2) Among these five documents, select the ones that are different from the two documents used in the first step.
   2.3) Use these selected documents to further expand the query.
3. Optimized tf-idf

- Optimized tf-idf uses properties of a query to enhance ranking. Every query in the query file given has three properties, namely, query_number(num), title, and description(desc).
- Instead of giving equal weightage to every term in a query, Optimized tf-idf gives more weightage to terms in the title, unlike normal tf-idf.
- To give extra weights to the terms in the title, the algorithm simply multiples constant C with term_frequency to give the term more weight. Value for constant C is decided as below:
  a) If the term is present in both the title and description, multiply the term frequency by 3, i.e., constant C=3
  b) If the term is present in the title, multiply the term frequency by 2, i.e, constant C=3
  c) If the term is only present in the description, constant C=1. That is, the term frequency is used as-is.

E) Experimental results: 1. Boolean:

2. Tf:

3. Tf-IDF:

4. Relevance Feedback:

5. Own:

Patterns observed:
- Recall increases with rank.
- Precision decrease with the rank.
- Pseudo relevance and relevance feedback seem to have higher precision in the
  selection of the top documents in comparison to other algorithms.
- Although algorithms have similar performance when selecting higher-ranked
  documents.
- The terms in the title seem to have more importance than the terms in the
  description.
- The performance of the five algorithms is as follows:
  Own > Relevance feedback > Pseudo relevance feedback > Tf-Idf > Tf > Boolean
  F) What you have learned from this assignment:
1. Implementing search engine:
   a. Learned how to configure a Lucene project in Java
   b. Leaned to create both filesystem and ram index using Lucene
   c. Learned to update Lucene index to hold extra useful values such as
   document frequency, term vectors, positional index etc
   d. Learned to query from the index using Lucene
   e. Learned to use various ranking algorithms given by Lucene to rank
   fetched documents
   f. Learned to update/override Lucene classes to create own ranking
   algorithms
2. Application development design principles:
   a. Learned to create a maven project in Java.
   b. Learned to use SOLID design principles.
   c. Learned various concepts like value object, POJO(Plain old java objects)
   etc while developing search engine application
   d. Learned basic oops concepts like polymorphism, inheritance etc

3. Evaluation:
   a. Got on hands experience with evaluating search engines.
   b. Learned to use reac_eval to evaluate search engine performance.
   c. Learned to use various evaluation results to come up with a better
   algorithm.
   d. Learned various patterns of recall/precision shown by search engine
   ranking algorithms.
