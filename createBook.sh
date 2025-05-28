#!/bin/sh

JSON='{"foo":18, "bar": "Baz", "copiesSold": 42, "timestamp": "2025-05-23T12:34:56Z"}'
#curl -s 'http://localhost:8080/createbook?title=Pride%20and%20Prejudice&authorName=Jane%20Austen&price=19.99' \
#--header 'Content-Type: application/json' \
#--data "$JSON" | jq

curl -s -X POST \
  'http://localhost:8080/createbook?title=Pickle%20and%20Pumba&authorName=Jane%20Austen&price=19.99' \
  | jq
