import React from 'react'
import ReactDOM from 'react-dom'

import { ApolloClient, InMemoryCache } from '@apollo/client';

const client = new ApolloClient({
    uri: 'https://48p1r2roz4.sse.codesandbox.io',
    cache: new InMemoryCache()
});

ReactDOM.render(<div>test 123</div>, document.getElementById("root"))