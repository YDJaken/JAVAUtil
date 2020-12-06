import React from "react";

export default class History extends React.Component {
    constructor() {
        super();
    }

    handleClickReview(index, e) {
        this.root.selectView(index);
    }

    handleClickCompare(index, e) {
        this.root.selectCompare(index);
    }

    handleClickDelete(index, e) {
        this.root.deleteHistory(index);
    }

    render() {
        this.root = this.props.root;
        let manager = this.root.historyManager;
        let ret = [<div style={{display: "table", width: '100%'}} key={-1}>历史列表</div>];
        for (let i = 0; i < manager.history.length; i++) {
            let target = manager.history[i];
            let background = 'white';
            if (this.root.selectViewIndex === i) {
                background = 'aliceblue'
            } else if (this.root.selectCompareIndex === i) {
                background = 'antiquewhite';
            }
            ret.push(<div style={{display: "table", width: '100%', background: background}} key={i}>
                <div className={"innerCell"}>关键词:</div>
                <div className={"innerCell"}>{target.searchKeyWord}</div>
                <div className={"innerCell"}>
                    <button onClick={this.handleClickReview.bind(this, i)}>回顾</button>
                </div>
                <div className={"innerCell"} style={{display: this.root.showCompareFlag ? "table-cell" : "none"}}>
                    <button onClick={this.handleClickCompare.bind(this, i)}>对比</button>
                </div>
                <div className={"innerCell"}>
                    <button onClick={this.handleClickDelete.bind(this, i)}>删除</button>
                </div>
            </div>)
        }
        return ret;
    }
}