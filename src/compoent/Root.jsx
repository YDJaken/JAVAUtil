import React from 'react'
import ObjectUtil from "../util/ObjectUtil";
import Check from "../util/Check";
import ReactDOM from 'react-dom'
import DisplayTab from "./DisplayTab.jsx"
import History from "./History.jsx"
import OperationTab from "./OperationTab.jsx"
import xhrManager from "../util/XhrManager";
import HistoryManager from "./HistoryManager";


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

export default class root extends React.Component {
    constructor(obj) {
        super();
        this.setProperty(obj);
        this.historyManager = new HistoryManager(this.totalHistory);
        this.showCompareFlag = false;
        this.selectViewIndex = -1;
        this.selectCompareIndex = -1;
    }

    showCompare() {
        this.showCompareFlag = !this.showCompareFlag;
        this.rerender = true;
    }

    selectView(index) {
        if (this.selectCompareIndex === index) {
            return;
        }
        this.selectViewIndex = index;
        if (this.selectViewIndex !== -1) {
            this.rerender = true;
        }
    }

    selectCompare(index) {
        if (this.selectViewIndex === -1 || this.selectViewIndex === index) {
            return;
        }
        this.selectCompareIndex = index;
        if (this.selectCompareIndex !== -1) {
            this.rerender = true;
        }
    }

    deleteHistory(index) {
        if (this.selectViewIndex === index) {
            this.selectView(-1);
        } else if (this.selectCompareIndex === index) {
            this.selectCompare(-1);
        }
        this.historyManager.remove(index);
        this.rerender = true;
    }

    /**
     *  统一的设置入口
     * @param {Object} obj - 传入的配置项
     */
    setProperty(obj) {
        ObjectUtil.setProperties(this, obj);
    }

    set rerender(flag) {
        if (flag === true) {
            this.render();
        }
    }

    setElement(element) {
        this.element = Check.string(element) ? document.getElementById(element) : element;
        if (!this.element instanceof HTMLElement) {
            throw new Error("Plase pass a HTMLElement or HTMLElementID");
        }
        return this;
    }

    setLimitNetNumber(limitNetNumber) {
        this.limitNetNumber = Check.number(limitNetNumber) ? limitNetNumber : 5;
        this.totalHistory = this.limitNetNumber - 1;
        return this;
    }

    setManager(manager) {
        this.manager = Check.checkDefined(manager) ? manager : new xhrManager(this.limitNetNumber);
        return this;
    }

    setToken(token) {
        this.token = Check.string(token) ? token : '';
        return this;
    }

    setHostURL(hostURL) {
        this.hostURL = Check.string(hostURL) ? hostURL : "";
        return this;
    }

    sendQuery(searchKeyWord) {
        let oldHis = this.historyManager.loadByKeyWord(searchKeyWord);
        if (oldHis !== -1) {
            this.historyManager.loadHistory(oldHis);
            this.selectView(0);
            return;
        }
        let sendData = JSON.stringify({
            query: baseicQuerry,
            variables: {searchValue: searchKeyWord}
        });
        this.manager.post(`${this.hostURL}?access_token=${this.token}`, sendData, {header: {Authorization: this.token}}).then((response) => {
            if (response.status === 200) {
                let target = JSON.parse(response.response);
                target.searchKeyWord = searchKeyWord;
                this.historyManager.addHistory(target);
                this.selectView(0);
            }
        }, (error) => {
            // TODO 添加处理错误的页面逻辑
            console.log(error);
        })
    }


    render() {
        let maxHeight = Math.max(this.element.clientHeight, 300);
        ReactDOM.render(
            <div style={{
                width: '100%',
                height: '100%',
                borderSpacing: '0px',
                minWidth: "400px",
                minHeight: "300px",
                border: "none"
            }}>
                <div style={{width: '100%', float: 'left', height: '20%'}}><OperationTab root={this}/></div>
                <div style={{
                    float: 'left',
                    height: '80%',
                    maxHeight: `${maxHeight * 0.95}px`,
                    overflowX: "hidden",
                    overflowY: "scroll"
                }}><History root={this}/></div>
                <div style={{
                    float: 'left',
                    height: '80%',
                    maxHeight: `${maxHeight * 0.95}px`,
                    overflowX: "hidden",
                    overflowY: "scroll"
                }}><DisplayTab
                    root={this} viewIndex={this.selectViewIndex}/></div>
                <div style={{
                    float: 'left',
                    height: '80%',
                    display: this.showCompareFlag ? "" : "none",
                    maxHeight: `${maxHeight * 0.95}px`,
                    overflowX: "hidden",
                    overflowY: "scroll"
                }}>
                    <DisplayTab root={this} viewIndex={this.selectCompareIndex}/></div>
            </div>, this.element);
    }
}