#!/bin/bash

function dump {
    DB=$1
    sqlite3 $DB ".dump" > "$DB.txt"
    echo "$DB.txt"
}

function compress {
    TARGET=$1
    lzma $TARGET
}