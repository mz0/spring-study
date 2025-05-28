```
The query used a deprecated function. ('id' has been replaced by 'elementId or an application-generated id')

OPTIONAL MATCH (hlp:`Book`) WHERE id(hlp) = $__id__ WITH hlp WHERE hlp IS NULL CREATE (book:`Book`) ...
                                  ^

MERGE (startNode)-[relProps:`WRITTEN_BY`]->(endNode) RETURN id(relProps) AS __elementId__
                                                            ^
```
