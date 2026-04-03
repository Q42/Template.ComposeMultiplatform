#!/bin/bash

# Script to create a git branch from a Jira issue key (aka ticket number
# Usage: ./branch.sh ISSUE_KEY

set -e

# Check if issue key is provided
if [ -z "$1" ]; then
    echo "Error: Jira Issue key (aka ticket number) not provided"
    echo "Usage: $0 ISSUE_KEY"
    exit 1
fi

ISSUE_KEY=$1

# Check if acli is installed
if ! command -v acli &> /dev/null; then
    echo "Jira CLI (acli) not found. Installing..."

    # Check if brew is installed
    if ! command -v brew &> /dev/null; then
        echo "Error: Homebrew is not installed. Please install Homebrew first."
        exit 1
    fi

    # Install jira CLI
    echo "Adding Atlassian tap..."
    brew tap atlassian/homebrew-acli

    echo "Installing jira CLI..."
    brew install acli
fi

# Check if user is logged in to acli
if ! acli auth status &> /dev/null; then
    echo "Not logged in to Jira. Logging in..."
    acli auth login
fi

echo "Fetching issue summary for $ISSUE_KEY..."
WORKITEM=$(acli jira workitem view "$ISSUE_KEY")

TYPE=$(echo "$WORKITEM" | grep "^Type:" | cut -d' ' -f2-)
SUMMARY=$(echo "$WORKITEM" | grep "^Summary:" | cut -d' ' -f2- | tr ' ' '-' | tr '[:upper:]' '[:lower:]' | sed 's/[^a-z0-9-]//g')

if [ -z "$SUMMARY" ]; then
    echo "Error: Could not fetch summary for issue $ISSUE_KEY"
    exit 1
fi

# Map issue type to branch prefix
case "$TYPE" in
    Bug)       PREFIX="fix" ;;
    Story)     PREFIX="feature" ;;
    Task)      PREFIX="chore" ;;
    Android)   PREFIX="feature" ;;
    iOS)       PREFIX="feature" ;;
    Backend)   PREFIX="feature" ;;
    *)         PREFIX="feature" ;;
esac

BRANCH="$PREFIX/$ISSUE_KEY-$SUMMARY"

echo "Creating git branch: $BRANCH"
git checkout -b "$BRANCH"

echo "Successfully created and checked out branch: $BRANCH"
