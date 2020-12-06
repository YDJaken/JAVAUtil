import React from 'react'
import ReactDOM from 'react-dom'

import {ApolloClient, InMemoryCache, gql} from '@apollo/client';

const baseicQuerry = gql`query testQuary($searchValue: String!) {
  search(type: REPOSITORY, first: 15, query: $searchValue) {
    nodes {
      ... on Repository {
        url
        nameWithOwner
        description
      }
      ... on App {
        id
        name
        description
        url
      }
    }
  }
}`

let baseRequestCount = 1;

let xhrs = [];
for (let i = 0; i < baseRequestCount; i++) {
    xhrs.push(new XMLHttpRequest())
}

const token = {};
const value = "checkCount"

xhrs[0].onload = (data) => {
    if (data.currentTarget.response !== undefined) {
        token.token = JSON.parse(data.currentTarget.response).token;
    }

    ReactDOM.render(<div>test 123</div>, document.getElementById("root"))
}

xhrs[0].open("GET", "token.json");

xhrs[0].send();