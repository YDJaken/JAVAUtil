import React from 'react'
import ReactDOM from 'react-dom'
import xhrManager from "./util/xhrManager";

const baseicQuerry = `query testQuary($searchValue: String!) {
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

let baseRequestCount = 5;

let manager = new xhrManager({
    netNumber: baseRequestCount
})

const token = {};
const value = "checkCount"

manager.get("token.json").then((data) => {
    if (data.response !== undefined) {
        token.token = JSON.parse(data.response).token;
    }
    let sendData = JSON.stringify({
        query: baseicQuerry,
        variables: {searchValue: value}
    });
    manager.post(`https://api.github.com/graphql?access_token=${token.token}`, sendData, {header: {Authorization: token.token}});
    ReactDOM.render(<div>test 123</div>, document.getElementById("root"));
}, (data) => {
    console.log(data);
});
