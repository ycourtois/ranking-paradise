#!/bin/bash
set -euo pipefail

display_usage() {
    echo
    echo "Usage: $0"
    echo
    echo "--s {path_to_file} to specify maven settings file (optional)"
    echo "--localstack to start a local database (optional). Docker and docker-compose are required."
    echo "--aws-profile {aws_profile} to specify an aws profile (default to 'default')"
    echo "--p {maven_profile} to specify maven profiles (default to 'package-shade')"
    echo
}

clean() {
  echo -e "\n"
  echo -e "Cleaning existing target directory..."
  echo -e "\n"
  mvnw clean ${MAVEN_ARGS_CMD-}
}

package() {
  echo -e "\n"
  echo "Building and creating jar ..."
  echo -e "\n"
  mvnw package ${MAVEN_ARGS_CMD-}
}

run() {

  if [[ ! -z ${AWS_PROFILE-} ]] ; then
    echo -e "\n"
    echo "Using aws profile ${AWS_PROFILE} ..."
    echo -e "\n"
  fi

  if [[ ! -z ${LOCALSTACK-} ]] ; then
    export AWS_ENDPOINT="http://localhost:4566"
    echo -e "\n"
    echo "Starting local database ..."
    echo -e "\n"
    docker-compose up -d
  fi

  echo -e "\n"
  echo "Creating database table ..."
  echo -e "\n"
  java -jar target/ranking-paradise.jar init-db dw-config.yml

  echo -e "\n"
  echo "Running Web Application"
  echo -e "\n"
  java -jar target/ranking-paradise.jar server dw-config.yml
}

while [[ "${1-}" == "--"* ]] ;
do
    opt="$1";
    shift;              #expose next argument
    case "$opt" in
        "--s" )
           MAVEN_SETTINGS_CMD="-s $1"; shift;;
        "--p" )
           MAVEN_PROFILE_CMD="-P $1"; shift;;
        "--aws-profile" )
           AWS_PROFILE="$1"; shift;;
        "--localstack" )
           LOCALSTACK=true;;
        *) echo >&2 "Invalid option: $opt"; display_usage ;exit 1;;
   esac
done

MAVEN_ARGS_CMD="${MAVEN_SETTINGS_CMD-} ${MAVEN_PROFILE_CMD:-"-Ppackage-shade"} -DskipTests"

clean
package
run

