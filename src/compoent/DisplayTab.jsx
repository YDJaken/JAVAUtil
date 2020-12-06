import React from "react";
import Check from "../util/Check";

export default class DisplayTab extends React.Component {
    constructor() {
        super();
        this.inputValue = "";
        this.intervalID = -1;
        this.order = true;
    }

    handleChange(e) {
        this.inputValue = e.target.value;
    }

    handleKeyUp(e) {
        clearInterval(this.intervalID);
        this.intervalID = setInterval(() => {
            clearInterval(this.intervalID);
            this.intervalID = -1;
            this.root.rerender = true;
        }, 500)
    }

    handleReorder(e) {
        this.order = !this.order;
        this.root.rerender = true;
    }

    static StringCompare(nameA, nameB) {
        let lengthA = nameA.length();
        let lengthB = nameB.length();
        let i = 0
        while (i < lengthA) {
            if (i >= lengthB) {
                return 1
            } else if (nameA[i] > nameB[i]) {
                return 1;
            } else if (nameA[i] < nameB[i]) {
                return -1;
            }
        }
        return lengthB > lengthA ? -1 : 0;
    }

    static numberCompare(a, b) {
        if (a > b) {
            return 1;
        } else if (a < b) {
            return -1;
        } else {
            return 0;
        }
    }

    render() {
        this.root = this.props.root;
        let manager = this.root.historyManager;
        let viewIndex = this.props.viewIndex;
        let ret = [<div style={{display: "table", width: '100%'}} key={-1}>列表 <button
            onClick={this.handleReorder.bind(this)}>修改排序</button>  <input
            onChange={this.handleChange.bind(this)} onKeyUp={this.handleKeyUp.bind(this)} placeholder={"请输入项目介绍内过滤字"}/>
        </div>];
        let target = manager.loadHistory(viewIndex);
        if (Check.checkDefined(target)) {
            target = [...target.data.search.nodes];
            target.sort((a, b) => {
                if (this.order) {
                    return DisplayTab.numberCompare(a.stargazerCount, b.stargazerCount);
                } else {
                    return DisplayTab.numberCompare(a.forkCount, b.forkCount);
                }
            })
            for (let i = 0; i < target.length; i++) {
                let aim = target[i];
                if (this.inputValue !== "" && Check.checkDefined(aim.description) && aim.description.indexOf(this.inputValue) === -1) {
                    continue;
                }
                ret.push(
                    <div style={{display: "table-row", width: '100%'}} key={i}>
                        <div className={"innerCell"}>{`start:${aim.stargazerCount},fork:${aim.forkCount}`}</div>
                        <div className={"innerCell"}><a href={aim.url}>项目名:</a>{aim.name}</div>
                        <div className={"innerCell"}>项目介绍:{aim.description}</div>
                    </div>)
            }
        }
        return ret;
    }
}