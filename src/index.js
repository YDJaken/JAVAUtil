import xhrManager from "./util/XhrManager";
import Root from "./compoent/Root.jsx"

let baseRequestCount = 5;

let manager = new xhrManager({
    netNumber: baseRequestCount
})

const token = {};

manager.get("token.json").then((data) => {
    if (data.response !== undefined) {
        token.token = JSON.parse(data.response).token;
    }
    const root = new Root({
        hostURL: "https://api.github.com/graphql",
        element: "root",
        manager: manager,
        limitNetNumber: baseRequestCount,
        token: token.token
    });
    root.render();
}, (data) => {
    console.log(data);
});
