#!/bin/bash

# Script to fetch develop branch and create a feature branch from a Jira issue key (aka ticket number)
# Usage: ./branchd.sh ISSUE_KEY

set -e

# Check if issue key is provided
if [ -z "$1" ]; then
    echo "Error: Jira Issue key (aka ticket number) not provided"
    echo "Usage: $0 ISSUE_KEY"
    exit 1
fi

ISSUE_KEY=$1

echo "Fetching develop branch..."
git fetch origin develop

echo "Checking out develop branch..."
git checkout develop

echo "Updating local develop branch..."
git merge --ff-only origin/develop
echo "Calling branch.sh with issue key: $ISSUE_KEY..."
"$(dirname "$0")/branch.sh" "$ISSUE_KEY"

