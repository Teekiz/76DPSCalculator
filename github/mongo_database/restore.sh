#!/bin/bash

# Check if the database already exists
if ! mongo --username root --password example --authenticationDatabase admin --eval "db.getMongo().getDBNames().indexOf('76dpscalculatordb') >= 0"; then
  echo "Database not found. Restoring from dump..."
  mongorestore --username root --password example --authenticationDatabase admin --db 76dpscalculatordb /dump
else
  echo "Database already exists. Skipping restore."
fi