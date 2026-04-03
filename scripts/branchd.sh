#!/bin/bash

# Script to fetch the base branch and create a feature branch from a Jira issue key (aka ticket number)
# Usage: BASE_BRANCH=<branch> ./branchd.sh ISSUE_KEY

set -e

# Check if issue key is provided
if [ -z "$1" ]; then
    echo "Error: Jira Issue key (aka ticket number) not provided"
    echo "Usage: BASE_BRANCH=<branch> $0 ISSUE_KEY"
    exit 1
fi

ISSUE_KEY=$1
BASE_BRANCH=${BASE_BRANCH:-$(git symbolic-ref --quiet --short refs/remotes/origin/HEAD 2>/dev/null | sed 's|^origin/||')}

if [ -z "$BASE_BRANCH" ]; then
    echo "Error: Could not determine base branch from origin/HEAD. Set BASE_BRANCH explicitly."
    exit 1
fi

echo "Fetching base branch: $BASE_BRANCH..."
git fetch origin "$BASE_BRANCH"

echo "Checking out base branch: $BASE_BRANCH..."
git checkout "$BASE_BRANCH"
echo "Updating local develop branch..."
git merge --ff-only origin/develop
echo "Calling branch.sh with issue key: $ISSUE_KEY..."
"$(dirname "$0")/branch.sh" "$ISSUE_KEY"

