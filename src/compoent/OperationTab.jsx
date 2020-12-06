import React from "react";


export default class OperationTab extends React.Component {
    constructor() {
        super();
        this.inputValue = "";
        this.intervalID = -1;
    }

    handleKeyUp(e) {
        clearInterval(this.intervalID);
        this.intervalID = setInterval(() => {
            clearInterval(this.intervalID);
            this.intervalID = -1;
            this.handleSearch();
        }, 2000)
    }

    handleChange(e) {
        this.inputValue = e.target.value;
    }

    handleSearch(e) {
        clearInterval(this.intervalID);
        if (this.inputValue !== "") {
            this.root.sendQuery(this.inputValue);
        }
    }

    render() {
        this.root = this.props.root;
        return [<input onChange={this.handleChange.bind(this)} onKeyUp={this.handleKeyUp.bind(this)} placeholder={"请输入关键字"} key={1}/>,
            <button onClick={this.handleSearch.bind(this)} key={2}>搜索</button>,
            <button onClick={this.root.showCompare.bind(this.root)} key={3}>对比模式</button>];
    }
}