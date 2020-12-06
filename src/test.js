import React from 'react'
import ReactDOM from 'react-dom'

import {ApolloClient, InMemoryCache, gql} from '@apollo/client';

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

let xhrs = [];
for (let i = 0; i < baseRequestCount; i++) {
    xhrs.push(new XMLHttpRequest())
}

const token = {};
const value = "checkCount"
const testCount = 30;

let counter = 0;
//index.js:39 1:  4991.13818359375 ms 同时开30个请求 共请求30次 用时5秒
//1: 6498.072021484375 ms 同时开10个请求 共30次
function loadData(data) {
    if (++counter >= testCount) {
        console.timeEnd("1");
    } else {
        if (counter + baseRequestCount > testCount) {
            return;
        }
        let index = xhrs.indexOf(data.currentTarget);
        xhrs[index].open("POST", `https://api.github.com/graphql?access_token=${token.token}`)

        xhrs[index].setRequestHeader("Authorization", token.token)

        xhrs[index].send(JSON.stringify({
            query: baseicQuerry,
            variables: {
                searchValue: value + counter
            }
        }));
    }
}

xhrs[0].onload = (data) => {
    if (data.currentTarget.response !== undefined) {
        token.token = JSON.parse(data.currentTarget.response).token;
    }

    console.time("1");
    for (let i = 0; i < baseRequestCount; i++) {
        xhrs[i].onload = loadData;
        xhrs[i].open("POST", `https://api.github.com/graphql?access_token=${token.token}`)
        xhrs[i].setRequestHeader("Authorization", token.token)
        xhrs[i].send(JSON.stringify({
            query: baseicQuerry,
            variables: {
                searchValue: value + i
            }
        }));
    }

    // window.client = new ApolloClient({
    //     uri: `https://api.github.com/graphql?access_token=${token.token}`,
    //     headers: {
    //         Authorization: token.token
    //     },
    //     cache: new InMemoryCache(),
    //     // 重复请求使用cache
    //     queryDeduplication: true,
    //     defaultOptions: {
    //         watchQuery: {
    //             fetchPolicy: "cache-first",
    //             errorPolicy: 'all'
    //         },
    //         query: {
    //             fetchPolicy: "cache-first",
    //             errorPolicy: 'all',
    //         },
    //         mutate: {
    //             errorPolicy: 'all',
    //         }
    //     }
    // });

    // index.js:123 2: 7401.730224609375 ms 同时30个请求 用时7秒
//     let counter2 = 0;
//     console.time("2");
//     for (let i = 0; i < testCount; i++) {
//         client.query({
//             query: gql`{
//   search(query: "want${i}", type: REPOSITORY, first: 15) {
//     nodes {
//       ... on Repository {
//         url
//         nameWithOwner
//         description
//       }
//       ... on App {
//         id
//         name
//         description
//         url
//       }
//     }
//   }
// }`
//         }).then((data) => {
//             if (++counter2 === testCount) {
//                 console.timeEnd("2");
//             }
//         })
//     }


    ReactDOM.render(<div>test 123</div>, document.getElementById("root"))
}

xhrs[0].open("GET", "token.json");

xhrs[0].send();