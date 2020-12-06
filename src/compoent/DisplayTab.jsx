import React from "react";
import Check from "../util/Check";

export default class DisplayTab extends React.Component {
    constructor() {
        super();
    }

    render() {
        this.root = this.props.root;
        let manager = this.root.historyManager;
        let viewIndex = this.props.viewIndex;
        let ret = [<div style={{display: "table", width: '100%'}} key={-1}>列表</div>];
        let target = manager.loadHistory(viewIndex);
        if (Check.checkDefined(target)) {
            target = target.data.search.nodes;
            for (let i = 0; i < target.length; i++) {
                let aim = target[i];
                ret.push(
                    <div style={{display: "table-row", width: '100%'}} key={i}>
                        <div className={"innerCell"}>作者/项目名:</div>
                        <div className={"innerCell"}><a href={aim.url}>link</a>{aim.nameWithOwner}</div>
                        <div className={"innerCell"}>项目介绍:</div>
                        <div className={"innerCell"}>{aim.description}</div>
                    </div>)
            }
        }
        return ret;
    }
}